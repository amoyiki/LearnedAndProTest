package org.amoyiki.action;

import javax.annotation.Resource;

import org.amoyiki.dao.TestDao;
import org.amoyiki.entity.User;
import org.amoyiki.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Controller("userAction")
@Scope("prototype")
public class UserAction extends ActionSupport{

	private static final long serialVersionUID = -8985876646100092685L;

	
	@Resource(name="testDao")
	private TestDao testDao;
	@Resource(name="userService")
	private UserService userService;
	private String username;
	private String password;
	public String login(){
		System.out.println("进入login方法");
		Subject subject = SecurityUtils.getSubject();  
		UsernamePasswordToken token = new UsernamePasswordToken(username,password);
		try {    
			subject.login(token);
			return SUCCESS;
		}catch (AuthenticationException e) { //验证身份失败
			ActionContext ct=ActionContext.getContext();
			ct.put("emsg", e.getMessage());
            return "input";  
		}    
	
	
	}
	public String add(){
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		userService.add(user);
		
		
		return "input";	
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
