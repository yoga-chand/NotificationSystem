package com.notification.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private static final String DB_URL = "jdbc:hsqldb:hsql://localhost/";
	private static final String DB_USERNAME = "sa";
	private static final String DB_PASSWORD = "";

	//Creates a DB Connection object
	public static Connection getConnection(){

		Connection conn = null;

		try {
			Class.forName("org.hsqldb.jdbcDriver");
			conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
		}
		catch(SQLException e){
			System.err.println("Exception while connecting to DB "+e.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return conn;
	}



}

