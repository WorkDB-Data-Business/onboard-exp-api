package br.com.harvest.onboardexperience.domain.entities.listeners;

import br.com.harvest.onboardexperience.domain.entities.Position;

import javax.persistence.PrePersist;
import java.math.RoundingMode;
import java.util.Objects;

public class PositionListener {

    @PrePersist
    public void adjustScale(Position position){
        if(Objects.nonNull(position.getXAxis()) && Objects.nonNull(position.getYAxis())){
            position.setXAxis(position.getXAxis().setScale(4, RoundingMode.HALF_UP));
            position.setYAxis(position.getYAxis().setScale(4, RoundingMode.HALF_UP));
        }
    }
}
