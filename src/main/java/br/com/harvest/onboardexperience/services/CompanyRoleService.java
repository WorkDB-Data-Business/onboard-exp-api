package br.com.harvest.onboardexperience.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.harvest.onboardexperience.domain.dto.CompanyRoleDto;
import br.com.harvest.onboardexperience.domain.entities.CompanyRole;
import br.com.harvest.onboardexperience.domain.exceptions.CompanyRoleNotFoundException;
import br.com.harvest.onboardexperience.mappers.CompanyRoleMapper;
import br.com.harvest.onboardexperience.repositories.CompanyRoleRepository;

@Service
public class CompanyRoleService implements IService<CompanyRoleDto>{

	@Autowired
	private CompanyRoleMapper mapper;
	
	@Autowired
	private CompanyRoleRepository repository;
	
	@Override
	public CompanyRoleDto create(CompanyRoleDto dto) {
		CompanyRole companyRole = repository.save(mapper.toEntity(dto));
		return mapper.toDto(companyRole);
	}

	@Override
	public CompanyRoleDto update(Long id, CompanyRoleDto dto) {
		CompanyRole companyRole = repository.findById(id).orElseThrow(() -> new CompanyRoleNotFoundException("The company role with ID " + id + " has not found"));
		
		BeanUtils.copyProperties(dto, companyRole, "id");
		
		companyRole = repository.save(companyRole);
		
		return mapper.toDto(companyRole);
	}

	@Override
	public CompanyRoleDto findById(Long id) {
		CompanyRole companyRole = repository.findById(id).orElseThrow(() -> new CompanyRoleNotFoundException("The company role with ID " + id + " has not found"));
		
		return mapper.toDto(companyRole);
	}
	
	public CompanyRoleDto findByIdOrName(Long id, String name) {

		CompanyRole companyRole = repository.findByIdOrNameContainingIgnoreCase(id, name)
				.orElseThrow(() -> new CompanyRoleNotFoundException("Company Role with ID " + id + " or name " 
						+ name + " not found."));

		return mapper.toDto(companyRole);
	}

	@Override
	public Page<CompanyRoleDto> findAll(Pageable pageable) {
		List<CompanyRoleDto> companyRoles = repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
		return new PageImpl<>(companyRoles, pageable, companyRoles.size());
	}

	@Override
	public void delete(Long id) {
		CompanyRole companyRole = repository.findById(id).orElseThrow(
				() -> new CompanyRoleNotFoundException("The company role with ID " + id + " has not found"));
		
		repository.delete(companyRole);
	}

}
