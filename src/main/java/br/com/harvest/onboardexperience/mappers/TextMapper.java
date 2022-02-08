package br.com.harvest.onboardexperience.mappers;

import br.com.harvest.onboardexperience.domain.dtos.TextDto;
import br.com.harvest.onboardexperience.domain.dtos.forms.PositionDTO;
import br.com.harvest.onboardexperience.domain.entities.Position;
import br.com.harvest.onboardexperience.domain.entities.Text;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TextMapper extends AbstractMapper<Text, TextDto> {

    TextMapper INSTANCE = Mappers.getMapper(TextMapper.class);

}
