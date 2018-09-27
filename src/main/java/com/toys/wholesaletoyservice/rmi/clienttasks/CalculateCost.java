package com.toys.wholesaletoyservice.rmi.clienttasks;

import java.io.Serializable;

import com.toys.wholesaletoyservice.compute.Task;

/**
 * 
 * @author user this class: 1. create a receipt for the purchases based on
 *         logged in clerk/cashiers 2. data included in the receipt is
 *         transactionCost, totalDiscount, amountPaid, balanceDue,
 *         cashierLoggedin
 */
public class CalculateCost implements Task<String>, Serializable {
	private static final long serialVersionUID = 227L;

	private final String vendcheckout;

	/**
	 * receive a json representation of the sale made.. that includes the toy
	 */
	public CalculateCost(String vendcheckout) {
		this.vendcheckout = vendcheckout;
	}

	// implement execute method
	public String execute() {
		// call to private method that invokes class behavior
		return sendreceipt();
	}

	public String sendreceipt() {
		return "Purchase receipt for "+vendcheckout;
	}
}
