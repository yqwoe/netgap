package com.miclcx.netgap;

import com.miclcx.netgap.entity.SyncJobConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SqlTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testSql(){
        String sql = "select * from sync_job_config";
        System.out.println(jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(SyncJobConfig.class)));
    }
}
