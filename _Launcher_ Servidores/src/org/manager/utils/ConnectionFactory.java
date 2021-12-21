package org.manager.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	
	public static String host;
	public static String user;
	public static String password;
	public static int port;
	public static String database;

	public static Connection newConnection() throws SQLException {

		return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
	}
}
