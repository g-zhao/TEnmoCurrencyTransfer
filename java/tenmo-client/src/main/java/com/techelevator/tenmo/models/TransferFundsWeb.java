package com.techelevator.tenmo.models;

public class TransferFundsWeb {
	
	private long userToId;
	private double transferAmount;
	
	public long getUserToId() {
		return userToId;
	}
	public void setUserToId(long userId) {
		this.userToId = userId;
	}
	public double getTransferAmount() {
		return transferAmount;
	}
	public void setTransferAmount(double transferAmount) {
		this.transferAmount = transferAmount;
	}
	
	
	

}
