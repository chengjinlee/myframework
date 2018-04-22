package com.framework.util;

import javafx.beans.binding.ObjectExpression;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class JDBCUtil {

    //初始化
    static {
        init();
        initPool();
    }
    private static ArrayList<Connection> pool;
    private static Properties properties;
    private static String url;
    private static int min_size;
    private static int max_size;

    private static void init(){
        pool = new ArrayList<>();
        properties = new Properties();
        try{
            InputStream is = JDBCUtil.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        url = properties.getProperty("url");
        String driver = properties.getProperty("driver");
        min_size = Integer.parseInt(properties.getProperty("min_conn"));
        max_size = Integer.parseInt(properties.getProperty("max_conn"));

        try{
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        initPool();
    }

    private static void initPool() {

        pool.clear();
        for (int i=0; i<min_size;i++){
            try{
                pool.add(DriverManager.getConnection(url,properties));
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public static Connection getConnection(){
        Connection conn = null;
        if (pool.size() <= 0){
            try{
                conn = DriverManager.getConnection(url,properties);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }else {
            conn = pool.remove(0);
            try {
                if (conn == null || conn.isClosed()){
                    init();
                    conn = pool.remove(0);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static int executeUpdate(String sql, Object... obj){
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try{
            assert conn != null;
            ps = conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return freeConnection(conn);
        }
        try{
            ps.executeQuery("show tables");
        } catch (SQLException e) {
            initPool();
            return  executeUpdate(sql,obj);
        }
        if (obj != null){
            for (int i = 0 ; i< obj.length;i++){
                try{
                    ps.setObject(i+1,obj[i]);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return freeConnection(conn);
                }
            }
        }
        int i = -1;
        try {
            i = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return freeConnection(conn);
        }
        freeConnection(conn);
        return i;
    }

    private static ResultSet executeQuery(String sql, Object... obj) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            freeConnection(conn);
            return null;
        }
        try {
            ps.executeQuery("show tables");
        } catch (SQLException e) {
            e.printStackTrace();
            initPool();
            return executeQuery(sql, obj);
        }
        if (obj != null){
            for (int i=0;i<obj.length;i++){
                try {
                    ps.setObject(i+1,obj[i]);
                } catch (SQLException e) {
                    e.printStackTrace();
                    freeConnection(conn);
                    return null;
                }
            }
        }
        ResultSet res = null;
        try{
            res = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        freeConnection(conn);
        return res;
    }

    private static int freeConnection(Connection conn) {

        if (conn != null){
            if (pool.size() < max_size){
                pool.add(conn);
            }else{
                try{
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    public static void freeResultSet(ResultSet res){
        if (res != null){
            try{
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
