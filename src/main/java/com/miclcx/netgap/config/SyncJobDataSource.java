package com.miclcx.netgap.config;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 网闸配置需要同步的多数据源
 */
public class SyncJobDataSource {

    /**
     * 多数据源
     */
    public static Map<String,DataSource> dataSourceMap = new ConcurrentHashMap<>();

    /**
     * 多JdbcTemplate
     */
    public static Map<String,JdbcTemplate> jdbcTemplateMap = new ConcurrentHashMap<>();

}
