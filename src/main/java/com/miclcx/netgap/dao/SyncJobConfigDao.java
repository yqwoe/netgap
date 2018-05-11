package com.miclcx.netgap.dao;

import com.miclcx.netgap.entity.SyncJobConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SyncJobConfigDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<SyncJobConfig> getAll(){
        String sql = "select * from sync_job_config where enable=1";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(SyncJobConfig.class));
    }

}
