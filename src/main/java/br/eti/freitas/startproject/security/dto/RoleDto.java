package br.eti.freitas.startproject.security.dto;

public class RoleDto {

	private String name;
	private String description;
	private boolean enabled;

	public RoleDto(String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RoleDto [name=").append(name).append(", description=").append(description).append(", enabled=")
				.append(enabled).append("]");
		return builder.toString();
	}

}
