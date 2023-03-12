package br.eti.freitas.startproject.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.eti.freitas.startproject.model.Organization;

public interface OrganizationService {

	public List<Organization> getOrganizations();

	public Page<Organization> getOrganizations(Pageable pageable);

	public Organization getOrganization(Long id);

	public Organization createOrganization(Organization organization);

	public Organization updateOrganization(Long id, Organization organization);

	public void deleteOrganization(Long id);

}
