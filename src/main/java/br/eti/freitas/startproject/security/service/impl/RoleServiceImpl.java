package br.eti.freitas.startproject.security.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.eti.freitas.startproject.exception.ResourceNotFoundException;
import br.eti.freitas.startproject.model.Organization;
import br.eti.freitas.startproject.repository.RoleRepository;
import br.eti.freitas.startproject.repository.UserRepository;
import br.eti.freitas.startproject.security.constant.SecurityConstantMessage;
import br.eti.freitas.startproject.security.model.Role;
import br.eti.freitas.startproject.security.model.User;
import br.eti.freitas.startproject.security.service.RoleService;

@Transactional
@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<Role> getRoles() {
		List<Role> role = roleRepository.findAll();
		if (role.size() > 0) {
			return role;
		} else {
			throw new ResourceNotFoundException(SecurityConstantMessage.NOT_FOUND, null);
		}
	}

	@Override
	public Page<Role> getRoles(Pageable pageable) {
		Page<Role> role = roleRepository.findAll(pageable);
		if (role.getSize() > 0) {
			return role;
		} else {
			throw new ResourceNotFoundException(SecurityConstantMessage.NOT_FOUND, null);
		}
	}

	@Override
	public Role getRole(Long id) {
		Optional<Role> role = roleRepository.findById(id);
		return role.orElseThrow(() -> new ResourceNotFoundException(
				String.format(SecurityConstantMessage.NOT_EXISTS, Organization.class.getSimpleName(), id)));
	}

	@Override
	public Role getRole(String roleName) {
		Optional<Role> role = roleRepository.findByName(roleName);
		return role.orElseThrow(() -> new ResourceNotFoundException(
				String.format(SecurityConstantMessage.NOT_EXISTS, Organization.class.getSimpleName(), roleName)));
	}

	@Override
	public Role createRole(Role role) {
		role.setRoleId(null);
		return roleRepository.save(role);
	}

	@Override
	public void deleteRole(Long id) {
		try {
			roleRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException(String.format(SecurityConstantMessage.NOT_DELETE,
					Organization.class.getSimpleName(), id.toString()));
		}
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		User user = userRepository.findByUsername(username);
		Optional<Role> role = roleRepository.findByName(roleName);
		user.getRoles().add(role.get());
	}

}
