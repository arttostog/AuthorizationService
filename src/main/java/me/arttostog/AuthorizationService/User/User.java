package me.arttostog.AuthorizationService.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;


@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private String mail;
	private String username;
	private String password;
	private Long accessToken;

	public User() {}

	public User(String mail, String username, String password) {
		this.mail = mail;
		this.username = username;
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public UUID getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setAccessToken(Long accessToken) {
		this.accessToken = accessToken;
	}
}
