package org.amoyiki.oa.dao;

import org.amoyiki.oa.entities.User;

public interface UserDao {
	public Integer save(User user);
	public User findByName(String username);
	
}

