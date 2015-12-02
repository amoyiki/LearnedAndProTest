package org.amoyiki.oa.viewModel;

import java.io.Serializable;

/**
 * 
 * <p>Title:Message</p>
 * <p>Description: 后台与前台的信息传递Bean</p>
 * @author amoyiki
 * @date 2015年12月1日 上午12:30:21
 *
 */
public class Message implements Serializable{
	
	private static final long serialVersionUID = -8367114872693602164L;
	public String title = "提示";
	public String message;
	public boolean status = false;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
