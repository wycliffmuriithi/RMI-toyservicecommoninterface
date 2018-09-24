package com.toys.wholesaletoyservice.rmi.clienttasks;

import java.io.Serializable;

import com.toys.wholesaletoyservice.compute.Task;



/**
 * 
 * @author user
 * this class:
 * 	1. create a receipt for the purchases based on logged in clerk/cashierS
 */
public class CalculateCost implements Task<String>, Serializable {
	private static final long serialVersionUID = 227L;

	private final String receipt;

	// constructor to accept the toy_price entity
	public CalculateCost(String receipt) {
		this.receipt = receipt;
	}

	// implement execute method
	public String sendreceipt (){
		return  "The cost of the "+this.receipt+" is 100 dollars";
	}
	public String execute() {
		// call to private method that invokes class behavior
		return sendreceipt();
	}
}
