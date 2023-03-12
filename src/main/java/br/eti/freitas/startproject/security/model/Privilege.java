package br.eti.freitas.startproject.security.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "PRIVILEGE")
@SQLDelete(sql="UPDATE PRIVILEGE SET deleted=true WHERE privilegeId=?")
@Where(clause="deleted=false")
public class Privilege implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long privilegeId;

	@Column(length = 40, nullable = false, unique = true)
	private String name;

	@Column(columnDefinition = "boolean default true")
	private boolean enabled;

	@JsonIgnore
	@Column(columnDefinition = "boolean default false")
	private boolean deleted;

	@ManyToMany(mappedBy = "privileges")
	private Collection<Role> roles;

	@ManyToMany(mappedBy = "privileges")
	private Collection<User> users;

	public Privilege() {
	}

	public Privilege(String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
		this.deleted = false;
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

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public Collection<User> getUsers() {
		return users;
	}

	public void setUsers(Collection<User> users) {
		this.users = users;
	}

	@Override
	public String getAuthority() {
		return this.name;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Privilege [privilegeId=").append(privilegeId).append(", name=").append(name)
				.append(", enabled=").append(enabled).append(", roles=").append(roles).append("]");
		return builder.toString();
	}

}
