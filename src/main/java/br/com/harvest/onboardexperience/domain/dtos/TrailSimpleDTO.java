package br.com.harvest.onboardexperience.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TrailSimpleDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("imagePreviewEncoded")
    private String imagePreviewEncoded;

    @JsonProperty("description")
    private String description;

    @JsonProperty("conclusionDate")
    private LocalDateTime conclusionDate;

    @JsonProperty("isActive")
    @Builder.Default
    private Boolean isActive = true;

    @JsonProperty("coin")
    private CoinSimpleDTO coin;

}
