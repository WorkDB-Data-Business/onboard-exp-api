package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.forms.PositionForm;
import br.com.harvest.onboardexperience.domain.entities.Position;
import br.com.harvest.onboardexperience.domain.entities.PositionId;
import br.com.harvest.onboardexperience.mappers.PositionMapper;
import br.com.harvest.onboardexperience.repositories.PositionRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionService {

    @Autowired
    private PositionRepository repository;

    public Position getPosition(@NonNull PositionForm positionForm){
        PositionId key = PositionId.builder()
                .xAxis(positionForm.getXAxis())
                .yAxis(positionForm.getYAxis())
                .build();

        return repository.findById(key).orElseGet(() -> PositionMapper.INSTANCE.toEntity(positionForm));
    }

    public List<Position> getPosition(@NonNull List<PositionForm> positionsForm){
        return positionsForm.stream().map(this::getPosition).collect(Collectors.toList());
    }

}
