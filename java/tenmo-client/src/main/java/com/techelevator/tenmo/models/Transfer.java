package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {

	private Long transferId;
	private String transferTypeDesc;
	private String transferStatusDesc;
	private Long userFrom;
	private Long userTo;
	private BigDecimal amount;

	public Transfer() {

	}

	public Transfer(Long userFrom, Long userTo, BigDecimal amount) {
		this.userFrom = userFrom;
		this.userTo = userTo;
		this.amount = amount;
	}

	public Long getTransferId() {
		return transferId;
	}

	public void setTransferId(Long transferId) {
		this.transferId = transferId;
	}

	public String getTransferTypeDesc() {
		return transferTypeDesc;
	}

	public void setTransferTypeDesc(String transferTypeDesc) {
		this.transferTypeDesc = transferTypeDesc;
	}

	public String getTransferStatusDesc() {
		return transferStatusDesc;
	}

	public void setTransferStatusDesc(String transferStatusDesc) {
		this.transferStatusDesc = transferStatusDesc;
	}

	public Long getUserFrom() {
		return userFrom;
	}

	public void setUserFrom(Long accountFrom) {
		this.userFrom = accountFrom;
	}

	public Long getUserTo() {
		return userTo;
	}

	public void setUserTo(Long accountTo) {
		this.userTo = accountTo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "\nTransfer Id = " + transferId + "\nTransfer Type = " + transferTypeDesc + "\nTransfer Status = "
				+ transferStatusDesc + "\nSender's Account = " + userFrom + "\nReceiver's Account = " + userTo
				+ "\nTransfer Amount = " + amount;
	}

}
