package br.eti.freitas.startproject.security.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.eti.freitas.startproject.repository.RoleRepository;
import br.eti.freitas.startproject.repository.UserRepository;
import br.eti.freitas.startproject.security.constant.SecurityConstantMessage;
import br.eti.freitas.startproject.security.dto.LoginDto;
import br.eti.freitas.startproject.security.dto.SignUpDto;
import br.eti.freitas.startproject.security.dto.TokenDto;
import br.eti.freitas.startproject.security.model.Role;
import br.eti.freitas.startproject.security.model.User;
import br.eti.freitas.startproject.security.service.impl.SecurityUserDetailsServiceImpl;
import br.eti.freitas.startproject.security.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
* This controller is responsible for the management of the <b>Authenticate</b>
*
* @author  Roberto Freitas
* @version 1.0
* @since   2023-03-01
*/
@RestController
@RequestMapping("/api/auth")
@Api(tags = "Authenticate", description = "Endpoints for authenticate users")
public class AuthenticateController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private SecurityUserDetailsServiceImpl securityUserDetailsService;

	@Autowired
	private JwtUtil jwtTokenService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation( value = "Logs user into the system"
				 , notes = "Method used to authenticate user on platform")
	public ResponseEntity<Object> authenticateUser(@RequestBody @Validated LoginDto loginDto) throws Exception {

		try {
			final Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                		loginDto.getUsername(),
	                		loginDto.getPassword()
	                )
	        );
			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (DisabledException e) {
			throw new Exception(SecurityConstantMessage.USER_DISABLED, e);
		} catch (BadCredentialsException e) {
			throw new Exception(SecurityConstantMessage.INVALID_CREDENTIALS, e);
		}

        UserDetails userDetails = securityUserDetailsService.loadUserByUsername(loginDto.getUsername());

		String token = jwtTokenService.generateToken(userDetails);

		TokenDto tokenDto = new TokenDto("Bearer", token, null);

		return ResponseEntity.ok().body(tokenDto);

	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation( value = "Create user"
				 , notes = "Method used to create a new user on the platform")
	public ResponseEntity<Object> registerUser(@RequestBody SignUpDto signUpDto) {

		if (userRepository.existsByUsername(signUpDto.getUsername())) {
			return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(signUpDto.getEmail())) {
			return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
		}

		User user = new User();
		user.setName(signUpDto.getName());
		user.setUsername(signUpDto.getUsername());
		user.setEmail(signUpDto.getEmail());
		user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

		Role role = roleRepository.findByName("USER").get();
		user.setRoles(Collections.singleton(role));

		userRepository.save(user);

		return ResponseEntity.created(null).build();

	}

}
