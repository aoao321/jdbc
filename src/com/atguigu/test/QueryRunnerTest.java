package com.atguigu.test;

import com.atguigu.bean.Customers;
import com.atguigu.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author aoao
 * @create 2025-01-12-19:01
 */
public class QueryRunnerTest {

    @Test
    public void testInsert() throws SQLException {
        QueryRunner qr = new QueryRunner();
        Connection connection = JDBCUtils.getConnection();

        qr.update(connection,"insert into customers(name,email,birth) values(?,?,?)","蔡徐坤","cxk@qq.com","1997-09-08");
        JDBCUtils.close(connection,null);
    }

    /*
    * BeanHandler是ResultSetHandler接口的实现类，用于封装表中的一条记录
    * */
    @Test
    public void testQuery() throws SQLException {
        QueryRunner qr = new QueryRunner();
        Connection connection = JDBCUtils.getConnection();
        String sql = "select * from customers where id=?";

        BeanHandler<Customers> handler = new BeanHandler<Customers>(Customers.class);
        Customers customer = qr.query(connection, sql, handler, 20);

        System.out.println(customer);
        JDBCUtils.close(connection,null);
    }

    /*
     * BeanListHandler是ResultSetHandler接口的实现类，用于封装表中的多条记录
     * */
    @Test
    public void testQuery1() throws SQLException {
        QueryRunner qr = new QueryRunner();
        Connection connection = JDBCUtils.getConnection();
        String sql = "select * from customers ";

        BeanListHandler<Customers> handler = new BeanListHandler<Customers>(Customers.class);
        List<Customers> customersList = qr.query(connection, sql, handler);

        for (Customers customer : customersList) {
            System.out.println(customer);
        }

        JDBCUtils.close(connection,null);
    }


}

