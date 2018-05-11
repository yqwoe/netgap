package com.miclcx.netgap;

import com.miclcx.netgap.core.SyncJobDataSourceHelper;
import com.miclcx.netgap.entity.SyncJobConfig;
import com.miclcx.netgap.service.SyncDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration
@EnableAsync
@EnableScheduling
public class App {

    private Logger logger =  LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SyncDataService syncDataService;
    @Autowired
    private SyncJobDataSourceHelper syncJobDataSourceHelper;

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(
                App.class, args);
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2*Runtime.getRuntime().availableProcessors());
        executor.setQueueCapacity(Integer.MAX_VALUE);

        return executor;
    }
    @Bean
    public ThreadPoolTaskScheduler scheduleExecutor() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(2*Runtime.getRuntime().availableProcessors());
        return executor;
    }

    @PostConstruct
    void init(){
        logger.info("---------------App init----------------");

        // 获取所有启用的同步任务
        List<SyncJobConfig> list = syncDataService.getAllSyncJobConfig();

        // 初始化数据同步任务的DataSource
        initSyncDataSource(list);

        // 初始化目标数据库、数据表
        initTargetDatabase(list);

        // 启动数据同步任务
        startSyncJob(list);

        logger.info("---------------END init----------------");
    }

    /**
     * 启动数据同步任务
     * @param list
     */
    private void startSyncJob(List<SyncJobConfig> list){
        for(SyncJobConfig syncJobConfig : list){
            syncDataService.startSyncJob(syncJobConfig);
        }
    }

    /**
     * 初始化目标数据库、数据表
     * @param list
     */
    private void initTargetDatabase(List<SyncJobConfig> list){
        for(SyncJobConfig syncJobConfig : list){
            syncDataService.initTargetDatabase(syncJobConfig);
        }
    }

    /**
     * 初始化数据同步任务的DataSource
     */
    private void initSyncDataSource(List<SyncJobConfig> list){

        for(SyncJobConfig syncJobConfig : list){

            // 创建源数据库DataSource和JdbcTemplate
            syncJobDataSourceHelper.createSourceJdbcTemplate(syncJobConfig.getSourceDataSourceKey(),syncJobConfig.getSourceDbHost(),syncJobConfig.getSourceDbPort(),
                    syncJobConfig.getSourceDbName(),syncJobConfig.getSourceDbUsername(),syncJobConfig.getSourceDbPassword());

            // 创建目标数据库DataSource和JdbcTemplate
            syncJobDataSourceHelper.createSourceJdbcTemplate(syncJobConfig.getTargetDataSourceKey(),syncJobConfig.getTargetDbHost(),syncJobConfig.getTargetDbPort(),
                    syncJobConfig.getTargetDbName(),syncJobConfig.getTargetDbUsername(),syncJobConfig.getTargetDbPassword());
        }

    }
}
