package com.atguigu.dao;

import com.atguigu.bean.Customers;

import java.sql.Connection;
import java.sql.Date;
import java.util.Collections;
import java.util.List;

/**
 * @author aoao
 * @create 2025-01-11-15:21
 */
public class CustomerDAOImpl extends DAO implements CustomerDAO{
    @Override
    public void insert(Connection connection, Customers customer) {
        String sql = "insert into customers (id,name,email,birth)  values(?,?,?,?)";
        update(connection,sql,customer.getId(),customer.getName(), customer.getEmail(), customer.getBirth());
    }

    @Override
    public void update(Connection connection, Customers customer) {
        String sql = "update customers set name=?,email=?,birth=? where id=?";
        update(connection,sql,customer.getName(), customer.getEmail(), customer.getBirth(),customer.getId());
    }

    @Override
    public void deleteById(Connection connection, int id) {
        String sql = "delete from customers where id=?";
        update(connection,sql,id);
    }

    @Override
    public Customers findById(Connection connection, int id) {
        String sql = "select name,email,birth from customers where id=?";
        return getInstance(connection,Customers.class,sql,id);
    }

    @Override
    public List<Customers> findAll(Connection connection) {
        String sql = "select name,email,birth from customers ";
        return getForList(connection, Customers.class, sql);
    }

    @Override
    public Long getCount(Connection connection) {
        String sql = "select count(*) from customers ";
        return getValue(connection,sql);
    }

    @Override
    public Date getMaxBrith(Connection connection) {
        String sql = "select max(birth) from customers ";
        return getValue(connection,sql);
    }
}
