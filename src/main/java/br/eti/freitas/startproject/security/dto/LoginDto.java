package br.eti.freitas.startproject.security.dto;

public class LoginDto {

	private String username;
	private String password;

	public LoginDto(String username, String password) {
		this.username = username;
		this.password = password;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoginDto [username=").append(username).append(", password=").append(password).append("]");
		return builder.toString();
	}

}
