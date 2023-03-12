package br.eti.freitas.startproject.security.dto;

public class TokenDto {

	private String token;
	private String type;
	private String expires_in;

	public TokenDto(String type, String token, String expires_in) {
		this.type = type;
		this.token = token;
		this.expires_in = expires_in;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TokenDto [token=").append(token).append(", type=").append(type).append(", expires_in=")
				.append(expires_in).append("]");
		return builder.toString();
	}

}
