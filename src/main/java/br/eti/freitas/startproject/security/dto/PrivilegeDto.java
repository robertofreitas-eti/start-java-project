package br.eti.freitas.startproject.security.dto;

/**
 * This class is responsible for allowing data transfer from <b>Privilege</b> between subsystems
 *
 * @author Roberto Freitas
 * @version 1.0
 * @since 2023-03-12
 */
public class PrivilegeDto {

	private Long privilegeId;
	private String name;
	private boolean enabled;

	public PrivilegeDto() {
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
