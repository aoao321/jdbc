package com.atguigu.connection;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author aoao
 * @create 2025-01-12-18:25
 */
public class DruidTest {

    @Test
    public void test() throws Exception {
        InputStream is = DruidTest.class.getClassLoader().getResourceAsStream("druid.properties");
        Properties pros = new Properties();
        pros.load(is);

        DataSource dataSource = DruidDataSourceFactory.createDataSource(pros);
        System.out.println(dataSource.getConnection());

    }
}
