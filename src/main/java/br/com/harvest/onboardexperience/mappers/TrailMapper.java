package br.com.harvest.onboardexperience.mappers;

import br.com.harvest.onboardexperience.domain.dtos.TrailDTO;
import br.com.harvest.onboardexperience.domain.entities.Trail;

import java.util.Set;
import java.util.stream.Collectors;

public class TrailMapper {

    public static Trail toEntity(TrailDTO dto){
        return Trail
                .builder()
                .id(dto.getId())
                .arquivoTrilhaBytes(dto.getArquivoTrilhaBytes())
                .arquivoTrilhaNome(dto.getArquivoTrilhaNome())
                .nameTrail(dto.getNameTRail())
                .descriptionTrail(dto.getDescriotionTrail())
                .build();
    }

    public static Set<Trail> toEntity(Set<TrailDTO> dtos){
        return dtos.stream().map(TrailMapper::toEntity).collect(Collectors.toSet());
    }

    public static TrailDTO toDto(Trail entity){
        return TrailDTO
                .builder()
                .id(entity.getId())
                .arquivoTrilhaBytes(entity.getArquivoTrilhaBytes())
                .arquivoTrilhaNome(entity.getArquivoTrilhaNome())
                .descriotionTrail(entity.getDescriptionTrail())
                .nameTRail(entity.getNameTrail())
                .build();
    }

    public static Set<TrailDTO> toDto(Set<Trail> entites){
        return entites.stream().map(TrailMapper::toDto).collect(Collectors.toSet());
    }

}
