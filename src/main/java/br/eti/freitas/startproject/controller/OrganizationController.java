package br.eti.freitas.startproject.controller;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.eti.freitas.startproject.dto.OrganizationDto;
import br.eti.freitas.startproject.model.Organization;
import br.eti.freitas.startproject.service.OrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
* This controller is responsible for the management of the <b>Organization</b>
*
* @author  Roberto Freitas
* @version 1.0
* @since   2023-03-01
*/
@RestController
@RequestMapping(value = "/api/v1")
@Api(tags = "Organization", description = "Endpoints for managing Organization")
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized")
					  , @ApiResponse(code = 404, message = "Not found")
					  , @ApiResponse(code = 500, message = "Internal error")
                      }
			  )
public class OrganizationController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private OrganizationService organizationService;

	@PreAuthorize("hasPermission('organization', 'read')")
	@GetMapping(value = "/organizations")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Find all Oganizations", notes = "Method used to find all Organizations")
	public List<OrganizationDto> readOganizations() {
		return organizationService.getOrganizations().stream()
				.map(organization -> modelMapper.map(organization, OrganizationDto.class)).collect(Collectors.toList());
	}

	@PreAuthorize("hasPermission('organization', 'read')")
	@GetMapping(value = "/organization/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Find an Organization by id", notes = "Method used to find an Organization by id")
	public ResponseEntity<OrganizationDto> readOrganization(@PathVariable("id") long id) {
		Organization organization = organizationService.getOrganization(id);
		if (!organization.getName().isEmpty()) {
			OrganizationDto organizationDto = modelMapper.map(organization, OrganizationDto.class);
			return ResponseEntity.ok().body(organizationDto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("hasPermission('organization', 'read')")
	@GetMapping(value = "/organizations/page")
	@ResponseStatus(HttpStatus.PARTIAL_CONTENT)
	@ApiOperation(value = "Find all Organizations", notes = "Method used to find all Organizations and show per page")
	public ResponseEntity<Page<Organization>> readOganizations(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "20") Integer size,
			@RequestParam(value = "sort", defaultValue = "name") String sort,
			@RequestParam(value = "direction", defaultValue = "ASC") Direction direction) {

		PageRequest pageRequest = PageRequest.of(page, size, direction, sort);
		Page<Organization> organizationsPage = organizationService.getOrganizations(pageRequest);

		if (organizationsPage.getContent().isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(organizationsPage);
		}
	}

	@PreAuthorize("hasPermission('organization', 'write')")
	@PostMapping(value = "/organization")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Add a new Organization", notes = "Method used to add a new Organization")
	public ResponseEntity<Void> writeOrganization(@Validated @RequestBody OrganizationDto organizationDto) {
		Organization organization = organizationService
				.createOrganization(modelMapper.map(organizationDto, Organization.class));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(organization.getOrganizationId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PreAuthorize("hasPermission('organization', 'write') ")
	@PutMapping(value = "/organization/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Update an existing Organization", notes = "Method used to update an existing Organization")
	public ResponseEntity<Void> writeOrganization(@PathVariable("id") long id,
			@RequestBody OrganizationDto organizationDto) {
		Organization organization = modelMapper.map(organizationDto, Organization.class);
		organizationService.updateOrganization(id, organization);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasPermission('organization', 'delete')")
	@DeleteMapping(value = "/organization/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete Organization by id", notes = "Method used to delete an Organization by Id")
	public ResponseEntity<Void> deleteOrganization(@PathVariable("id") long id) throws Exception {
		organizationService.deleteOrganization(id);
		return ResponseEntity.noContent().build();
	}

}
