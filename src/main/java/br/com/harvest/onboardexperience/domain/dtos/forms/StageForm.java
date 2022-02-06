package br.com.harvest.onboardexperience.domain.dtos.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StageForm {

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("amountCoins")
    private BigInteger amountCoins;

    @JsonProperty("minimumScore")
    private BigDecimal minimumScore;

    @JsonProperty("trailId")
    private Long trailId;

    @JsonProperty("isPreRequisite")
    private Boolean isPreRequisite;

    @JsonProperty("position")
    private PositionDTO position;

    @JsonProperty("scorms")
    private List<String> scorms;

    @JsonProperty("files")
    private List<Long> files;

    @JsonProperty("links")
    private List<Long> links;

}
