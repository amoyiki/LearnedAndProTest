package org.amoyiki.oa.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.amoyiki.oa.entities.User;
import org.amoyiki.oa.service.UserService;
import org.amoyiki.oa.utils.JacksonUtil;
import org.amoyiki.oa.viewModel.Message;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


public class UserAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private UserService userService;
	private String username;
	private String password;
	private String captcha;
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
	
	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String login(){
		
		PrintWriter out = null;
		//防止中文乱码
		HttpServletResponse httpServletResponse = ServletActionContext.getResponse();
		httpServletResponse.setContentType("text/plain");
		httpServletResponse.setCharacterEncoding("utf-8");
		String json=null;
		Message message = new Message();
		message.setTitle("登录提示");
		
		String kaptchaExpected = (String)ActionContext.getContext().getSession()
				.get(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		System.out.println(kaptchaExpected);
		System.out.println("==========================");
		System.out.println(captcha);
		
		if (username == null || username.equals("")) {
			message.setMessage("用户名不能为空");
			
		}else if (password == null || password.equals("")) {
			message.setMessage("用户名不能为空");
		}else if (captcha == null || !captcha.equals(kaptchaExpected)){ 
			message.setMessage("验证码错误");
		}else {
			User user = userService.login(username, password);
			if (user == null) {
				message.setMessage("登录失败，请检查用户名或密码！");
			}else{
				message.setMessage("登录成功");
				message.setStatus(true);  
			}
			
		}
		
		try {
			out = httpServletResponse.getWriter();
			json = JacksonUtil.toJson(message);
			System.out.println(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print(json);
		out.close();
		
		
		
		return null;
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
