package com.techelevator.tenmo.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

	@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "Your transfer has been canceled for one of the following reasons; insufficient funds, transfer amount is less than 0, or you are trying to transfer to your own account.")
	public class InsufficientFundsException extends Exception {
		private static final long serialVersionUID = 1L;

		public String toString() {
			return "Your transfer has been canceled for one of the following reasons; insufficient funds, transfer amount is less than 0, or you are trying to transfer to your own account.";
		}

	}
