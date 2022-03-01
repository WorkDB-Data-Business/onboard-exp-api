package br.com.harvest.onboardexperience.domain.dtos;

import lombok.*;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserCoinDto {

    private Long id;

    private String coinName;

    private BigInteger amount;

}
