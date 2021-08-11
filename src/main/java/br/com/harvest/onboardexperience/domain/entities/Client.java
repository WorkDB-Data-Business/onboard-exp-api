package br.com.harvest.onboardexperience.domain.entities;

import javax.persistence.*;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.harvest.onboardexperience.utils.SQLQueryUtils;
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
@Entity
@Table(name = "tbclient", schema="public")
@SQLDelete(sql = SQLQueryUtils.SOFT_DELETE_CLIENT, check = ResultCheckStyle.COUNT)
@Where(clause = SQLQueryUtils.IS_ACTIVE_FILTER)
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
	
	@Column(name = "email")
	private String email;
	
}
