package br.eti.freitas.startproject.security.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.eti.freitas.startproject.security.dto.PrivilegeDto;
import br.eti.freitas.startproject.security.model.Privilege;
import br.eti.freitas.startproject.security.service.PrivilegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
* This controller is responsible for the management of the <b>Privilege</b>
*
* @author  Roberto Freitas
* @version 1.0
* @since   2023-03-01
*/
@RestController
@RequestMapping("/api/v1")
@Api(tags = "Privileges", description = "Endpoints for managing Privileges")
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized")
					  , @ApiResponse(code = 404, message = "Not found")
					  , @ApiResponse(code = 500, message = "Internal error")
                      }
			  )
public class PrivilegeController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PrivilegeService privilegeService;

	@PreAuthorize("hasPermission('privilege','read') or hasRole('ADMIN')")
	@RequestMapping(value = "/privileges", method = RequestMethod.GET)
	@ApiOperation(value = "Get Privilege")
	public List<PrivilegeDto> getPrivileges() {
		return privilegeService.getPrivileges().stream()
				.map(privilege -> modelMapper.map(privilege, PrivilegeDto.class)).collect(Collectors.toList());
	}

	@PreAuthorize("hasPermission('privilege','read') or hasRole('ADMIN')")
	@RequestMapping(value = "/privilege", method = RequestMethod.GET)
	@ApiOperation(value = "Get Privilege by Name")
	public ResponseEntity<PrivilegeDto> getPrivilege(@RequestParam String privilegeName) {
		Privilege privilege = privilegeService.getPrivilege(privilegeName);
		if (!privilege.getName().isEmpty()) {
			PrivilegeDto privilegeDto = modelMapper.map(privilege, PrivilegeDto.class);
			return ResponseEntity.ok().body(privilegeDto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("hasPermission('privilege','read') or hasRole('ADMIN')")
	@RequestMapping(value = "/privileges/page", method = RequestMethod.GET)
	@ApiOperation(value = "Get Privilege")
	public ResponseEntity<Page<Privilege>> getPrivileges(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "20") Integer size,
			@RequestParam(value = "sort", defaultValue = "name") String sort,
			@RequestParam(value = "direction", defaultValue = "ASC") Direction direction) {

		PageRequest pageRequest = PageRequest.of(page, size, direction, sort);
		Page<Privilege> privilegesPage = privilegeService.getPrivileges(pageRequest);

		if (privilegesPage.getContent().isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(privilegesPage);
		}

	}
}
