package org.amoyiki.oa.viewModel;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * <p>Title:EasyUITree</p>
 * <p>Description: 后台动态生成树传递到前台</p>
 * @author amoyiki
 * @date 2015年12月2日 下午4:22:40
 *
 */
public class EasyUITree implements Serializable{

	
	private static final long serialVersionUID = 1L;

	private String id;  
    private String text;  
    private Boolean checked = false;  
    private Map<String, Object> attributes; //tree attributes值设置，例如url:xxx.action
    private String state = "open";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	} 
}
