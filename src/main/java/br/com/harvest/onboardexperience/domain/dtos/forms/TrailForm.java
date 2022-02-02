package br.com.harvest.onboardexperience.domain.dtos.forms;

import br.com.harvest.onboardexperience.utils.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrailForm {

    @NotBlank
    @JsonProperty("name")
    private String name;

    @NotBlank
    @JsonProperty("description")
    private String description;

    @DateTimeFormat(pattern = Constants.Date.DATE_PATTERN)
    @JsonProperty("conclusionDate")
    private LocalDateTime conclusionDate;

    @JsonSetter(nulls = Nulls.SKIP)
    @JsonProperty("isActive")
    private Boolean isActive = true;

    @NotNull
    @JsonProperty("coinId")
    private Long coinId;

    @NotEmpty
    @JsonProperty("groupsId")
    private List<Long> groupsId;

}
