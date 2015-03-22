package edu.pitt.sis.infsci2711.query.server.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcPresto {
	public static final String DEFAULT_HOST = "127.0.0.1";
	
	public static final int DEFAULT_PORT = 8080;
	
	public static final String DEFAULT_USER = "any"; //CHANGE TO YOUR MYSQL USER NAME
	
	public static final String DEFAULT_PASSWOD = ""; // CHANGE TO YOUR MYSQL PASSWORD
	
	public static final String DEFAULT_CATALOG = "";
	
	public static final String DEFAULT_SCHEMA = "";
	
	protected String connStr="jdbc:presto://";
	//public static String url = "jdbc:presto://localhost:8080/";
	public static Connection getConnection() throws Exception {
		return DriverManager.getConnection(getConnectionString(), "any", null);
	}

	public static String getConnectionString() {
		//return String.format("jdbc:mysql://%s:%d/%s", DEFAULT_HOST, DEFAULT_PORT, DEFAULT_DATABASE);
		String catalog=DEFAULT_CATALOG;
		if(catalog!="")
		{
			catalog+="/";
			catalog+=DEFAULT_SCHEMA;
		}
		
		return String.format("jdbc:presto://%s:%d/%s", DEFAULT_HOST, DEFAULT_PORT, catalog);
	}

}
