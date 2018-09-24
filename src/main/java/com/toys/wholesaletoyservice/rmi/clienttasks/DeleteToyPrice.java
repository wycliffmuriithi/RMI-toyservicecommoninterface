package com.toys.wholesaletoyservice.rmi.clienttasks;

import java.io.Serializable;

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

	private final String toy_price;

	// constructor to accept the toy_price entity
	public DeleteToyPrice(String toy_price) {
		this.toy_price = toy_price;
	}

	// implement execute method
	public String execute() {
		// call to private method that invokes class behavior
		return toy_price;
	}
}
