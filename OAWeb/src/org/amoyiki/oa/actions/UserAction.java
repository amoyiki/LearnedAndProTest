package org.amoyiki.oa.actions;

import java.util.Map;

import org.amoyiki.oa.entities.User;
import org.amoyiki.oa.service.UserService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport{

	private UserService userService;
	private String username;
	private String password;
	private String confirm;
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
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

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	
	public String login(){
		Map<String,Object> request = (Map<String,Object>)ActionContext.getContext().get("request");
		if (username == null || username.equals("")) {
			request.put("msg", "用户名不能为空");
			
		}else if (password == null || password.equals("")) {
			request.put("msg", "密码不能为空");
		}else{
			User user = userService.login(username, password);
			if (user == null) {
				request.put("msg", "登录失败，请检查用户名或密码！");
			}else{
				ActionContext.getContext().getSession().put("user", user);
				return SUCCESS;
			}
		}
		return INPUT;
	}
	public String register(){
		Map<String,Object> request = (Map<String,Object>)ActionContext.getContext().get("request");
		if (username == null || username.equals("")) {
			request.put("msg", "用户名不能为空");
			
		}else if (password == null || password.equals("")) {
			request.put("msg", "密码不能为空");
		}else if(!password.equals(confirm)){
			request.put("msg", "两次密码不一致");
		}else{
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			if (userService.regisiter(user)) {
				return INPUT;
			}
			request.put("msg", "用户名已经被使用了");
		}
		
		return "register";
	}
	

}
