package com.amayadream.webchat.utils;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by dell on 2017/5/1.
 */
public class DBCPUtils {
    private static String driverClassName="";
    private static String url="";
    private static String username="";
    private static String password="";

    private static Properties properties = new Properties();
    private static DataSource dataSource;

    private static ThreadLocal<Connection> tl = new ThreadLocal();//事物专用连接

    public static void loadProperty(){
        InputStream is = DBCPUtils.class.getClassLoader().getResourceAsStream("config/dbcp.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driverClassName=properties.getProperty("driver");
        url=properties.getProperty("url");
        username=properties.getProperty("username");
        password=properties.getProperty("password");
        System.out.println("配置文件里有什么:"+driverClassName+","+url+","+username+","+password);
    }

    public static DataSource getDataSource(){
        try {
            loadProperty();
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataSource;
    }

    public static Connection getConnection() throws SQLException{
        Connection con = tl.get();
        // 当con不等于null，说明已经调用过beginTransaction()，表示开启了事务！
        if(con != null) return con;
        return getDataSource().getConnection();
    }

    public static void releaseConnection(Connection connection) throws SQLException{
        Connection con = tl.get();
        //判断它是不是事务专用，如果是，就不关闭！
        //如果不是事务专用，那么就要关闭！
        // 如果con == null，说明现在没有事务，那么connection一定不是事务专用的！
        if(con == null) connection.close();
        // 如果con != null，说明有事务，那么需要判断参数连接是否与con相等，若不等，说明参数连接不是事务专用连接
        if(con != connection) connection.close();
    }

    /**
     * 1. 获取一个Connection，设置它的setAutoComnmit(false)
     * 2. 还要保证dao中使用的连接是我们刚刚创建的！
     * --------------
     * 1. 创建一个Connection，设置为手动提交
     * 2. 把这个Connection给dao用！
     * 3. 还要让commitTransaction或rollbackTransaction可以获取到！
     */
    public static void beginTransaction() throws SQLException{
        Connection connection = tl.get();
        if(connection != null) throw new SQLException("已经开启了事务,不需要再开启");
        connection = getConnection();//给connection赋值，表示事务已经开始了
        connection.setAutoCommit(false);//给con设置为手动提交！
        tl.set(connection);//把当前线程的connection保存起来！
    }

    public static void commitTransaction() throws SQLException{
        Connection connection = tl.get();//获取当前线程的专用连接
        if(connection == null) throw new SQLException("还没有开启事务,不能提交");
        connection.commit();
        connection.close();
        // 把它设置为null，表示事务已经结束了！下次再去调用getConnection()返回的就不是connection了
        tl.remove();//从tl中移除连接
    }

    public static void rollbackTransaction() throws SQLException{
        Connection connection = tl.get();
        if(connection == null) throw new SQLException("还没有开启事务，不能回滚！");
        connection.rollback();
        connection.close();
        tl.remove();
    }

}
