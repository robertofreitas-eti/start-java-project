package br.eti.freitas.startproject.security.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.eti.freitas.startproject.security.model.Role;

public interface RoleService {

	public List<Role> getRoles();

	public Page<Role> getRoles(Pageable pageable);

	public Role getRole(Long id);

	public Role getRole(String roleName);

	public Role createRole(Role role);

	public void deleteRole(Long Id);

	public void addRoleToUser(String username, String roleName);

}
