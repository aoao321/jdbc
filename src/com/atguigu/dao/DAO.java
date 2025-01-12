package com.atguigu.dao;

import com.atguigu.utils.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author aoao
 * @create 2025-01-11-14:34
 */
public abstract class DAO<T> {

    private Class<T> clazz = null;

    {
        //获取当前DAO子类继承的父类中泛型
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        clazz = (Class<T>) typeArguments[0];
    }

    //查询单条操作
    public T getInstance(Connection connection,String sql, Object ...args){
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            if(rs.next()){
                //获取泛型实例
                T t = clazz.newInstance();
                int columnCount = rsmd.getColumnCount();
                for (int i = 0;i < columnCount;i++) {
                    //获取每一列的值
                    Object value = rs.getObject(i+1);
                    //获取列别名
                    String columnLabel = rsmd.getColumnLabel(i+1);
                    //获取属性，赋值给对象
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,value);
                }
                return t;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.close(null,ps,rs);
        }
        return null;
    }

    //查询多条操作
    public List<T> getForList(Connection connection, String sql, Object ...args){
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            //创建集合对象
            List<T> list = new ArrayList<>();
            while(rs.next()){
                //获取泛型实例
                T t = clazz.newInstance();
                int columnCount = rsmd.getColumnCount();
                for (int i = 0;i < columnCount;i++) {
                    //获取每一列的值
                    Object value = rs.getObject(i+1);
                    //获取列别名
                    String columnLabel = rsmd.getColumnLabel(i+1);
                    //获取属性，赋值给对象
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,value);
                }
                //添加进集合中
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }  finally {
            JDBCUtils.close(null,ps,rs);
        }
    }

    //通用的增删改操作
    public int update(Connection connection,String sql,Object ...args){
        PreparedStatement ps = null;
        //获取连接
        try {
            //预编译sql返回PreparedStatement对象
            ps = connection.prepareStatement(sql);
            //将args[]依次填入通配符中
            for(int i = 0; i < args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            //执行
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            //关闭流
            JDBCUtils.close(null,ps);
        }

    }

    //特殊值查询
    public <E> E getValue(Connection connection, String sql, Object ...args){
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            //填充通配符
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            //获取结果集
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if(rs.next()){
                return (E)rs.getObject(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            JDBCUtils.close(null,ps,rs);
        }
        return null;
    }

}
