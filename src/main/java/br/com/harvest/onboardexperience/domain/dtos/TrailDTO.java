package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.domain.dtos.forms.PositionForm;
import br.com.harvest.onboardexperience.domain.entities.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TrailDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("mapImagePath")
    private String mapImagePath;

    @JsonProperty("mapMusicPath")
    private String mapMusicPath;

    @JsonProperty("conclusionDate")
    private LocalDateTime conclusionDate;

    @JsonProperty("author")
    private UserSimpleDto author;

    @JsonProperty("isActive")
    @Builder.Default
    private Boolean isActive = true;

    @JsonProperty("coin")
    private CoinSimpleDTO coin;

    @JsonProperty("characterMapPositionPath")
    private List<PositionForm> characterMapPositionPath;

    @JsonProperty("groups")
    private List<GroupDto> groups;

}
