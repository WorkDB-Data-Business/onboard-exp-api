package br.com.harvest.onboardexperience.mappers;

import br.com.harvest.onboardexperience.domain.dtos.TrilhaDTO;
import br.com.harvest.onboardexperience.domain.entities.Trilha;

import java.util.Set;
import java.util.stream.Collectors;

public class TrilhaMapper {

    public static Trilha toEntity(TrilhaDTO dto){
        return Trilha
                .builder()
                .id(dto.getId())
                .arquivoTrilhaBytes(dto.getArquivoTrilhaBytes())
                .arquivoTrilhaNome(dto.getArquivoTrilhaNome())
                .nomeTrilha(dto.getNomeTrilha())
                .descricaoTrilha(dto.getDescricaoTrilha())
                .build();
    }

    public static Set<Trilha> toEntity(Set<TrilhaDTO> dtos){
        return dtos.stream().map(TrilhaMapper::toEntity).collect(Collectors.toSet());
    }

    public static TrilhaDTO toDto(Trilha entity){
        return TrilhaDTO
                .builder()
                .id(entity.getId())
                .arquivoTrilhaBytes(entity.getArquivoTrilhaBytes())
                .arquivoTrilhaNome(entity.getArquivoTrilhaNome())
                .descricaoTrilha(entity.getDescricaoTrilha())
                .nomeTrilha(entity.getNomeTrilha())
                .build();
    }

    public static Set<TrilhaDTO> toDto(Set<Trilha> entites){
        return entites.stream().map(TrilhaMapper::toDto).collect(Collectors.toSet());
    }

}
