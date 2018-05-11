package com.miclcx.netgap.core;

import com.miclcx.netgap.config.SyncJobDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class SyncJobDataSourceHelper {


    private Logger logger =  LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DynamicDataSource dynamicDataSource;


    /**
     * 创建数据DataSource和JdbcTemplate
     * @return
     */
    public void createSourceJdbcTemplate(String key,String dbHost,int dbPort,String dbName,
                                         String username,String password){
        String url = "jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull";
        url = String.format(url,dbHost,dbPort,dbName);

        DataSource dataSource = SyncJobDataSource.dataSourceMap.get(key);

        if(dataSource == null){

            dataSource = dynamicDataSource.createDataSource(url,username,password);
            SyncJobDataSource.dataSourceMap.put(key,dataSource);
            logger.info("create DataSource {} success!",key);
        }

        JdbcTemplate jdbcTemplate = SyncJobDataSource.jdbcTemplateMap.get(key);
        if(jdbcTemplate == null){
            SyncJobDataSource.jdbcTemplateMap.put(key,dynamicDataSource.createJdbcTemplate(dataSource));
            logger.info("create JdbcTemplate {} success!",key);
        }

    }

}
