package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class MysqlConfig {

    private static String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static String URL = "jdbc:mysql://localhost:3306/crm_app";
    private static String USER_NAME = "root";
    private static String PASSWORD = "1234";

    public static Connection getConnection() {
        Connection connection = null;

        try {

            // Đăng ký sử dụng driver cho cơ sở dữ liệu MYSQL
            Class.forName(DRIVER_NAME);

            // Mở kết nối tới CSDL theo driver đã chỉ định
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (Exception e) {
            System.out.println("Lỗi kết nối tới CSDL: " + e.getMessage());
        }
        return connection;
    }

}
