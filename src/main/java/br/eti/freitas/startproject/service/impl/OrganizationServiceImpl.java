package br.eti.freitas.startproject.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.eti.freitas.startproject.exception.ResourceNotFoundException;
import br.eti.freitas.startproject.model.Organization;
import br.eti.freitas.startproject.repository.OrganizationRepository;
import br.eti.freitas.startproject.security.constant.SecurityConstantMessage;
import br.eti.freitas.startproject.service.OrganizationService;

@Transactional
@Service
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private OrganizationRepository organizationRepository;

	@Override
	public List<Organization> getOrganizations() {
		List<Organization> organization = organizationRepository.findAll();
		if (organization.size() > 0) {
			return organization;
		} else {
			throw new ResourceNotFoundException(SecurityConstantMessage.NOT_FOUND, null);
		}
	}

	@Override
	public Page<Organization> getOrganizations(Pageable pageable) {
		Page<Organization> organization = organizationRepository.findAll(pageable);
		if (organization.getSize() > 0) {
			return organization;
		} else {
			throw new ResourceNotFoundException(SecurityConstantMessage.NOT_FOUND, null);
		}
	}

	@Override
	public Organization getOrganization(Long id) {
		Optional<Organization> organization = organizationRepository.findById(id);
		return organization.orElseThrow(() -> new ResourceNotFoundException(
				String.format(SecurityConstantMessage.NOT_EXISTS, Organization.class.getSimpleName(), id.toString())));
	}

	@Override
	public Organization createOrganization(Organization organization) {
		organization.setOraganizationKey(UUID.randomUUID());
		return organizationRepository.save(organization);
	}

	@Override
	public Organization updateOrganization(Long id, Organization organization) {
		Optional<Organization> organizationUpdate = organizationRepository.findById(id);
		organization.setName(organizationUpdate.get().getName());
		organization.setEmail(organizationUpdate.get().getEmail());
		organization.setEnabled(organizationUpdate.get().isEnabled());
		return organizationRepository.save(organization);
	}

	@Override
	public void deleteOrganization(Long id) {
		try {
			organizationRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException(String.format(SecurityConstantMessage.NOT_DELETE,
					Organization.class.getSimpleName(), id.toString()));
		}
	}

}
