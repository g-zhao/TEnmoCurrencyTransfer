package com.techelevator.tenmo.services;

import java.math.BigDecimal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class AccountService {

	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	private String token;

	public AccountService(String url) {
		this.BASE_URL = url;
	}

	public BigDecimal getAccountBalance() {
		BigDecimal accountBalance = new BigDecimal(0);
		return accountBalance = restTemplate.exchange(BASE_URL + "account/balance", HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();

	}

	/**
	 * Returns an {HttpEntity} with the `Authorization: Bearer:` header
	 * 
	 * @return {HttpEntity}
	 */
	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(this.token);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
