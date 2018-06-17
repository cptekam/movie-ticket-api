package com.movie.model;

import springfox.documentation.annotations.Cacheable;

import javax.management.relation.Role;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Set;

@Entity
public class Users {

	@Id
	private int userid;	

	private String name;

	private String password;

	public Users() {}

	public Users(int userid, String name, String password) {
		super();
		this.userid = userid;
		this.name = name;
		this.password = password;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	@Cacheable("users")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	
	
}
