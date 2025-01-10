package com.atguigu.java;

import com.atguigu.bean.Customers;
import com.atguigu.utils.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @author aoao
 * @create 2025-01-04-14:56
 */
public class CustomerForQuery {

    //对表Customers的查询通用方法
    public static Customers queryForCustomers(String sql,Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);

            //将args[]依次填入通配符中
            for(int i = 0; i < args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            //执行
            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            if(rs.next()){
                Customers cust = new Customers();
                //通过循环获得一行的数据
                for(int i = 0; i < columnCount; i++){
                    Object value = rs.getObject(i + 1);
                    //获取列名字
                    String columnName = rsmd.getColumnName(i + 1);
                    //给cust某个属性赋值为value
                    Field field = Customers.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(cust,value);
                }

                return cust;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }finally{
            JDBCUtils.close(conn,ps,rs);
        }
        return null;
    }

    @Test
    public void test2(){
        System.out.println(queryForCustomers("select id,name,email,birth from customers where id=?",12));
    }


    @Test
    public void test1() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, 9);
            rs = ps.executeQuery();
            //获得结果集
            rs = ps.executeQuery();
            //处理结果集
            if (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String email = rs.getString(3);
                Date brith = rs.getDate(4);

                Customers customers = new Customers(id,name,email,brith);
                System.out.println(customers);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        JDBCUtils.close(conn,ps,rs);
        }

    }

