package com.atguigu.java;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author aoao
 * @create 2025-01-01-17:21
 */
public class ConnectionTest {
    @Test
    public void test1() throws SQLException {

        Driver driver = new com.mysql.jdbc.Driver();

        //jdbc:mysql:协议
        //localhost:ip地址
        //3306:端口号
        //test:数据库
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";
        //封装用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "040731");

        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }
    
    @Test
    public void test2() throws SQLException {
        //获取配置文件的输入流
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties info = new Properties();
        try {
            info.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //获取配置信息
        String user = info.getProperty("user");
        String password = info.getProperty("password");
        String url = info.getProperty("url");
        String driverClass = info.getProperty("driverClass");
        //利用反射来实例化driverClass
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
        try {
            is.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        conn.close();
    }
}
