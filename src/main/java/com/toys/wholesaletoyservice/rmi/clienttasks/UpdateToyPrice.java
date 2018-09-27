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
 * @author user this class: 1. update the price of a toy on the database
 */
public class UpdateToyPrice implements Task<String>, Serializable {
	private static final long serialVersionUID = 227L;
	private static final Logger LOGGER = Logger.getLogger(UpdateToyPrice.class.getName());
	private final DatabaseConnector databaseConnector = new DatabaseConnector();
	private final String updatetoyrequest;

	// constructor to accept the toy_price entity
	public UpdateToyPrice(String updatetoyrequest) {
		this.updatetoyrequest = updatetoyrequest;
	}

	// implement execute method
	public String execute() {
		// call to private method that invokes class behavior
		return updateToyPrice();
	}

	private String updateToyPrice() {
		Connection databaseconnection = null;
		String updatetoyresult = "Database connection failed";
		try {
			JSONObject newtoycostrequest = new JSONObject(this.updatetoyrequest);
			String username = newtoycostrequest.getString("username");
			String password = newtoycostrequest.getString("password");

			if (databaseConnector.cashierLogin(username, password)) {
				double newprice = newtoycostrequest.getDouble("toycost");
				int units = newtoycostrequest.getInt("units");
				databaseconnection = databaseConnector.getDatabaseConnection();
				if (newprice > 0) {
					PreparedStatement st_updatetoy = databaseconnection
							.prepareStatement("UPDATE ShopInventory set toycost=? WHERE toyname=?");
					st_updatetoy.setDouble(1, newprice);
					st_updatetoy.setString(2, newtoycostrequest.getString("toyname"));
					st_updatetoy.executeUpdate();
				}
				PreparedStatement st_updatetoy = databaseconnection
						.prepareStatement("UPDATE ShopInventory set unitsavailable=unitsavailable + ? WHERE toyname=?");
				st_updatetoy.setInt(1, units);
				st_updatetoy.setString(2, newtoycostrequest.getString("toyname"));
				st_updatetoy.executeUpdate();
				updatetoyresult = "Toy Details updated";
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
