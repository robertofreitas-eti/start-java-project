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
import br.eti.freitas.startproject.repository.PrivilegeRepository;
import br.eti.freitas.startproject.repository.RoleRepository;
import br.eti.freitas.startproject.security.constant.SecurityConstantMessage;
import br.eti.freitas.startproject.security.model.Privilege;
import br.eti.freitas.startproject.security.model.Role;
import br.eti.freitas.startproject.security.service.PrivilegeService;

@Transactional
@Service
public class PrivilegeServiceImpl implements PrivilegeService {

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Privilege getPrivilege(String privilegeName) {
		return privilegeRepository.findByName(privilegeName);
	}

	@Override
	public List<Privilege> getPrivileges() {
		return privilegeRepository.findAll();
	}

	@Override
	public Page<Privilege> getPrivileges(Pageable pageable) {
		Page<Privilege> privilege = privilegeRepository.findAll(pageable);
		if (privilege.getSize() > 0) {
			return privilege;
		} else {
			throw new ResourceNotFoundException(SecurityConstantMessage.NOT_FOUND, null);
		}
	}

	@Override
	public Privilege createPrivilege(Privilege privilege) {
		privilege.setPrivilegeId(null);
		return privilegeRepository.save(privilege);
	}

	@Override
	public Privilege updatePrivilege(Long id, Privilege privilege) {
		Optional<Privilege> privilegeUpdate = privilegeRepository.findById(id);
		if (privilegeUpdate.isPresent()) {
			privilege.setName(privilegeUpdate.get().getName());
			privilege.setEnabled(privilegeUpdate.get().isEnabled());
			return privilegeRepository.save(privilege);
		} else {
			throw new ResourceNotFoundException(SecurityConstantMessage.NOT_FOUND, null);
		}
	}

	@Override
	public void deletePrivilege(Long id) {
		try {
			privilegeRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException(
					String.format(SecurityConstantMessage.NOT_DELETE, Privilege.class.getSimpleName(), id.toString()));
		}
	}

	@Override
	public void addPrivilegeToRole(String roleName, String privilegeName) {
		Role role = roleRepository.findByName(roleName).get();
		Privilege privilege = privilegeRepository.findByName(privilegeName);
		role.getPrivileges().add(privilege);
	}

}
