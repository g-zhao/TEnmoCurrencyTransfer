package com.techelevator.tenmo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.User;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserDAO userDAO;

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public User getUsernameById(@PathVariable int id) {
		List<User> userList = userDAO.findAll();
		User user = new User();
		for (User thisUser : userList) {
			if (thisUser.getId().equals((long)(id))) {
				user.setId(thisUser.getId());
				user.setUsername(thisUser.getUsername());
				break;
			}
		}
		return user;
	}
	
}
