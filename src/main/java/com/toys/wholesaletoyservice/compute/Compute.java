package com.toys.wholesaletoyservice.compute;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Compute extends Remote {
	/**
	 * Submit a task.
	 * 
	 * @param t
	 *            the task to be executed
	 * @return the completed task.
	 * 
	 * @throws RemoteException
	 *             if a remote exception occurs
	 */
	<T> T executeTask(Task<T> t) throws RemoteException;
//	String executeTask(String t) throws RemoteException;
}
