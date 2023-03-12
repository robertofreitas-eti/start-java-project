package br.eti.freitas.startproject.security.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.eti.freitas.startproject.security.model.Privilege;

public interface PrivilegeService {

	Privilege getPrivilege(String privilegeName);

	List<Privilege> getPrivileges();

	Page<Privilege> getPrivileges(Pageable pageable);

	Privilege createPrivilege(Privilege privilege);

	Privilege updatePrivilege(Long id, Privilege privilege);

	void deletePrivilege(Long Id);

	void addPrivilegeToRole(String roleName, String privilegeName);

}
