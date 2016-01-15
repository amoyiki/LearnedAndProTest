package org.amoyiki.action;

import org.amoyiki.dao.UserDao;
import org.amoyiki.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

@Controller("userAction")
@Scope("prototype")
public class UserAction extends ActionSupport{

	private static final long serialVersionUID = -8985876646100092685L;

	@Autowired
	private UserDao userDao;
	
	private String username;
	private String password;
	
	public String login(){
		System.out.println("sdddddddds.......===============");
		User user = userDao.query(username, password);
		if(user == null) {
			System.out.println("===============");
			return "input";
		}else
			return SUCCESS;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
