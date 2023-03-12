	package br.eti.freitas.startproject.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.eti.freitas.startproject.repository.UserRepository;
import br.eti.freitas.startproject.security.constant.SecurityConstantMessage;
import br.eti.freitas.startproject.security.custom.CustomUserPrincipal;
import br.eti.freitas.startproject.security.model.User;;

@Service
public class SecurityUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

		try {
			User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
			if (user == null) {
				throw new UsernameNotFoundException(String.format(SecurityConstantMessage.USER_NOT_FOUND, usernameOrEmail));
			}

			return new CustomUserPrincipal(user);

		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
