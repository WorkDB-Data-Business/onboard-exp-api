package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.exceptions.BusinessException;
import br.com.harvest.onboardexperience.mappers.ClientMapper;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.harvest.onboardexperience.domain.dtos.ClientDto;
import br.com.harvest.onboardexperience.domain.dtos.CoinDto;
import br.com.harvest.onboardexperience.domain.entities.Coin;
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
    private ClientMapper clientMapper;

//    @Autowired
//    private FileStorageService fileStorageService;


    public CoinDto create(@NonNull CoinDto dto, MultipartFile file, String token) {
        String tenant = jwtUtils.getUserTenant(token);

        validate(dto, tenant);
//        saveImage(file, dto);

        Coin coin = repository.save(CoinMapper.INSTANCE.toEntity(dto));

        log.info("The coin " + dto.getName() + " was saved successful.");
        return CoinMapper.INSTANCE.toDto(coin);
    }


    public CoinDto update(@NonNull Long id, @NonNull CoinDto dto, MultipartFile file, @NonNull String token) {
        String tenant = jwtUtils.getUserTenant(token);

        Coin coin = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new CoinNotFoundException(ExceptionMessageFactory.createNotFoundMessage("coin", "ID", id.toString())));

        dto.setClient(clientMapper.toDto(coin.getClient()));
        dto.setImagePath(coin.getImagePath());

        validate(coin, dto, tenant);

//        saveImage(file, dto);

        BeanUtils.copyProperties(dto, coin, "id", "client", "createdAt", "createdBy", dto.getImagePath() == null ? "imagePath" : "");

        coin = repository.save(coin);

        log.info("The coin " + dto.getName() + " was updated successful.");

        return CoinMapper.INSTANCE.toDto(coin);
    }


    public CoinDto findByIdAndTenant(@NonNull Long id, @NonNull String token) {
        String tenant = jwtUtils.getUserTenant(token);

        CoinDto coin = CoinMapper.INSTANCE.toDto(repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new CoinNotFoundException(ExceptionMessageFactory.createNotFoundMessage("coin", "ID", id.toString()))));

        return coin;
    }

    public Page<CoinDto> findByCriteria(String criteria, Pageable pageable, String token) {
        String tenant = jwtUtils.getUserTenant(token);
        if(GenericUtils.stringNullOrEmpty(criteria)){
            return findAllByTenant(pageable, token);
        }
        return repository.findByCriteria(criteria, tenant, pageable).map(CoinMapper.INSTANCE::toDto);
    }


    public Page<CoinDto> findAllByTenant(Pageable pageable, @NonNull String token) {
        String tenant = jwtUtils.getUserTenant(token);
        return repository.findAllByClient_Tenant(tenant, pageable).map(CoinMapper.INSTANCE::toDto);
    }


    public void delete(@NonNull Long id, @NonNull String token) {
        String tenant = jwtUtils.getUserTenant(token);

        Coin coin = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new CoinNotFoundException(ExceptionMessageFactory.createNotFoundMessage("coin", "ID", id.toString())));

        repository.delete(coin);
    }

    @Transactional
    public void disableCoin(@NonNull final Long id, @NonNull final String token) {
        String tenant = jwtUtils.getUserTenant(token);
            Coin coin = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                    () -> new CoinNotFoundException(ExceptionMessageFactory.createNotFoundMessage("coin", "ID", id.toString())));

            coin.setIsActive(!coin.getIsActive());
            repository.save(coin);

            String isEnabled = coin.getIsActive().equals(true) ? "disabled" : "enabled";
            log.info("The coin with ID " + id + " was " + isEnabled + " successful.");
    }

//    private void saveImage(MultipartFile file, CoinDto dto) {
//		String filePath = "";
//    	if(file != null){
//			filePath = fileStorageService.save(file, dto.getName(), new String[]{dto.getClient().getCnpj(), FileTypeEnum.COIN.getName()});
//		}else{
//			filePath = fileStorageService.rename(dto.getName(), dto.getImagePath());
//		}
//
//        dto.setImagePath(filePath);
//    }

    private Boolean checkIfIsSameCoin(@NonNull Coin coin, @NonNull CoinDto coinDto) {
        Boolean sameName = coin.getName().equalsIgnoreCase(coinDto.getName());

        if (sameName) {
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
        if (repository.findByNameContainingIgnoreCaseAndClient_Tenant(dto.getName(), tenant).isPresent()) {
            throw new BusinessException(ExceptionMessageFactory.createAlreadyExistsMessage("coin", "name", dto.getName()));
        }
    }

    private void checkIfCoinAlreadyExists(@NonNull Coin coin, @NonNull CoinDto dto, @NonNull final String tenant) {
        if (!checkIfIsSameCoin(coin, dto)) {
            checkIfCoinAlreadyExists(dto, tenant);
        }
    }

}
