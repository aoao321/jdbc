package com.atguigu.test;

import com.atguigu.bean.User;
import com.atguigu.utils.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @author aoao
 * @create 2025-01-11-12:20
 */
public class TransactionTest {

    @Test
    public void queryTest(){
        Connection conn = JDBCUtils.getConnection();
        try {
            //获取隔离级别
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            System.out.println(conn.getTransactionIsolation());
            //手动提交
            conn.setAutoCommit(false);
            User user = getInstance(conn, User.class, "select user,password from user_table where user=?", "AA");
            System.out.println(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void updateTest(){
        Connection conn = JDBCUtils.getConnection();
        //手动提交
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //更新操作
        update(conn,"update user_table set password=? where user=?", "123456aabb", "AA");
        try {
            Thread.sleep(15000);
            System.out.println("修改结束");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        try {
//            //提交
//            conn.commit();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

    }

    //查询操作，考虑上事务
    public <T> T getInstance(Connection connection,Class<T> clazz,String sql,Object ...args){
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.close(null,ps,rs);
        }
        return null;
    }

    //通用的增删改操作
    public void update(Connection connection,String sql,Object ...args){
        //获取连接
        try {

            //预编译sql返回PreparedStatement对象
            //String sql = " INSERT INTO customers (id,name,email,birth) VALUES (?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            //将args[]依次填入通配符中
            for(int i = 0; i < args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            //执行
            ps.execute();
            //关闭流
            JDBCUtils.close(null,ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
