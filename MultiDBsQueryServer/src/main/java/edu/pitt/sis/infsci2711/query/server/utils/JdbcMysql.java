package edu.pitt.sis.infsci2711.query.server.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import com.mysql.jdbc.*;

public class JdbcMysql {
	
		public static final String DEFAULT_HOST = "localhost";
		public static final int DEFAULT_PORT = 3306;
		public static final String DEFAULT_USER = "multidbsuser"; //CHANGE TO YOUR MYSQL USER NAME
		public static final String DEFAULT_PASSWOD = "infsci2711"; // CHANGE TO YOUR MYSQL PASSWORD
		public static final String DEFAULT_DATABASE = "prestomatch";
		
		public static Connection getConnection() throws Exception {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(getConnectionString(), DEFAULT_USER, DEFAULT_PASSWOD);
		}
		public static Connection getConnection(String ip, String port, String username, String password) throws Exception {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(getConnectionString(ip,port), username, password);
		}
		public static String getConnectionString() {
			return String.format("jdbc:mysql://%s:%d/%s", DEFAULT_HOST, DEFAULT_PORT, DEFAULT_DATABASE);
		}
		public static String getConnectionString(String ip, String port) {
			return String.format("jdbc:mysql://%s:%s/", ip,port);
		}
}
