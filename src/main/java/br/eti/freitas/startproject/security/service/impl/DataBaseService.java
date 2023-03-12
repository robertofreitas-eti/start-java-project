package br.eti.freitas.startproject.security.service.impl;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.eti.freitas.startproject.model.Organization;
import br.eti.freitas.startproject.security.custom.CustomPermissionEvaluator;
import br.eti.freitas.startproject.security.model.Privilege;
import br.eti.freitas.startproject.security.model.Role;
import br.eti.freitas.startproject.security.model.User;
import br.eti.freitas.startproject.security.service.PrivilegeService;
import br.eti.freitas.startproject.security.service.RoleService;
import br.eti.freitas.startproject.security.service.UserService;
import br.eti.freitas.startproject.service.OrganizationService;

@Service
public class DataBaseService {

	private static final Logger LOG = LoggerFactory.getLogger(CustomPermissionEvaluator.class);

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PrivilegeService privilegeService;

	public void iniciarBancoTeste() {

		LOG.info("Database - Starting...");

		// Add Organization
		Organization org1 = new Organization("EMPRESA 1", "empresa1@teste.com.br", true);
		Organization org2 = new Organization("EMPRESA 2", "empresa2@teste.com.br", true);

		organizationService.createOrganization(org1);
		organizationService.createOrganization(org2);

		// Add users
		userService.createUser(
				new User("Bill Gates", "bill.gates@microsoft.com", "bill.gates", "1234", org1, new ArrayList<>()));
		userService.createUser(
				new User("Henry Ford", "henry.ford@ford.com", "henry.ford", "1234", org1, new ArrayList<>()));
		userService.createUser(
				new User("Mark Zuckerberg", "mark.zuckerberg@facebook.com", "mark.zuckerberg","1234", org1, new ArrayList<>()));
		userService.createUser(
				new User("Sergey Brin", "sergey.brin@odiseia.com", "sergey.brin", "1234", org2, new ArrayList<>()));
		userService.createUser(
				new User("Thomas Edson", "thomas.edson@energia.com", "thomas.edson", "1234", org2, new ArrayList<>()));


		// Add roles
		roleService.createRole(new Role("ADMIN", true));
		roleService.createRole(new Role("MANAGER", true));
		roleService.createRole(new Role("USER", true));

		// Add Privileges
		Privilege privOrganizationR = new Privilege("organization:read", true);
		Privilege privOrganizationW = new Privilege("organization:write", true);
		Privilege privOrganizationD = new Privilege("organization:delete", true);

		privilegeService.createPrivilege(privOrganizationR);
		privilegeService.createPrivilege(privOrganizationW);
		privilegeService.createPrivilege(privOrganizationD);

		Privilege privUserR = new Privilege("user:read", true);
		Privilege privUserW = new Privilege("user:write", true);
		Privilege privUserD = new Privilege("user:delete", true);

		privilegeService.createPrivilege(privUserR);
		privilegeService.createPrivilege(privUserW);
		privilegeService.createPrivilege(privUserD);

		Privilege privRoleR = new Privilege("role:read", true);
		Privilege privRoleW = new Privilege("role:write", true);
		Privilege privRoleD = new Privilege("role:delete", true);

		privilegeService.createPrivilege(privRoleR);
		privilegeService.createPrivilege(privRoleW);
		privilegeService.createPrivilege(privRoleD);

		// Add Privilege to Role ADMIN
		privilegeService.addPrivilegeToRole("ADMIN", "user:read");
		privilegeService.addPrivilegeToRole("ADMIN", "role:write");
		privilegeService.addPrivilegeToRole("ADMIN", "role:delete");

		privilegeService.addPrivilegeToRole("ADMIN", "organization:read");
		privilegeService.addPrivilegeToRole("ADMIN", "organization:write");
		privilegeService.addPrivilegeToRole("ADMIN", "organization:delete");

		// Add Privilege to Role MANAGER
		privilegeService.addPrivilegeToRole("MANAGER", "user:read");
		privilegeService.addPrivilegeToRole("MANAGER", "user:write");

		privilegeService.addPrivilegeToRole("MANAGER", "role:read");
		privilegeService.addPrivilegeToRole("MANAGER", "role:write");

		privilegeService.addPrivilegeToRole("MANAGER", "organization:read");
		privilegeService.addPrivilegeToRole("MANAGER", "organization:write");

		// Add Privilege to Role user
		privilegeService.addPrivilegeToRole("USER", "user:read");
		privilegeService.addPrivilegeToRole("USER", "role:read");
		privilegeService.addPrivilegeToRole("USER", "organization:read");

		// Add role to user
		roleService.addRoleToUser("bill.gates", "USER");
		roleService.addRoleToUser("henry.ford", "MANAGER");
		roleService.addRoleToUser("sergey.brin", "ADMIN");
		roleService.addRoleToUser("thomas.edson", "ADMIN");
		roleService.addRoleToUser("thomas.edson", "MANAGER");
		//roleService.addRoleToUser("mark.zuckerberg", "user");

		// Add privilege to user
		userService.addPrivilegeToUser("mark.zuckerberg", "organization:read");
		userService.addPrivilegeToUser("mark.zuckerberg", "role:read");
		userService.addPrivilegeToUser("mark.zuckerberg", "privilege:read");

		LOG.info("Database - Start complete");

	}

}
