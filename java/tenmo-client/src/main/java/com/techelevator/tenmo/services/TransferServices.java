package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.TransferFundsWeb;
import com.techelevator.tenmo.models.User;

public class TransferServices {

	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	private String token;

	public TransferServices(String url) {
		this.BASE_URL = url;
	}

	public List<User> getRegisteredUsers() {
		return restTemplate.exchange(BASE_URL + "transfer/registeredUsers", HttpMethod.GET, makeAuthEntity(), new ParameterizedTypeReference<List<User>>() {}).getBody();
	}

	public Transfer getTransferById(Long transferId) {
		Transfer transfer = restTemplate
				.exchange(BASE_URL + "transfer/" + transferId, HttpMethod.GET, makeAuthEntity(), Transfer.class)
				.getBody();
		return transfer;

	}
	
	public Transfer[] allTransfers(String token, Integer integer) {
		Transfer[] tArray = null;
		try {
		tArray = restTemplate.exchange(BASE_URL + "transfer", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
	  } catch (RestClientResponseException ex) {
         System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
      }
		
		
		return tArray;
	}

	public List<Transfer> historyOfTransfers() {
		ResponseEntity<List<Transfer>> responseEntity = restTemplate.exchange(BASE_URL + "transfer", HttpMethod.GET,
				makeAuthEntity(), new ParameterizedTypeReference<List<Transfer>>() {
				});
		List<Transfer> transferHist = responseEntity.getBody();
		return transferHist;
	}

	public String sendTransfer(Long userToId, BigDecimal transferAmount) {
		TransferFundsWeb transfer = new TransferFundsWeb();
		transfer.setUserToId(userToId);
		transfer.setTransferAmount(transferAmount.doubleValue());
		String transferStatus = restTemplate.exchange(BASE_URL + "transfer", HttpMethod.POST, makeTransferEntity(transfer), String.class).getBody();
		return transferStatus;
	}

	/**
	 * Returns an {HttpEntity} with the `Authorization: Bearer:` header
	 * 
	 * @return {HttpEntity}
	 */

	private HttpEntity<TransferFundsWeb> makeTransferEntity(TransferFundsWeb transfer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(this.token);
		HttpEntity<TransferFundsWeb> entity = new HttpEntity<>(transfer, headers);
		return entity;
	}

	private HttpEntity<Object> makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(this.token);
		HttpEntity<Object> entity = new HttpEntity<>(headers);
		return entity;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
