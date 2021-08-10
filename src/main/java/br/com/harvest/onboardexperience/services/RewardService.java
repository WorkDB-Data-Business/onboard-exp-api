package br.com.harvest.onboardexperience.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.harvest.onboardexperience.domain.dto.ClientDto;
import br.com.harvest.onboardexperience.domain.dto.RewardDto;
import br.com.harvest.onboardexperience.domain.entities.Reward;
import br.com.harvest.onboardexperience.domain.exceptions.RewardAlreadyExistsException;
import br.com.harvest.onboardexperience.domain.exceptions.RewardNotFoundException;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.RewardMapper;
import br.com.harvest.onboardexperience.repositories.RewardRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RewardService {


	@Autowired
	private JwtTokenUtils jwtUtils;

	@Autowired
	private RewardRepository repository;

	@Autowired
	private ClientService clientService;
	
	@Autowired
	private FileStorageService fileStorageService;

	
	public RewardDto create(@NonNull RewardDto dto, MultipartFile file , @NonNull final String token) {
		try {
			String tenant = jwtUtils.getUserTenant(token);

			validate(dto, tenant);
			
			saveImage(file, false, dto);

			Reward reward = repository.save(RewardMapper.INSTANCE.toEntity(dto));

			log.info("The reward " + dto.getName() + " was saved successful.");
			return RewardMapper.INSTANCE.toDto(reward);
		} catch (Exception e) {
			log.error("An error has occurred when saving reward " + dto.getName() , e);
			return null;
		}
	}

	
	public RewardDto update(@NonNull Long id, @NonNull RewardDto dto, MultipartFile file, @NonNull final String token) {
		try {

			String tenant = jwtUtils.getUserTenant(token);

			Reward reward = repository.findByIdAndTenant(id, tenant).orElseThrow(
					() -> new RewardNotFoundException(ExceptionMessageFactory.createNotFoundMessage("reward", "ID", id.toString())));

			validate(reward, dto, tenant);
			saveImage(file, true, dto);

			BeanUtils.copyProperties(dto, reward, "id", "client", "createdAt", "createdBy");

			reward = repository.save(reward);

			log.info("The reward " + dto.getName() + " was updated successful.");

			return RewardMapper.INSTANCE.toDto(reward);
		} catch (Exception e) {
			log.error("An error has occurred when updating reward with ID " + id, e);
			return null;
		}
	}

	
	public RewardDto findByIdAndTenant(@NonNull Long id, @NonNull final String token) {
		String tenant = jwtUtils.getUserTenant(token);

		Reward reward = repository.findByIdAndTenant(id, tenant).orElseThrow(
				() -> new RewardNotFoundException(ExceptionMessageFactory.createNotFoundMessage("reward", "ID", id.toString())));

		return RewardMapper.INSTANCE.toDto(reward);
	}

	
	public Page<RewardDto> findAllByTenant(Pageable pageable, @NonNull final String token) {
		String tenant = jwtUtils.getUserTenant(token);
		List<RewardDto> rewards = repository.findAllByTenant(tenant).stream().map(RewardMapper.INSTANCE::toDto).collect(Collectors.toList());
		return new PageImpl<>(rewards, pageable, rewards.size());
	}

	
	public void delete(@NonNull Long id, @NonNull final String token) {
		String tenant = jwtUtils.getUserTenant(token);

		Reward reward = repository.findByIdAndTenant(id, tenant).orElseThrow(
				() -> new RewardNotFoundException(ExceptionMessageFactory.createNotFoundMessage("reward", "ID", id.toString())));

		repository.delete(reward);
	}

	@Transactional
	public void disableReward(@NonNull final Long id, @NonNull final String token) {
		String tenant = jwtUtils.getUserTenant(token);
		try {
			Reward reward = repository.findByIdAndTenant(id, tenant).orElseThrow(
					() -> new RewardNotFoundException(ExceptionMessageFactory.createNotFoundMessage("reward", "ID", id.toString())));

			reward.setIsActive(!reward.getIsActive());
			repository.save(reward);

			String isEnabled = reward.getIsActive().equals(true) ? "disabled" : "enabled";
			log.info("The Reward with ID " + id + " was " + isEnabled + " successful.");
		} catch (Exception e) {
			log.error("An error has occurred when disabling or enabling reward with ID " + id, e);
		}
	}
	
	private void saveImage(MultipartFile file, Boolean overwrite, RewardDto dto) {
		String filePath = fileStorageService.save(file, dto.getName(), dto.getClient().getCnpj());
		dto.setImagePath(filePath);
	}

	private void validate(@NonNull RewardDto reward, @NonNull final String tenant) {
		checkIfRewardAlreadyExists(reward, tenant);
		fetchAndSetClient(reward, tenant);
	}

	private void validate(@NonNull Reward reward, @NonNull RewardDto dto, @NonNull final String tenant) {
		checkIfRewardAlreadyExists(reward, dto, tenant);
	}

	private Boolean checkIfIsSameReward(@NonNull Reward reward, @NonNull RewardDto rewardsDto) {
		Boolean sameName = reward.getName().equalsIgnoreCase(rewardsDto.getName());

		if(sameName) {
			return true;
		}
		return false;
	}

	private void fetchAndSetClient(@NonNull RewardDto dto, String tenant) {
		ClientDto client = clientService.findByTenant(tenant);
		dto.setClient(client);
	}

	private void checkIfRewardAlreadyExists(@NonNull RewardDto dto, @NonNull final String tenant) {
		if(repository.findByNameContainingIgnoreCaseAndTenant(dto.getName(), tenant).isPresent()) {
			throw new RewardAlreadyExistsException(ExceptionMessageFactory.createAlreadyExistsMessage("reward", "name", dto.getName()));
		}
	}

	private void checkIfRewardAlreadyExists(@NonNull Reward Reward, @NonNull RewardDto dto, @NonNull final String tenant) {
		if(!checkIfIsSameReward(Reward, dto)) {
			checkIfRewardAlreadyExists(dto, tenant);
		}
	}
}
