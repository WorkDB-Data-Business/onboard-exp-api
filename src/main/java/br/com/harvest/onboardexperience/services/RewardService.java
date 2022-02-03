package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.forms.RewardForm;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.mappers.ClientMapper;
import br.com.harvest.onboardexperience.domain.enumerators.FileTypeEnum;
import br.com.harvest.onboardexperience.infra.storage.services.ImageStorageService;
import br.com.harvest.onboardexperience.mappers.CoinMapper;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.harvest.onboardexperience.domain.dtos.ClientDto;
import br.com.harvest.onboardexperience.domain.dtos.RewardDto;
import br.com.harvest.onboardexperience.domain.entities.Reward;
import br.com.harvest.onboardexperience.domain.exceptions.RewardAlreadyExistsException;
import br.com.harvest.onboardexperience.domain.exceptions.RewardNotFoundException;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.RewardMapper;
import br.com.harvest.onboardexperience.repositories.RewardRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
	private ImageStorageService imageStorageService;

	@Autowired
	private ClientMapper clientMapper;

	@Autowired
	private TenantService tenantService;

	@Autowired
	private FetchService fetchService;

	@Autowired
	private UserService userService;

	
	public RewardDto create(@NonNull RewardForm form, MultipartFile file , @NonNull final String token) {
		Client client = tenantService.fetchClientByTenantFromToken(token);
		User user = userService.findUserByToken(token);

		RewardDto dto = convertFormToRewardDto(form, token);

		try {

			validate(dto, client);
			
			saveImage(file, dto, user);

			Reward reward = repository.save(RewardMapper.INSTANCE.toEntity(dto));

			log.info("The reward " + dto.getName() + " was saved successful.");
			return RewardMapper.INSTANCE.toDto(reward);
		} catch (Exception e) {
			log.error("An error has occurred when saving reward " + dto.getName() , e);
			return null;
		}
	}

	
	public RewardDto update(@NonNull Long id, @NonNull RewardForm form, MultipartFile file, @NonNull final String token) {
		Client client = tenantService.fetchClientByTenantFromToken(token);
		User user = userService.findUserByToken(token);

		Reward reward = findRewardByIdAndToken(id, token);

		Boolean needToImagePreview = Objects.nonNull(file);

		RewardDto dto = convertFormToRewardDto(form, token);

		validate(reward, dto, client);

		if(needToImagePreview){
			saveImage(file, dto, user);
		}

		BeanUtils.copyProperties(dto, reward, "id", "client", "createdAt", "createdBy",
				!needToImagePreview ? "imagePath" : "");

		reward = repository.save(reward);

		log.info("The reward " + dto.getName() + " was updated successful.");

		return RewardMapper.INSTANCE.toDto(reward);
	}

	
	public RewardDto findRewardDtoByIdAndTenant(@NonNull Long id, @NonNull final String token) {
		return RewardMapper.INSTANCE.toDto(findRewardByIdAndToken(id, token));
	}

	public Reward findRewardByIdAndToken(@NonNull Long id, @NonNull String token){
		return repository.findByIdAndClient_Tenant(id, jwtUtils.getUserTenant(token)).orElseThrow(
				() -> new RewardNotFoundException(ExceptionMessageFactory.createNotFoundMessage("reward", "ID", id.toString())));
	}

	public Page<RewardDto> findByCriteria(String criteria, final Pageable pageable, final String token) {
		String tenant = jwtUtils.getUserTenant(token);
		if(GenericUtils.stringNullOrEmpty(criteria)){
			return findAllByTenant(pageable, token);
		}
		return repository.findByCriteria(criteria, tenant, pageable).map(RewardMapper.INSTANCE::toDto);
	}

	public Page<RewardDto> findAllByTenant(Pageable pageable, @NonNull final String token) {
		String tenant = jwtUtils.getUserTenant(token);
		return repository.findAllByClient_Tenant(tenant, pageable).map(RewardMapper.INSTANCE::toDto);
	}

	public List<RewardDto> findAllByTenant(@NonNull final String token) {
		return repository.findAllByClient(tenantService.fetchClientByTenantFromToken(token)).stream()
				.map(RewardMapper.INSTANCE::toDto).collect(Collectors.toList());
	}

	
	public void delete(@NonNull Long id, @NonNull final String token) {
		repository.delete(findRewardByIdAndToken(id, token));
	}

	@Transactional
	public void disableReward(@NonNull final Long id, @NonNull final String token) {
			Reward reward = findRewardByIdAndToken(id, token);

			reward.setIsActive(!reward.getIsActive());
			repository.save(reward);

			String isEnabled = reward.getIsActive().equals(true) ? "disabled" : "enabled";
			log.info("The Reward with ID " + id + " was " + isEnabled + " successful.");
	}

	private void validate(@NonNull RewardDto reward, @NonNull final Client client) {
		checkIfRewardAlreadyExists(reward, client);
	}

	private void saveImage(MultipartFile file, RewardDto dto, User author) {
		dto.setImagePath(imageStorageService.uploadImage(file, dto.getClient().getCnpj(), dto.getName(), FileTypeEnum.REWARD, author));
	}

	private void validate(@NonNull Reward reward, @NonNull RewardDto dto, @NonNull final Client client) {
		checkIfRewardAlreadyExists(reward, dto, client);
	}

	private Boolean checkIfIsSameReward(@NonNull Reward reward, @NonNull RewardDto rewardsDto) {
		return reward.getName().equalsIgnoreCase(rewardsDto.getName());
	}

	private void checkIfRewardAlreadyExists(@NonNull RewardDto dto, @NonNull final Client client) {
		if(repository.findByNameAndClient(dto.getName(), client).isPresent()) {
			throw new RewardAlreadyExistsException(ExceptionMessageFactory.createAlreadyExistsMessage("reward", "name", dto.getName()));
		}
	}

	private void checkIfRewardAlreadyExists(@NonNull Reward Reward, @NonNull RewardDto dto, @NonNull final Client client) {
		if(!checkIfIsSameReward(Reward, dto)) {
			checkIfRewardAlreadyExists(dto, client);
		}
	}

	private RewardDto convertFormToRewardDto(@NonNull RewardForm form, @NonNull String token){

		return RewardDto.builder()
				.client(tenantService.fetchClientDtoByTenantFromToken(token))
				.description(form.getDescription())
				.isActive(form.getIsActive())
				.name(form.getName())
				.price(form.getPrice())
				.coin(CoinMapper.INSTANCE.toDto(fetchService.fetchCoin(form.getCoinId(), token)))
				.build();
	}

}
