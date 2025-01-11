package com.atguigu.test;

import com.atguigu.bean.Order;
import com.atguigu.utils.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @author aoao
 * @create 2025-01-04-19:29
 */
public class OrderQueryTest {

    @Test
    public void test1(){
        Order order = orderForQuery("select order_id as orderId,order_name as orderName,order_date as orderDate from `order` where order_id = ?", 1);
        System.out.println(order);
    }


    //查询Order表的通用操作
    public static Order orderForQuery(String sql,Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if(rs.next()){
                Order order = new Order();
                for (int i = 0; i < columnCount; i++) {
                //获取属性
                Object value = rs.getObject(i+1);
                //获取列名
                  //  String columnName = rsmd.getColumnName(i+1);
                //获取列的别名
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                //利用反射获取Order类里面的属性
                Field declaredField = Order.class.getDeclaredField(columnLabel);
                declaredField.setAccessible(true);
                declaredField.set(order,value);
                }
                return order;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.close(conn,ps,rs);
        }
        return null;
    }

    @Test
    public void test() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "select order_id,order_name,order_date from `order` where order_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,2);
            //执行
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                if(rs.next()){
                    int id = (int) rs.getObject(1);
                    String name = (String) rs.getObject(2);
                    Date date = (Date) rs.getObject(3);
                    Order order = new Order(id, name, date);
                    System.out.println(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.close(conn, ps, rs);
        }

    }
}
