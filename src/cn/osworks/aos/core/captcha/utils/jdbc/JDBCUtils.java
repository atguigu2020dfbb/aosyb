package cn.osworks.aos.core.captcha.utils.jdbc;

/**
 * @Auther: Ricardo
 * @Date: 2020/3/16 19:50
 * @Description: JDBC 工具类
 *     1. 加载驱动
 *     2. 创建连接对象
 *     3. 关闭资源
 */

import cn.osworks.aos.system.modules.service.archive.DataService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCUtils {
    // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
    private static  String driver;
    //主数据库
    private static  String url;
    //温湿度数据库
    private static  String url2;
    ///门禁数据库
    private static  String url3;
    // 数据库的用户名与密码，需要根据自己的设置
    private static  String userName;
    private static  String passWord ;

    static {
        Properties pro = new Properties();
        try {
            //加载资源文件
            //InputStream in = new FileInputStream("G:\\JavaWorkspace\\jdbc\\src\\DB.properties");
            //动态加载src目录
            InputStream in = DataService.class
                    .getResourceAsStream("/jdbc.properties");
            pro.load(in);
            driver = pro.getProperty("jdbc.driver");
            url = pro.getProperty("jdbc.url");
            url2 = pro.getProperty("jdbc.url2");
            url3 = pro.getProperty("jdbc.url3");
            userName = pro.getProperty("jdbc.username");
            passWord = pro.getProperty("jdbc.password");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // 注册 JDBC 驱动
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * 打开 主数据库 链接
     * @return Connection
     */
    public static Connection getMainConnection(){
        return getConnection(url,userName,passWord);
    }
    /**
     * 打开 温湿度数据库 链接
     * @return Connection
     */
    public static Connection getWSDConnection(){
        return getConnection(url2,userName,"1");
    }
    /**
     * 打开 主数据库 链接
     * @return Connection
     */

    public static Connection getMJConnection(){
        return getConnection(url3,userName,"1");
    }
    /**
     * 打开链接
     * @return Connection
     */
    private static Connection getConnection(String url,String userName,String passWord){
        Connection conn = null;
        try {
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(url,userName,passWord);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(Connection conn){
        if(conn!=null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Statement stat){
        if(stat!=null) {
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(ResultSet res){
        if(res!=null) {
            try {
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
