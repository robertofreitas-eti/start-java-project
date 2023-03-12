package br.eti.freitas.startproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.eti.freitas.startproject.model.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

}
