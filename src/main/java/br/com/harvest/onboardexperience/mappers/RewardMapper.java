package br.com.harvest.onboardexperience.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.harvest.onboardexperience.domain.dtos.RewardDto;
import br.com.harvest.onboardexperience.domain.entities.Reward;

@Mapper(componentModel="spring")
public interface RewardMapper extends AbstractMapper<Reward, RewardDto>{
	
	RewardMapper INSTANCE = Mappers.getMapper(RewardMapper.class);

}
