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
	private CompanyRoleRepository repository;
	
	@Autowired
	private JwtTokenUtils jwtUtils;
	
	@Autowired
	private TenantService tenantService;
	
	
	public CompanyRoleDto create(@NonNull CompanyRoleDto dto, @NonNull final String token) {
		
		dto.setClient(tenantService.fetchClientDtoByTenantFromToken(token));
		
		CompanyRole companyRole = repository.save(CompanyRoleMapper.INSTANCE.toEntity(dto));
		return CompanyRoleMapper.INSTANCE.toDto(companyRole);
	}

	
	public CompanyRoleDto update(@NonNull final Long id, @NonNull CompanyRoleDto dto, @NonNull final String token) {
		
		String tenant = jwtUtils.getUserTenant(token);
		
		CompanyRole companyRole = repository.findByIdAndTenant(id, tenant).orElseThrow(
				() -> new CompanyRoleNotFoundException(ExceptionMessageFactory.createNotFoundMessage("company role", "ID", id.toString())));
		
		BeanUtils.copyProperties(dto, companyRole, "id", "client", "createdBy", "createdAt");
		
		companyRole = repository.save(companyRole);
		
		return CompanyRoleMapper.INSTANCE.toDto(companyRole);
	}

	
	public CompanyRoleDto findByIdAndTenant(@NonNull final Long id, @NonNull final String token) {
		String tenant = jwtUtils.getUserTenant(token);
		
		CompanyRole companyRole = repository.findByIdAndTenant(id, tenant).orElseThrow(() -> new CompanyRoleNotFoundException(ExceptionMessageFactory.createNotFoundMessage("company role", "ID", id.toString())));
		
		return CompanyRoleMapper.INSTANCE.toDto(companyRole);
	}
	
	public CompanyRoleDto findByIdOrNameAndTenant(String name, String tenant) {
		
		CompanyRole companyRole = repository.findByNameContainingIgnoreCaseAndTenant(name, tenant)
				.orElseThrow(() -> new CompanyRoleNotFoundException("Company Role with name " + name + " not found."));

		return CompanyRoleMapper.INSTANCE.toDto(companyRole);
	}

	
	public Page<CompanyRoleDto> findAllByTenant(Pageable pageable, @NonNull final String token) {
		String tenant = jwtUtils.getUserTenant(token);
		List<CompanyRoleDto> companyRoles = repository.findAllByTenant(tenant).stream().map(CompanyRoleMapper.INSTANCE::toDto).collect(Collectors.toList());
		return new PageImpl<>(companyRoles, pageable, companyRoles.size());
	}

	
	public void delete(@NonNull final Long id, @NonNull final String token) {
		String tenant = jwtUtils.getUserTenant(token);
		CompanyRole companyRole = repository.findByIdAndTenant(id, tenant).orElseThrow(
				() -> new CompanyRoleNotFoundException("The company role with ID " + id + " has not found"));
		
		repository.delete(companyRole);
	}

}
