package com.library.utilities;

import java.sql.*;

public class DB_Util {
    private static Connection con;
    private static Statement stm;
    private static ResultSet rs;
    private static ResultSetMetaData rsmd;
    public static void createConnection(String url, String username, String password) {


        try {
            con = DriverManager.getConnection(url, username, password);
            System.out.println("CONNECTION SUCCESSFUL");
        } catch (Exception e) {
            System.out.println("CONNECTION HAS FAILED " + e.getMessage());
        }

    }
    public static void createConnection() {

        String url = ConfigurationReader.getProperty("dbUrl");

         String username = ConfigurationReader.getProperty("dbUsername") ;
       // String username = System.getenv("DB_USERNAME");

         String password = ConfigurationReader.getProperty("dbPassword") ;
       // String password = System.getenv("DB_PASSWORD");

        createConnection(url, username, password);

    }


    /**
     * Run the sql query provided and return ResultSet object
     *
     * @param sql the query to run
     * @return ResultSet object  that contains data
     */
    public static ResultSet runQuery(String sql) {

        try {
            stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery(sql); // setting the value of ResultSet object
            rsmd = rs.getMetaData();  // setting the value of ResultSetMetaData for reuse
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE RUNNING QUERY " + e.getMessage());
        }

        return rs;

    }

    /**
     * destroy method to clean up all the resources after being used
     */
    public static void destroy() {
        // WE HAVE TO CHECK IF WE HAVE THE VALID OBJECT FIRST BEFORE CLOSING THE RESOURCE
        // BECAUSE WE CAN NOT TAKE ACTION ON AN OBJECT THAT DOES NOT EXIST
        try {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (con != null) con.close();
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE CLOSING RESOURCES " + e.getMessage());
        }

    }
}
