package br.com.harvest.onboardexperience.domain.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtRequest implements Serializable {

	private static final long serialVersionUID = 5464883517930904082L;
	
	private String email;
	private String password;
	private Boolean rememberMe;

}
