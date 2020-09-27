package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.techelevator.tenmo.model.Transfer;


@Service
public class TransferSqlDAO implements TransferDAO {

	private JdbcTemplate jdbcTemplate;

	public TransferSqlDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public void addTransfer(Long aFrom, Long aTo, BigDecimal amount) {
		
		String insertSqlTransfer = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) "
				+ "VALUES (2, 2, ?, ?, ?)";
		
		String addSqlTransfer = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
		String subSqlTransfer = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";

		jdbcTemplate.update(insertSqlTransfer, aFrom, aTo, amount);
		jdbcTemplate.update(addSqlTransfer, amount, aTo);
		jdbcTemplate.update(subSqlTransfer, amount, aFrom);
	}

	@Override
	public Transfer getTransfersById(Long transferId) {
		Transfer thisTransfer = new Transfer();
		String sql = "SELECT * FROM transfers " 
				+ "JOIN transfer_types ON transfer_types.transfer_type_id = transfers.transfer_type_id " 
				+ "JOIN transfer_statuses ON transfer_statuses.transfer_status_id = transfers.transfer_status_id " 
				+ "WHERE transfer_id = ?";

		SqlRowSet output = jdbcTemplate.queryForRowSet(sql, transferId);

		if (output.next()) {
			thisTransfer = addRowToTransfer(output);
		}
		return thisTransfer;
	}

	@Override
	public List<Transfer> transfersByAccount(Long accountId) {
		List<Transfer> transferList = new ArrayList<Transfer>();
		String sqlTransfer = "SELECT * FROM transfers "
				+ "JOIN transfer_types ON transfer_types.transfer_type_id = transfers.transfer_type_id "
				+ "JOIN transfer_statuses ON transfer_statuses.transfer_status_id = transfers.transfer_status_id "
				+ "WHERE account_from = ? OR account_to = ?";

		SqlRowSet output = jdbcTemplate.queryForRowSet(sqlTransfer, accountId, accountId);

		while (output.next()) {
			Transfer newTransfer = addRowToTransfer(output);
			transferList.add(newTransfer);

		}
		return transferList;
	}

	private Transfer addRowToTransfer(SqlRowSet output) {
		Transfer newTransfer = new Transfer();

		newTransfer.setTransferId(output.getLong("transfer_id"));
		newTransfer.setTransferTypeDesc(output.getString("transfer_type_desc"));
		newTransfer.setTransferStatusDesc(output.getString("transfer_status_desc"));
		newTransfer.setUserFrom(output.getLong("account_from"));
		newTransfer.setUserTo(output.getLong("account_to"));
		newTransfer.setAmount(output.getBigDecimal("amount"));
		return newTransfer;
	}

}
