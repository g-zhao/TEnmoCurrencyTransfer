package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountsDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.InsufficientFundsException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferFundsWeb;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.SecurityUtils;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/transfer")
public class TransferController {

	@Autowired
	private TransferDAO transferDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private AccountsDAO accountsDAO;

	@RequestMapping(path = "/registeredUsers", method = RequestMethod.GET)
	public List<User> getListOfRegisteredUsers() {
		String currentUsername = SecurityUtils.getCurrentUsername().get();
		Long currentUserId = (long) userDAO.findIdByUsername(currentUsername);
		List<User> registeredUsersList = userDAO.findAllUsersExceptCurrent(currentUserId);
		return registeredUsersList;
	}

//	@ResponseStatus(code = HttpStatus.ACCEPTED, reason = "Transfer has been successfully completed")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public String transferFunds(@RequestBody TransferFundsWeb transferFundsWeb) {
		Long userId = transferFundsWeb.getUserToId();
		BigDecimal transferAmount = new BigDecimal(transferFundsWeb.getTransferAmount());
		String currentUsername = SecurityUtils.getCurrentUsername().get();
		Long currentUserId = (long) userDAO.findIdByUsername(currentUsername);
		Long currentAccountId = accountsDAO.findAccountIdByUserId(currentUserId);
		// determining if current user's funds are sufficient for transfer amount
		Long accountToId = accountsDAO.findAccountIdByUserId(userId);
		BigDecimal currentBalance = accountsDAO.accountBalanceByAccountId(currentAccountId);

		try {
			if (currentBalance.doubleValue() < transferAmount.doubleValue() || currentAccountId == accountToId
					|| transferAmount.doubleValue() <= 0) {
				throw new InsufficientFundsException();
			} else {
				throw new TransferAcceptedException();
			}
		} catch (TransferAcceptedException e) {
			transferDAO.addTransfer(currentAccountId, accountToId, transferAmount.setScale(2, RoundingMode.HALF_UP));
			return e.toString();
		} catch (InsufficientFundsException e) {
			return e.toString();
		}

	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<Transfer> getAllTransfersAccountId() {
		String currentUsername = SecurityUtils.getCurrentUsername().get();
		Long currentUserId = (long) userDAO.findIdByUsername(currentUsername);
		Long currentAccountId = accountsDAO.findAccountIdByUserId(currentUserId);

		List<Transfer> allTransfers = transferDAO.transfersByAccount(currentAccountId);

		return allTransfers;
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public Transfer getTransferbyId(@PathVariable int id) {
		return transferDAO.getTransfersById((long) id);
	}

}
