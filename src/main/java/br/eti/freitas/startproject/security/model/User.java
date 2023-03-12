package br.eti.freitas.startproject.security.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.eti.freitas.startproject.model.Organization;

@Entity
@Table(name = "USER")
@SQLDelete(sql="UPDATE USER SET deleted = true WHERE userId=?")
@Where(clause="deleted=false")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;

	@Column(length = 60, nullable = false)
	private String name;

	@Column(name="email", length = 100, nullable = false)
	private String email;

	@Column(length = 40, nullable = false, unique = true)
	private String username;

	private String password;

	@Column(columnDefinition = "boolean default false")
	private boolean deleted;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "organizationId", referencedColumnName = "organizationId")
	private Organization organization;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "USERS_ROLES",
				joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId")},
				  inverseJoinColumns = {@JoinColumn(name = "roleId", referencedColumnName = "roleId")})
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private Collection<Role> roles = new ArrayList<>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "USERS_PRIVILEGES",
				 joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId")},
				   inverseJoinColumns = {@JoinColumn(name = "privilegeId", referencedColumnName = "privilegeId")})
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private Collection<Privilege> privileges = new ArrayList<>();

	public User() {
	}

	public User(String name, String email, String username, String password, Organization organization, Collection<Role> roles) {
		this.name = name;
		this.email = email;
		this.username = username;
		this.password = password;
		this.organization = organization;
		this.roles = roles;
		this.deleted = false;
	}

	public User(Collection<Privilege> privileges) {
		this.privileges = privileges;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public Collection<Privilege> getPrivileges() {
		return privileges;
	}

	public void setDirect_privileges(Collection<Privilege> privileges) {
		this.privileges = privileges;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
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
		builder.append("User [userId=").append(userId).append(", name=").append(name).append(", email=").append(email)
				.append(", username=").append(username).append(", password=").append(password).append(", deleted=")
				.append(deleted).append(", organization=").append(organization).append(", roles=").append(roles)
				.append(", privileges=").append(privileges).append("]");
		return builder.toString();
	}

}
