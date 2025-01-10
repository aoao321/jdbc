package com.atguigu.java;

import com.atguigu.utils.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;


/**
 * @author aoao
 * @create 2025-01-02-17:05
 */
public class PreparedStatementUpdateTest {

    //通用的增删改操作
    public static void update(String sql,Object ...args){
        //获取连接
        try {
            Connection conn = JDBCUtils.getConnection();
            //预编译sql返回PreparedStatement对象
            //String sql = " INSERT INTO customers (id,name,email,birth) VALUES (?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            //将args[]依次填入通配符中
            for(int i = 0; i < args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            //执行
            ps.execute();
            //关闭流
            JDBCUtils.close(conn,ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test3() {
        update(" INSERT INTO customers (id,name,email) VALUES (?,?,?)",25,"肥宗","feizong@qq.com");
    }


    //向user表中添加数据
    @Test
    public void test() {
        //1.先与数据库建立连接
        //获取Properties文件输入流is
        InputStream is = PreparedStatementUpdateTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties prop = new Properties();
        try {
            prop.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //获取文件具体信息
        String user = prop.getProperty("user");
        String password = prop.getProperty("password");
        String url = prop.getProperty("url");
        String driverClass = prop.getProperty("driverClass");
        //利用反射获取driverManager类
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //获取连接
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            String sql = " INSERT INTO customers (id, name, email, birth) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,22);
            preparedStatement.setString(2,"王力宏");
            preparedStatement.setString(3,"wlihong@163.com");
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = sdf.parse("2025-1-4");
                preparedStatement.setDate(4,new Date(date.getTime()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            //执行操作
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //关闭流
        try {
            is.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test2() {
        //获取连接
        try {
            Connection conn = JDBCUtils.getConnection();
            //预编译sql语句，返回PreparedStatement语句
            String sql = " INSERT INTO customers (id,name,email,birth) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            //填充占位符
            preparedStatement.setInt(1,9);
            preparedStatement.setString(2,"女娲");
            preparedStatement.setString(3,"nvwa@163.com");
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = sdf.parse("2000-1-4");
                preparedStatement.setDate(4,new Date(date.getTime()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            //执行语句
            preparedStatement.execute();
            //关闭资源
            JDBCUtils.close(conn,preparedStatement);
            System.out.println("ok");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
