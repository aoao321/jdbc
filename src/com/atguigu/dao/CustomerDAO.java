package com.atguigu.dao;

import com.atguigu.bean.Customers;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * 此接口用于规范customers表常用操作
 *
 * @author aoao
 * @create 2025-01-11-15:06
 */
public interface CustomerDAO {
    //将customer对象添加到数据库中
    void insert(Connection connection, Customers customer);

    //修改指定id的customer为Java中Customers对象的属性
    void update(Connection connection,Customers customer);

    //删除指定id的customer
    void deleteById(Connection connection, int id);

    //查询指定id的customer信息
    Customers findById(Connection connection, int id);

    //查询所有customer信息
    List<Customers> findAll(Connection connection);

    //查询customers表中有多少条数据
    Long getCount(Connection connection);

    //查询customers表中最大生日
    Date getMaxBrith(Connection connection);
}
