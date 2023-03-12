package br.eti.freitas.startproject.dto;

import java.util.UUID;

public class OrganizationDto {

	private Long organizationId;

	private UUID organizationKey;

	private String name;

	private String email;

	private boolean enabled;

	public OrganizationDto(UUID organizationKey, String name, String email, boolean enabled) {
		super();
		this.organizationKey = organizationKey;
		this.name = name;
		this.email = email;
		this.enabled = enabled;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public UUID getOrganizationKey() {
		return organizationKey;
	}

	public void setOrganizationKey(UUID organizationKey) {
		this.organizationKey = organizationKey;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OrganizationDto [organizationId=").append(organizationId).append(", organizationKey=")
				.append(organizationKey).append(", name=").append(name).append(", email=").append(email)
				.append(", enabled=").append(enabled).append("]");
		return builder.toString();
	}

}
