package com.atguigu.test;

import com.atguigu.bean.Customers;
import com.atguigu.dao.CustomerDAOImpl;
import com.atguigu.utils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * @author aoao
 * @create 2025-01-11-16:21
 */
public class DAOTest {

    //获取CustomerDAOImpl实例对象
    CustomerDAOImpl customersDao = new CustomerDAOImpl();

    @Test
    public void insertTest() {
        Connection connection = JDBCUtils.getConnection();
        //将对象插入到表中
        Customers c1 = new Customers(29,"女娲","nvwa@qq.com", new Date(0000-00-00));
        Customers c2 = new Customers(30,"肥宗","feizong@qq.com", new Date(0000-00-00));

        customersDao.insert(connection,c1);
        customersDao.insert(connection,c2);

        JDBCUtils.close(connection,null);
    }

    @Test
    public void updateTest() {
        Connection connection = JDBCUtils.getConnection();
        Customers c1 = new Customers(29,"女娲","nvwa@163.com", new Date(0000-00-00));
        customersDao.update(connection,c1);
        JDBCUtils.close(connection,null);
    }

    @Test
    public void deleteTest() {
        Connection connection = JDBCUtils.getConnection();
        customersDao.deleteById(connection,30);
        JDBCUtils.close(connection,null);
    }

    @Test
    public void findTest() {
        Connection connection = JDBCUtils.getConnection();
        System.out.println(customersDao.findById(connection, 29));
        JDBCUtils.close(connection,null);
    }

    @Test
    public void findAllTest() {
        Connection connection = JDBCUtils.getConnection();
        List<Customers> customers = customersDao.findAll(connection);
        for (Customers c : customers) {
            System.out.println(c);
        }
        JDBCUtils.close(connection,null);
    }


}
