package br.eti.freitas.startproject.security.dto;

public class PrivilegeDto {

	private Long privilegeId;
	private String name;
	private boolean enabled;

	public PrivilegeDto(String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
	}

	public Long getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(Long privilegeId) {
		this.privilegeId = privilegeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		builder.append("PrivilegeDto [privilegeId=").append(privilegeId).append(", name=").append(name)
				.append(", enabled=").append(enabled).append("]");
		return builder.toString();
	}

}
