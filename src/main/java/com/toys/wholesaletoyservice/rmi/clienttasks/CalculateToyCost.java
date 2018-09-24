package com.toys.wholesaletoyservice.rmi.clienttasks;

import java.io.Serializable;

import com.toys.wholesaletoyservice.compute.Task;


/**
 * 
 * @author user
 *	this class
 *	 	1. query toy price
 * 		2. calculate the toy cost
 */
public class CalculateToyCost implements Task<String>, Serializable {
	private static final long serialVersionUID = 227L;

	private final String toy_cost;

	// constructor to accept the toy_price entity
	public CalculateToyCost(String toy_cost) {
		this.toy_cost = toy_cost;
	}

	// implement execute method
	public String execute() {
		// call to private method that invokes class behavior
		return toy_cost;
	}
}
