package net.octacomm.sample.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User implements Domain {
	
	@Id
	private String id;
	
	private String password;

	private String name;

	private String department;
	
	private String email;

	private String phone;
}
