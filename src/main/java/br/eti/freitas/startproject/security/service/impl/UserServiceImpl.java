package br.eti.freitas.startproject.security.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.eti.freitas.startproject.exception.ResourceNotFoundException;
import br.eti.freitas.startproject.repository.PrivilegeRepository;
import br.eti.freitas.startproject.repository.UserRepository;
import br.eti.freitas.startproject.security.constant.SecurityConstantMessage;
import br.eti.freitas.startproject.security.model.Privilege;
import br.eti.freitas.startproject.security.model.User;
import br.eti.freitas.startproject.security.service.UserService;

@Transactional
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User getUser(String username) {
		try {
			User user = userRepository.findByUsername(username);
			return user;
		} catch (Exception ex) {
			throw new ResourceNotFoundException(SecurityConstantMessage.NOT_FOUND, ex);
		}
	}

	@Override
	public List<User> getUsers() {
		List<User> user = userRepository.findAll();
		if (user.size() > 0) {
			return user;
		} else {
			throw new ResourceNotFoundException(SecurityConstantMessage.NOT_FOUND, null);
		}

	}

	@Override
	public User getById(long id) {
		try {
			Optional<User> user = userRepository.findById(id);
			return user.get();
		} catch (Exception ex) {
			throw new ResourceNotFoundException(SecurityConstantMessage.NOT_FOUND, ex);
		}

	}

	@Override
	public User createUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public void updateUser(long id, User userDetails) {
		try {
			User user = userRepository.findById(id).get();
			user.setEmail(userDetails.getEmail());
			userRepository.save(user);
		} catch (Exception ex) {
			throw new ResourceNotFoundException(SecurityConstantMessage.NOT_FOUND, ex);
		}
	}

	@Override
	public void deleteById(long id) {
		try {
			userRepository.deleteById(id);
		} catch (Exception ex) {
			throw new ResourceNotFoundException(SecurityConstantMessage.NOT_FOUND, ex);
		}

	}

	@Override
	public void addPrivilegeToUser(String username, String privilegeName) {
		User user = userRepository.findByUsername(username);
		Privilege privilege = privilegeRepository.findByName(privilegeName);
		user.getPrivileges().add(privilege);
	}

}
