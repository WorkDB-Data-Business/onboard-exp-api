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
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.CompanyRoleMapper;
import br.com.harvest.onboardexperience.repositories.CompanyRoleRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;

@Service
public class CompanyRoleService {

	@Autowired
	private CompanyRoleMapper mapper;
	
	@Autowired
	private CompanyRoleRepository repository;
	
	@Autowired
	private JwtTokenUtils jwtUtils;
	
	
	public CompanyRoleDto create(@NonNull CompanyRoleDto dto) {
		CompanyRole companyRole = repository.save(mapper.toEntity(dto));
		return mapper.toDto(companyRole);
	}

	
	public CompanyRoleDto update(@NonNull final Long id, @NonNull CompanyRoleDto dto) {
		CompanyRole companyRole = repository.findById(id).orElseThrow(() -> new CompanyRoleNotFoundException(ExceptionMessageFactory.createNotFoundMessage("company role", "ID", id.toString())));
		
		BeanUtils.copyProperties(dto, companyRole, "id");
		
		companyRole = repository.save(companyRole);
		
		return mapper.toDto(companyRole);
	}

	
	public CompanyRoleDto findById(@NonNull final Long id) {
		CompanyRole companyRole = repository.findById(id).orElseThrow(() -> new CompanyRoleNotFoundException(ExceptionMessageFactory.createNotFoundMessage("company role", "ID", id.toString())));
		
		return mapper.toDto(companyRole);
	}
	
	public CompanyRoleDto findByIdOrName(Long id, String name, String token) {
		String tenant = jwtUtils.getUsernameTenant(token);
		
		//TODO: finish the query by tenant
		CompanyRole companyRole = repository.findByIdOrNameContainingIgnoreCase(id, name)
				.orElseThrow(() -> new CompanyRoleNotFoundException("Company Role with ID " + id + " or name " 
						+ name + " not found."));

		return mapper.toDto(companyRole);
	}

	
	public Page<CompanyRoleDto> findAllByTenant(Pageable pageable, String token) {
		String tenant = jwtUtils.getUsernameTenant(token);
		List<CompanyRoleDto> companyRoles = repository.findAllByTenant(tenant).stream().map(mapper::toDto).collect(Collectors.toList());
		return new PageImpl<>(companyRoles, pageable, companyRoles.size());
	}

	
	public void delete(@NonNull final Long id, String token) {
		String tenant = jwtUtils.getUsernameTenant(token);
		CompanyRole companyRole = repository.findByIdAndTenant(id, tenant).orElseThrow(
				() -> new CompanyRoleNotFoundException("The company role with ID " + id + " has not found"));
		
		repository.delete(companyRole);
	}

}
