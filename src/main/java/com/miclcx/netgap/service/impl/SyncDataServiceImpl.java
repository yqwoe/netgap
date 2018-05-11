package com.miclcx.netgap.service.impl;

import com.miclcx.netgap.config.SyncJobDataSource;
import com.miclcx.netgap.contants.AppConstants;
import com.miclcx.netgap.core.DynamicDataSource;
import com.miclcx.netgap.dao.SyncJobConfigDao;
import com.miclcx.netgap.entity.SyncJobConfig;
import com.miclcx.netgap.service.SyncDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SyncDataServiceImpl implements SyncDataService {

    private Logger logger =  LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SyncJobConfigDao syncJobConfigDao;
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Autowired
    private DynamicDataSource dynamicDataSource;


    @Override
    public List<SyncJobConfig> getAllSyncJobConfig() {
        return syncJobConfigDao.getAll();
    }

    @Override
    public void startSyncJob(final SyncJobConfig syncJobConfig) {

        threadPoolTaskScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                JdbcTemplate sourceJdbcTemplate = SyncJobDataSource.jdbcTemplateMap.get(syncJobConfig.getSourceDataSourceKey());
                JdbcTemplate targetJdbcTemplate = SyncJobDataSource.jdbcTemplateMap.get(syncJobConfig.getTargetDataSourceKey());
                // 获取目标数据表最大ID
                Long maxTargetId = targetJdbcTemplate.queryForObject("select MAX(id) from "+syncJobConfig.getTargetTableName(),Long.class);
                // 获取源数据表中需要同步的数据
                String sqlGetSourceData = "select * from "+syncJobConfig.getSourceTableName()+" where 1=1 ";
                List sqlGetSourceDataArgs = new ArrayList();
                if(maxTargetId != null ){
                    sqlGetSourceData += " and id > ?";
                    sqlGetSourceDataArgs.add(maxTargetId);
                }
                sqlGetSourceData += " limit "+AppConstants.PER_SYNC_ROW_LIMIT;
                SqlRowSet rs = sourceJdbcTemplate.queryForRowSet(sqlGetSourceData,sqlGetSourceDataArgs.toArray());
                String sqlInsertTargetData = "insert into "+syncJobConfig.getTargetTableName()+" values(";
                // 表有多少列
                int columnCount = rs.getMetaData().getColumnCount();
                // 需要插入的数据
                List<Object[]> sqlInsertTargetDataArgs = new ArrayList<>();
                for(int i = 0;i < columnCount; i++){
                    if(i == 0){
                        sqlInsertTargetData += " ?";
                    }else{
                        sqlInsertTargetData += " ,?";
                    }

                }
                sqlInsertTargetData += " )";

                while(rs.next()){
                    Object[] obj = new Object[columnCount];
                    // 列索引从1开始
                    for(int i = 0 ;i < obj.length ; i++){
                        obj[i] = rs.getObject(i+1);
                    }
                    sqlInsertTargetDataArgs.add(obj);
                }
                if(!CollectionUtils.isEmpty(sqlInsertTargetDataArgs)){
                    logger.info("db {} table {} maxTargetId is {}",syncJobConfig.getTargetDbHost()+":"+syncJobConfig.getTargetDbPort()
                            ,syncJobConfig.getTargetDbName()+"."+syncJobConfig.getTargetTableName()
                            ,maxTargetId);
                    logger.info("sqlInsertTargetData is {}",sqlInsertTargetData);
                }
                // 插入数据到目标表
                batchInsert(targetJdbcTemplate,sqlInsertTargetData,sqlInsertTargetDataArgs);
            }
        },10);

    }

    /**
     * 批量插入数据到目标库
     * @param sql
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    private void batchInsert(JdbcTemplate targetJdbcTemplate,String sql,List<Object[]> list){
        // 插入数据到目标表
        targetJdbcTemplate.batchUpdate(sql,list);
    }

    @Override
    public void initTargetDatabase(SyncJobConfig syncJobConfig) {
        logger.info("--------------------initTargetDatabase");
        // 创建目标数据库
        createTargetDatabase(syncJobConfig);
        // 创建目标数据表
        createTargetTable(syncJobConfig);
    }

    /**
     * 创建目标数据库
     * @param syncJobConfig
     */
    private void createTargetDatabase(SyncJobConfig syncJobConfig){
        logger.info("--------------------createTargetDatabase");
        JdbcTemplate sourceJdbcTemplate = SyncJobDataSource.jdbcTemplateMap.get(syncJobConfig.getSourceDataSourceKey());

        SqlRowSet rs = sourceJdbcTemplate.queryForRowSet("show create database "+syncJobConfig.getSourceDbName());

        // 获取目标mysql数据源
        String url = "jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull";
        url = String.format(url,syncJobConfig.getTargetDbHost(),syncJobConfig.getTargetDbPort(),"mysql");

        DataSource targetDataSource = dynamicDataSource.createDataSource(url,syncJobConfig.getTargetDbUsername(),syncJobConfig.getTargetDbPassword());

        JdbcTemplate targetJdbcTemplate = dynamicDataSource.createJdbcTemplate(targetDataSource);

        while(rs.next()){
            System.out.println(rs.getString(2));
            String createDatabaseSql = rs.getString(2);
            // 加入IF NOT EXISTS
            createDatabaseSql = createDatabaseSql.replace("DATABASE `"+syncJobConfig.getSourceDbName()+"`",
                    "DATABASE IF NOT EXISTS `"+syncJobConfig.getTargetDbName()+"`");

            logger.info("-----------------------------------------------------------");
            logger.info(createDatabaseSql);
            logger.info("-----------------------------------------------------------");
            // 创建目标数据库
            targetJdbcTemplate.execute(createDatabaseSql);

        }

        logger.info("--------------------END createTargetDatabase");

    }
    /**
     * 创建目标数据表
     * @param syncJobConfig
     */
    private void createTargetTable(SyncJobConfig syncJobConfig){
        logger.info("--------------------createTargetDatabase");
        JdbcTemplate sourceJdbcTemplate = SyncJobDataSource.jdbcTemplateMap.get(syncJobConfig.getSourceDataSourceKey());

        SqlRowSet rs = sourceJdbcTemplate.queryForRowSet("show create table "+syncJobConfig.getSourceTableName());

        JdbcTemplate targetJdbcTemplate = SyncJobDataSource.jdbcTemplateMap.get(syncJobConfig.getTargetDataSourceKey());

        while(rs.next()){
            System.out.println(rs.getString(2));
            String createDatabaseSql = rs.getString(2);
            // 加入IF NOT EXISTS
            createDatabaseSql = createDatabaseSql.replace("TABLE `"+syncJobConfig.getSourceTableName()+"`",
                    "TABLE IF NOT EXISTS `"+syncJobConfig.getTargetTableName()+"`");

            logger.info("-----------------------------------------------------------");
            logger.info(createDatabaseSql);
            logger.info("-----------------------------------------------------------");
            // 创建目标数据表
            targetJdbcTemplate.execute(createDatabaseSql);

        }

        logger.info("--------------------END createTargetDatabase");

    }


}
