package org.amoyiki.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_USER")
public class User implements Serializable{


	private static final long serialVersionUID = 6770915569987104637L;
	private int id;
	private String username;
	private String password;
    public User() {
    }

    public User( String username, String password) {
        this.username = username;
        this.password = password;
    }

	
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "u_id", unique = true, nullable = false)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name = "u_name", unique = true, nullable = false)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name = "u_pwd", unique = true, nullable = false)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
