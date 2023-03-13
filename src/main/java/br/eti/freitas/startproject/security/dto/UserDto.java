package br.eti.freitas.startproject.security.dto;

/**
 * This class is responsible for allowing data transfer from <b>User</b> between subsystems
 *
 * @author Roberto Freitas
 * @version 1.0
 * @since 2023-03-01
 */
public class UserDto {

	private String name;
	private String email;
	private String username;

	public UserDto(String name, String email, String username) {
		this.name = name;
		this.email = email;
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserDto [name=").append(name).append(", email=").append(email).append(", username=")
				.append(username).append("]");
		return builder.toString();
	}

}
