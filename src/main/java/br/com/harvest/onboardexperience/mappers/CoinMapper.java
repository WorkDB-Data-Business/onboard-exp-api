package br.com.harvest.onboardexperience.mappers;

import br.com.harvest.onboardexperience.domain.dtos.CoinDto;
import br.com.harvest.onboardexperience.domain.dtos.CoinSimpleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.harvest.onboardexperience.domain.entities.Coin;

@Mapper(componentModel="spring")
public interface CoinMapper extends AbstractMapper<Coin, CoinDto>{

	CoinMapper INSTANCE = Mappers.getMapper(CoinMapper.class);

	CoinSimpleDTO toCoinSimpleDTO(Coin coin);
	
}
