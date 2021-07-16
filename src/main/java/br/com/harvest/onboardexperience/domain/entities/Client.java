package br.com.harvest.onboardexperience.domain.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbclient")
public class Client extends BaseEntityAudit {
	
	private static final long serialVersionUID = -5534785717060896603L;

	@Id
	@Column(name = "idclient")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "cnpj")
	private String cnpj;
	
	@Column(name = "name")
	private String name;
	
	@OneToMany(mappedBy="client")
	private List<User> users;
	
	@Column(name = "is_active")
	private Boolean isActive;
	
	@Column(name = "is_expired")
	private Boolean isExpired;
	
	@Column(name = "is_blocked")
	private Boolean isBlocked;
	
	@Column(name = "tenant")
	private String tenant;
	
	@Column(name = "is_master")
	private Boolean isMaster;
	
}
