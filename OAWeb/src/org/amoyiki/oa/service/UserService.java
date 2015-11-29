package org.amoyiki.oa.service;

import org.amoyiki.oa.entities.User;

public interface UserService {

	public boolean regisiter(User user);
	public User login(String username,String password);
	
}
