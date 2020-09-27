package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.User;

public class UserService {

	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	private String token;
	
	public UserService(String url) {
		this.BASE_URL = url;

	}

	public User getUserById(long id) {
		User user = new User();
		try {
			user = restTemplate.exchange(BASE_URL + "users/" + id, HttpMethod.GET, makeAuthEntity(), User.class).getBody();
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return user;
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
