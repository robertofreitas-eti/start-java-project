package br.eti.freitas.startproject.security.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.eti.freitas.startproject.security.model.Privilege;
import br.eti.freitas.startproject.security.model.Role;
import br.eti.freitas.startproject.security.model.User;

public class CustomUserPrincipal implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final User user;

	public CustomUserPrincipal(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		final List<GrantedAuthority> authorities = new ArrayList<>();
		final List<String> privileges = new ArrayList<>();
		final List<Privilege> collection = new ArrayList<>();

		// collect roles
		for (final Role role : user.getRoles()) {
			privileges.add(role.getName());
			collection.addAll(role.getPrivileges());
		}

		// collect priveleges from roles
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }

		// collect direct priveleges
		for (final Privilege privilege : user.getPrivileges()) {
			privileges.add(privilege.getName());
		}

        // convert to authorities
        for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
        return authorities;

	}

	public User getUser() {
		return user;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
