package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.techelevator.tenmo.model.Accounts;

@Service
public class AccountsSqlDAO implements AccountsDAO {

	private JdbcTemplate jdbcTemplate;

	public AccountsSqlDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// finds all the accounts and retrieves their properties
	@Override
	public List<Accounts> findAll() {
		List<Accounts> allAccounts = new ArrayList<>();

		String sql = "SELECT * FROM accounts";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

		while (results.next()) {
			Accounts account = mapRowToAccounts(results);
			allAccounts.add(account);
		}

		return allAccounts;
	}

	// finds account by user id
	@Override
	public Long findAccountIdByUserId(Long userId) {
		Long accountId = 0L;
		try {
			for (Accounts thisAccount : findAll()) {
				if (thisAccount.getUserId().equals(userId)) {
					accountId = thisAccount.getAccountId();
				}
			}
		} catch (Exception e) {
			System.out.println("No matching account found.");
		}
		return accountId;
	}

	//returns account balance by account id
	@Override
	public BigDecimal accountBalanceByAccountId(Long accountId) {
		BigDecimal accountBalance = new BigDecimal(0.00);

		for (Accounts thisAccount : findAll()) {
			if (thisAccount.getAccountId().equals(accountId)) {
				accountBalance = thisAccount.getBalance();
			}
		}

		return accountBalance;
	}
	
	private Accounts mapRowToAccounts(SqlRowSet rs) {
		Accounts account = new Accounts();
		account.setAccountId(rs.getLong("account_id"));
		account.setUserId(rs.getLong("user_id"));
		account.setBalance(rs.getBigDecimal("balance"));
		return account;
	}


}
