package com.toys.wholesaletoyservice.rmi.clienttasks;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.toys.toyservicecommon.DatabaseConnector;
import com.toys.wholesaletoyservice.compute.Task;

public class AddToyPrice implements Task<String>, Serializable {
	private static final long serialVersionUID = 227L;
	private static final Logger LOGGER = Logger.getLogger(DatabaseConnector.class.getName());	
	private final DatabaseConnector databaseConnector = new DatabaseConnector();
	
	private final String addtoyrequest;

	// constructor to accept the toy_price entity
	public AddToyPrice(String addtoyrequest) {
		this.addtoyrequest = addtoyrequest;
	}

	// implement execute method
	public String execute() {
		// call to private method that invokes class behavior
		return addNewToy();
	}

	private String addNewToy() {
		Connection databaseconnection = null;
		String addtoyresult = "Database connection failed";
		try {
			//map the string received from client into JSON object
			JSONObject addtoyrequest = new JSONObject(this.addtoyrequest);
			String username = addtoyrequest.getString("username");
			String password = addtoyrequest.getString("password");
			if (databaseConnector.cashierLogin(username, password)) {
				databaseconnection = databaseConnector.getDatabaseConnection();
				PreparedStatement st_addtoy = databaseconnection.prepareStatement("INSERT INTO ShopInventory (toyname,toycost,unitsavailable) VALUES (?,?,?)");
				st_addtoy.setString(1, addtoyrequest.getString("toyname"));
				st_addtoy.setDouble(2, addtoyrequest.getDouble("toycost"));
				st_addtoy.setInt(3, addtoyrequest.getInt("units"));
				
				st_addtoy.executeUpdate();
				
				addtoyresult =  "Toy added to table";
			}else {
				 addtoyresult =  "Cashier "+username+" not logged in";
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} finally {
			try {
				databaseconnection.close();
			} catch (Exception e) {
			}
		}
		return addtoyresult;
	}
}
