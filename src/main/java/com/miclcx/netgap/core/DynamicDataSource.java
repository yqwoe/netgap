package com.miclcx.netgap.core;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DynamicDataSource {

    public DataSource createDataSource(String url,String username,String password){
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .build();
    }

    public JdbcTemplate createJdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
