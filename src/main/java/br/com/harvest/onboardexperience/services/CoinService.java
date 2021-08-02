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
import br.com.harvest.onboardexperience.domain.dto.CoinDto;
import br.com.harvest.onboardexperience.domain.entities.Coin;
import br.com.harvest.onboardexperience.domain.exceptions.CoinAlreadyExistsException;
import br.com.harvest.onboardexperience.domain.exceptions.CoinNotFoundException;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.CoinMapper;
import br.com.harvest.onboardexperience.repositories.CoinRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CoinService {

	@Autowired
	private JwtTokenUtils jwtUtils;

	@Autowired
	private CoinRepository repository;

	@Autowired
	private ClientService clientService;
	
	@Autowired
	private FileStorageService fileStorageService;

	
	public CoinDto create(@NonNull CoinDto dto, MultipartFile file, String token) {
		try {
			String tenant = jwtUtils.getUserTenant(token);

			validate(dto, tenant);
			saveImage(file, dto);

			Coin coin = repository.save(CoinMapper.INSTANCE.toEntity(dto));

			log.info("The coin " + dto.getName() + " was saved successful.");
			return CoinMapper.INSTANCE.toDto(coin);
		} catch (Exception e) {
			log.error("An error has occurred when saving coin " + dto.getName() , e);
			return null;
		}
	}

	
	public CoinDto update(@NonNull Long id, @NonNull CoinDto dto, MultipartFile file, @NonNull String token) {
		try {

			String tenant = jwtUtils.getUserTenant(token);

			Coin coin = repository.findByIdAndTenant(id, tenant).orElseThrow(
					() -> new CoinNotFoundException(ExceptionMessageFactory.createNotFoundMessage("coin", "ID", id.toString())));

			validate(coin, dto, tenant);
			saveImage(file, dto);

			BeanUtils.copyProperties(dto, coin, "id", "client", "createdAt", "createdBy");

			coin = repository.save(coin);

			log.info("The coin " + dto.getName() + " was updated successful.");

			return CoinMapper.INSTANCE.toDto(coin);
		} catch (Exception e) {
			log.error("An error has occurred when updating coin with ID " + id, e);
			return null;
		}
	}

	
	public CoinDto findByIdAndTenant(@NonNull Long id, @NonNull String token) {
		String tenant = jwtUtils.getUserTenant(token);
		
		CoinDto coin = CoinMapper.INSTANCE.toDto(repository.findByIdAndTenant(id, tenant).orElseThrow(
				() -> new CoinNotFoundException(ExceptionMessageFactory.createNotFoundMessage("coin", "ID", id.toString()))));
		
		return coin;
	}

	
	public Page<CoinDto> findAllByTenant(Pageable pageable, @NonNull String token) {
		String tenant = jwtUtils.getUserTenant(token);
		List<CoinDto> coins = repository.findAllByTenant(tenant).stream().map(CoinMapper.INSTANCE::toDto).collect(Collectors.toList());
		return new PageImpl<>(coins, pageable, coins.size());
	}

	
	public void delete(@NonNull Long id, @NonNull String token) {
		String tenant = jwtUtils.getUserTenant(token);
		
		Coin coin = repository.findByIdAndTenant(id, tenant).orElseThrow(
				() -> new CoinNotFoundException(ExceptionMessageFactory.createNotFoundMessage("coin", "ID", id.toString())));
		
		repository.delete(coin);
	}
	
	@Transactional
	public void disableCoin(@NonNull final Long id, @NonNull final String token) {
		String tenant = jwtUtils.getUserTenant(token);
		try {
			Coin coin = repository.findByIdAndTenant(id, tenant).orElseThrow(
					() -> new CoinNotFoundException(ExceptionMessageFactory.createNotFoundMessage("coin", "ID", id.toString())));
			
			coin.setIsActive(!coin.getIsActive());
			repository.save(coin);
			
			String isEnabled = coin.getIsActive().equals(true) ? "disabled" : "enabled";
			log.info("The coin with ID " + id + " was " + isEnabled + " successful.");
		} catch (Exception e) {
			log.error("An error has occurred when disabling or enabling coin with ID " + id, e);
		}
	}
	
	private void saveImage(MultipartFile file, CoinDto dto) {
		String filePath = fileStorageService.save(file, dto.getClient().getCnpj());
		dto.setImagePath(filePath);
	}
	
	private Boolean checkIfIsSameCoin(@NonNull Coin coin, @NonNull CoinDto coinDto) {
		Boolean sameName = coin.getName().equalsIgnoreCase(coinDto.getName());
		
		if(sameName) {
			return true;
		}
		return false;
	}

	private void validate(@NonNull CoinDto coin, @NonNull final String tenant) {
		checkIfCoinAlreadyExists(coin, tenant);
		fetchAndSetClient(coin, tenant);
	}
	
	private void validate(@NonNull Coin coin, @NonNull CoinDto dto, @NonNull final String tenant) {
		checkIfCoinAlreadyExists(coin, dto, tenant);
	}

	private void fetchAndSetClient(@NonNull CoinDto dto, String tenant) {
		ClientDto client = clientService.findByTenant(tenant);
		dto.setClient(client);
	}
	
	private void checkIfCoinAlreadyExists(@NonNull CoinDto dto, @NonNull final String tenant) {
		if(repository.findByNameContainingIgnoreCaseAndTenant(dto.getName(), tenant).isPresent()) {
			throw new CoinAlreadyExistsException(ExceptionMessageFactory.createAlreadyExistsMessage("coin", "name", dto.getName()));
		}
	}
	
	private void checkIfCoinAlreadyExists(@NonNull Coin coin, @NonNull CoinDto dto, @NonNull final String tenant) {
		if(!checkIfIsSameCoin(coin, dto)) {
			checkIfCoinAlreadyExists(dto, tenant);
		}
	}
	
}
