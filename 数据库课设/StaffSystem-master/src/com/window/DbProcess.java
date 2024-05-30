package com.window;

import java.sql.*;

public class DbProcess {
    static Connection connection = null;
    static ResultSet rs=null ;
    public static void connect()
    {
        try{
            //mysql数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/staffdb","root","123456");

            if(connection!=null){
                System.out.println("数据库连接成功");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void disconnect(){
        try{
            if(connection != null){
                connection.close();
                connection = null;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static  ResultSet executeQuery(String sql) {

        try {
            System.out.println("executeQuery(). sql = " + sql);

            PreparedStatement pstm = connection.prepareStatement(sql); //建立用于执行对象

            rs = pstm.executeQuery();  //结果集存入rs中,并返回rs;

        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }

        return rs;
    }

    public static int executeUpdate(String sql) {
        int count = 0;
        connect();
        try {
            Statement stmt = connection.createStatement();
            count = stmt.executeUpdate(sql);
        }
        catch(SQLException ex) {
            System.err.println(ex.getMessage());
        }
        disconnect();
        return count;
    }
}
