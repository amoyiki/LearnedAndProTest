package org.amoyiki.oa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_tree")
public class TreeNode implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	private String id;   // 节点id
	private String pid; //父节点id
    private String text; // 显示的节点文本
    private String url; //节点的链接
    
    
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "text", unique = true, nullable = false)
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Column(name = "pid", unique = false, nullable = true)
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	@Column(name = "url", unique = false, nullable = true)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public TreeNode() {

	}
	public TreeNode(String id, String text, String pid, String url) {
		this.id = id;
		this.text = text;
		this.pid = pid;
		this.url = url;
	}
	@Override
	public String toString() {
		return "TreeNode [id=" + id + ", pid=" + pid + ", text=" + text
				+ ", url=" + url + "]";
	}
    
	
	
}
