package org.csu.mypetstore.persistence;

import java.sql.*;

public class DBUtil {
    private static String driveString = "com.mysql.jdbc.Driver";
    private static String connectionString = "jdbc:mysql://127.0.0.1/mypetstore?useUnicode=true&characterEncoding=UTF-8&useSSL=true";
    private static String username = "root";
    private static String password = "fuck1314520";

    //获取连接
    public static Connection getConnection() throws Exception {
        Connection connection = null;

        try {
            Class.forName(driveString);
            connection = DriverManager.getConnection(connectionString, username, password);
        }catch (Exception e){
            throw e;
        }

        return connection;
    }

    public static void closeStatement(Statement statement) throws Exception {
        statement.close();
    }

    public static void closePreparedStatement(PreparedStatement pStatement) throws Exception{
        pStatement.close();
    }

    public static void closeResultSet(ResultSet resultSet) throws Exception{
        resultSet.close();
    }

    //关闭连接
    public static void closeConnection(Connection connection) throws Exception {
        connection.close();
    }

    /*test the db connection
    public static void main(String[] args) throws Exception{
        Connection conn = DBUtil.getConnection();
        System.out.println(conn);
    }
    */
}
