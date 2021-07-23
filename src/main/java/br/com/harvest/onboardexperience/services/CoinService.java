package br.com.harvest.onboardexperience.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.harvest.onboardexperience.domain.dto.ClientDto;
import br.com.harvest.onboardexperience.domain.dto.CoinDto;
import br.com.harvest.onboardexperience.domain.entities.Coin;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.exceptions.CoinAlreadyExistsException;
import br.com.harvest.onboardexperience.domain.exceptions.CoinNotFoundException;
import br.com.harvest.onboardexperience.domain.exceptions.UserNotFoundException;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.CoinMapper;
import br.com.harvest.onboardexperience.repositories.CoinRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CoinService implements IService<CoinDto>{

	@Autowired
	private JwtTokenUtils jwtUtils;

	@Autowired
	private CoinRepository repository;

	@Autowired
	private ClientService clientService;

	@Override
	public CoinDto create(@NonNull CoinDto dto, String token) {
		try {
			String tenant = jwtUtils.getUsernameTenant(token);

			validate(dto, tenant);

			Coin coin = repository.save(CoinMapper.INSTANCE.toEntity(dto));

			log.info("The coin " + dto.getName() + " was saved successful.");
			return CoinMapper.INSTANCE.toDto(coin);
		} catch (Exception e) {
			log.error("An error has occurred when saving coin " + dto.getName() , e);
			return null;
		}
	}

	@Override
	public CoinDto update(@NonNull Long id, @NonNull CoinDto dto, @NonNull String token) {
		try {

			String tenant = jwtUtils.getUsernameTenant(token);

			Coin coin = repository.findByIdAndTenant(id, tenant).orElseThrow(
					() -> new CoinNotFoundException(ExceptionMessageFactory.createNotFoundMessage("coin", "ID", id.toString())));


			validate(dto);

			BeanUtils.copyProperties(dto, coin, "id", "client", "createdAt", "createdBy");

			coin = repository.save(coin);

			log.info("The coin " + dto.getName() + " was updated successful.");

			return CoinMapper.INSTANCE.toDto(coin);
		} catch (Exception e) {
			log.error("An error has occurred when updating user with ID " + id, e);
			return null;
		}
	}

	@Override
	public CoinDto findByIdAndTenant(@NonNull Long id, @NonNull String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<CoinDto> findAllByTenant(Pageable pageable, @NonNull String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(@NonNull Long id, @NonNull String token) {
		// TODO Auto-generated method stub

	}
	
	private Boolean checkIfIsSameCoin(@NonNull Coin coin, @NonNull CoinDto coinDto) {
		Boolean sameName = coin.getName().equals(coinDto.getName());
		
		if(sameName) {
			return true;
		}
		return false;
	}

	private void validate(@NonNull CoinDto coin, @NonNull final String tenant) {
		checkIfCoinAlreadyExists(coin, tenant);
		fetchAndSetClient(coin, tenant);
	}
	
	private void validate(@NonNull Coin coin, @NonNull CoinDto dto) {
		checkIfCoinAlreadyExists(coin, dto);
		
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
