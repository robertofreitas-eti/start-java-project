package br.eti.freitas.startproject.security.dto;

/**
 * This class is responsible for allowing data transfer from <b>SignUp</b> between subsystems
 *
 * @author Roberto Freitas
 * @version 1.0
 * @since 2023-03-12
 */
public class SignUpDto {

	private String name;
	private String username;
	private String email;
	private String password;

	public SignUpDto() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		builder.append("SignUpDto [name=").append(name).append(", username=").append(username).append(", email=")
				.append(email).append(", password=").append(password).append("]");
		return builder.toString();
	}

}
