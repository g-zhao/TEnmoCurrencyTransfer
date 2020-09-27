package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Accounts;

public interface AccountsDAO {

	List<Accounts> findAll();
	
	Long findAccountIdByUserId(Long userId);
	
	BigDecimal accountBalanceByAccountId(Long accountId);
	
}
