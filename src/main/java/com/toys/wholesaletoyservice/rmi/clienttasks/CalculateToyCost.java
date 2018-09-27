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

/**
 * 
 * @author user this class 1. query toy price 2. calculate the toy cost
 */
public class CalculateToyCost implements Task<String>, Serializable {
	private static final long serialVersionUID = 227L;
	private static final Logger LOGGER = Logger.getLogger(CalculateToyCost.class.getName());
	private final DatabaseConnector databaseConnector = new DatabaseConnector();

	private final String toyentity;

	// constructor to accept the toy_name and quantity
	public CalculateToyCost(String toyentity) {
		this.toyentity = toyentity;
	}

	// implement execute method
	public String execute() {
		// call to private method that invokes class behavior
		return fetchToyCosts();
	}

	private String fetchToyCosts() {
		Connection databaseconnection = null;
		String calculatetoycost = "Database connection failed";
		try {
			// map the string received from client into JSON object
			JSONObject gettoycostrequest = new JSONObject(this.toyentity);
			String username = gettoycostrequest.getString("username");
			String password = gettoycostrequest.getString("password");

			if (databaseConnector.cashierLogin(username, password)) {
				// user is logged in

				// get the toy details from table ShopInventory
				int availableunits = 0;
				double toyprice = 0;
				databaseconnection = databaseConnector.getDatabaseConnection();
				PreparedStatement st_gettoy = databaseconnection
						.prepareStatement("SELECT toycost,unitsavailable FROM ShopInventory WHERE toyname=?");
				st_gettoy.setString(1, gettoycostrequest.getString("toyname"));
				ResultSet rs_gettoy = st_gettoy.executeQuery();
				while (rs_gettoy.next()) {
					availableunits = rs_gettoy.getInt("unitsavailable");
					toyprice = rs_gettoy.getDouble("toycost");
				}

				// with the retrieved details, get the cost of the requested purchase
				int requestedunits = gettoycostrequest.getInt("units");
				if (requestedunits > availableunits) {
					// stock is not enough
					calculatetoycost = "not enough toys to satisfy purchase";
				} else if (requestedunits % 20 != 0) {
					// toys can only be purchased in multiples of 20
					calculatetoycost = "toys can only be purchased in multiples on 20";
				} else {

					calculatetoycost = calculateDiscount(requestedunits, toyprice).toString();
				}

			} else {
				calculatetoycost = "Cashier " + username + " not logged in";
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} finally {
			try {
				databaseconnection.close();
			} catch (Exception e) {
			}
		}
		return calculatetoycost;
	}

	/**
	 * toy price reduces by 5% for each until a total maximum of 25% is achieved
	 * where it remains constant
	 * 
	 * @param price
	 * @param units
	 * @return
	 */
	private JSONObject calculateDiscount(int units, double price) {
		JSONObject toypurchasevalue = new JSONObject();

		try {
		// calculate the 25% discount value amount
		double initialtoycost = units * price;
		double percent_discount = 0.25 * initialtoycost;

		double discountamount = 0;
		int counter = 0;

		//get the discount for the purchase
		while (counter < units && discountamount <= percent_discount) {
			discountamount += 0.05 * price; 
			counter++;
		}
		
		toypurchasevalue.put("initialtoycost", initialtoycost).put("discount", discountamount).put("costafterdiscount", initialtoycost-discountamount);
		
		}catch (Exception e) {
			LOGGER.log(Level.SEVERE,"exception getting toycost", e.getMessage());
		}
		return toypurchasevalue;
	}

}
