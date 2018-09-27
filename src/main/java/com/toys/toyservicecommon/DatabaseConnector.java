package com.toys.toyservicecommon;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * connect to the database
 */
public class DatabaseConnector implements Serializable{

	private static final Logger LOGGER = Logger.getLogger(DatabaseConnector.class.getName());

	public Connection getDatabaseConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://10.20.2.4:3306/Toyshop?verifyServerCertificate=false&useSSL=true", "toyuser",
					"password");
			return con;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return null;
	}

	public boolean cashierLogin(String username, String password) {
		LOGGER.info("Validating user");
		boolean cashierloggedin = false;
		Connection databaseconnection = getDatabaseConnection();
		try {
			LOGGER.log(Level.INFO, "Cashier " + username);
			PreparedStatement st_login = databaseconnection.prepareStatement("SELECT count(*) as user FROM ShopUsers WHERE username=? AND password=?");
			st_login.setString(1, username);
			st_login.setString(2, password);
			ResultSet rs_login = st_login.executeQuery();
			int user = 0;
			while(rs_login.next()) {
				user = rs_login.getInt("user");
			}
			if(user > 0) {
				cashierloggedin = true;
			}else {
				LOGGER.log(Level.INFO, "Cashier " + username+" incorrect credentials");
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} finally {
			try {
				databaseconnection.close();
			} catch (Exception e) {
			}
		}
		return cashierloggedin;
	}
}
