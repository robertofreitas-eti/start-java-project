package br.eti.freitas.startproject.security.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.eti.freitas.startproject.security.dto.UserDto;
import br.eti.freitas.startproject.security.model.User;
import br.eti.freitas.startproject.security.service.UserService;
import io.swagger.annotations.Api;

/**
* This controller is responsible for the management of the <b>Users</b>
*
* @author  Roberto Freitas
* @version 1.0
* @since   2023-03-01
*/
@RestController
@RequestMapping("/api/v1")
@Api(tags = "Users")
public class UserController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserService userService;

	@PreAuthorize("hasPermission('user', 'read') or hasRole('ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public List<UserDto> getUsers(){
		return userService.getUsers().stream().map(user -> modelMapper.map(user, UserDto.class))
				.collect(Collectors.toList());
	}

	@PreAuthorize("hasPermission('user', 'read') or hasRole('ADMIN')")
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<UserDto> getUserById(@PathVariable(value = "id") long id) {
		User user = userService.getById(id);
		if (!user.getUsername().isEmpty()) {
			UserDto userDto = modelMapper.map(user, UserDto.class);
			return ResponseEntity.ok().body(userDto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("hasPermission('user', 'write') or hasRole('ADMIN')")
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> saveUser(@RequestBody UserDto userDto) {
		User user = userService.createUser(modelMapper.map(userDto, User.class));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getUserId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@PreAuthorize("hasPermission('user', 'write') or hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UserDto> updateUser(@PathVariable(value = "id") long id, @RequestBody UserDto userDto) {
		User userDetails = modelMapper.map(userDto, User.class);
		userService.updateUser(id, userDetails);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasPermission('user', 'delete') or hasRole('ADMIN')")
	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable(value = "id") long id) {
		userService.deleteById(id);
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

}
