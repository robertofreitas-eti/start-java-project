package br.eti.freitas.startproject.security.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.eti.freitas.startproject.security.dto.RoleDto;
import br.eti.freitas.startproject.security.model.Role;
import br.eti.freitas.startproject.security.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
* This controller is responsible for the management of the <b>Roles</b>
*
* @author  Roberto Freitas
* @version 1.0
* @since   2023-03-01
*/
@RestController
@RequestMapping("/api/v1")
@Api(tags = "Roles", description = "Endpoints for managing Roles")
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized")
					  , @ApiResponse(code = 404, message = "Not found")
					  , @ApiResponse(code = 500, message = "Internal error")
                      }
			  )
public class RoleController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private RoleService roleService;

	@PreAuthorize("hasPermission('role', 'read') or hasRole('ADMIN')")
	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Find all Roles", notes = "Method used to find all Roles")
	public List<RoleDto> getRoles() {
		return roleService.getRoles().stream().map(role -> modelMapper.map(role, RoleDto.class))
				.collect(Collectors.toList());
	}

	@PreAuthorize("hasPermission('role', 'read') or hasRole('ADMIN')")
	@RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Find an Organization by id", notes = "Method used to find an Organization by id")
	public ResponseEntity<RoleDto> getRole(@PathVariable("id") long id) {
		Role role = roleService.getRole(id);
		if (!role.getName().isEmpty()) {
			RoleDto roleDto = modelMapper.map(role, RoleDto.class);
			return ResponseEntity.ok().body(roleDto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("hasPermission('role', 'read') or hasRole('ADMIN')")
	@RequestMapping(value = "/role/{roleName}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Find an Organization by name", notes = "Method used to find an Organization by name")
	public ResponseEntity<RoleDto> getRole(@RequestParam String roleName) {
		Role role = roleService.getRole(roleName);
		if (!role.getName().isEmpty()) {
			RoleDto roleDto = modelMapper.map(role, RoleDto.class);
			return ResponseEntity.ok().body(roleDto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("hasPermission('role', 'read') or hasRole('ADMIN')")
	@RequestMapping(value= "/roles/page", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.PARTIAL_CONTENT)
	@ApiOperation(value = "Find all Roles", notes = "Method used to find all Roles and show per page")
	public ResponseEntity<Page<Role>> getRoles(
		@RequestParam(value = "page", defaultValue = "0") Integer page,
		@RequestParam(value = "size", defaultValue = "20") Integer size,
		@RequestParam(value = "sort", defaultValue = "name") String sort,
		@RequestParam(value = "direction", defaultValue = "ASC") Direction direction) {

		PageRequest pageRequest = PageRequest.of(page, size, direction, sort);
		Page<Role> rolesPage = roleService.getRoles(pageRequest);

		if (rolesPage.getContent().isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(rolesPage);
		}
	}

	@PreAuthorize("hasPermission('role', 'write') or hasRole('ADMIN')")
	@RequestMapping(value = "/role", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Add a new Role", notes = "Method used to add a new Role")
	public ResponseEntity<Void> writeRole(@Validated @RequestBody RoleDto roleDto) {
		Role role =roleService.createRole(modelMapper.map(roleDto, Role.class));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(role.getRoleId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PreAuthorize("hasPermission('role', 'delete')")
	@RequestMapping(value = "/role/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete Role by id", notes = "Method used to delete an Role by Id")
	public ResponseEntity<Void> deleteRole(@PathVariable("id") long id) throws Exception {
		roleService.deleteRole(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasPermission('role:user', 'write') or hasRole('ADMIN')")
	@RequestMapping(value = "/role/user", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Add a Role to the User", notes = "Method used to add a new Role to the User")
	public ResponseEntity<Void> addRoleToUser(@RequestBody RoleToUserForm form) {
		roleService.addRoleToUser(form.getUsername(), form.getRoleName());
		return ResponseEntity.ok().build();
	}
}

class RoleToUserForm {

	private String username;
	private String roleName;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
