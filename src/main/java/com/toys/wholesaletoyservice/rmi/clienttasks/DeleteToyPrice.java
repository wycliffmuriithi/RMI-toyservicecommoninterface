package com.toys.wholesaletoyservice.rmi.clienttasks;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.toys.toyservicecommon.DatabaseConnector;
import com.toys.wholesaletoyservice.compute.Task;



/**
 * 
 * @author user
 * 	this class
 * 		1. delete a toy-price entity from database
 *
 */
public class DeleteToyPrice implements Task<String>, Serializable {
	private static final long serialVersionUID = 227L;
	private static final Logger LOGGER = Logger.getLogger(DeleteToyPrice.class.getName());
	private final DatabaseConnector databaseConnector = new DatabaseConnector();
	private final String deletetoyrequest;

	// constructor to accept the toy_price entity
	public DeleteToyPrice(String deletetoyrequest) {
		this.deletetoyrequest = deletetoyrequest;
	}

	// implement execute method
	public String execute() {
		// call to private method that invokes class behavior
		return deleteToyPrice();
	}
	
	private String deleteToyPrice() {
		Connection databaseconnection = null;
		String updatetoyresult = "Database connection failed";
		try {
			JSONObject newtoycostrequest = new JSONObject(this.deletetoyrequest);
			String username = newtoycostrequest.getString("username");
			String password = newtoycostrequest.getString("password");

			if (databaseConnector.cashierLogin(username, password)) {
				databaseconnection = databaseConnector.getDatabaseConnection();
				
					PreparedStatement st_deletetoy = databaseconnection
							.prepareStatement("DELETE FROM ShopInventory WHERE toyname=?");
					st_deletetoy.setString(1, newtoycostrequest.getString("toyname"));
					st_deletetoy.executeUpdate();
				
				updatetoyresult = "Toy deleted";
			} else {
				updatetoyresult = "Cashier " + username + " not logged in";
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} finally {
			try {
				databaseconnection.close();
			} catch (Exception e) {
			}
		}
		return updatetoyresult;
	}
}
