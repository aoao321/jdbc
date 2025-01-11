package com.atguigu.test;

import com.atguigu.bean.Customers;
import com.atguigu.utils.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author aoao
 * @create 2025-01-05-10:33
 */
public class PrepareStatementQueryTest {

    @Test
    public void test1(){
        String sql = "select id,name,email,birth from customers where id < ?";
        List<Customers> forList = getForList(Customers.class, sql, 4);
        forList.forEach(System.out::println);
    }

    public <T> List<T> getForList(Class<T> clazz, String sql, Object... args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        conn = JDBCUtils.getConnection();

        try {
            ps = conn.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            //创建集合对象
            ArrayList<T> list = new ArrayList<>();
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.close(conn,ps,rs);
        }

    }

    @Test
    public void test() {
        String sql = "select id,name,email from customers where id=?";
        Customers customer = getInstance(Customers.class, sql, 1);
        System.out.println(customer);
    }

    public <T> T getInstance(Class<T> clazz,String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        conn = JDBCUtils.getConnection();

        try {
            ps = conn.prepareStatement(sql);
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.close(conn,ps,rs);
        }
        return null;
    }
}
