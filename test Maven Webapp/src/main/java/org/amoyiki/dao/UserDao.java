package org.amoyiki.dao;

import org.amoyiki.entity.User;


public interface UserDao {

	User query(String username, String password);
}
