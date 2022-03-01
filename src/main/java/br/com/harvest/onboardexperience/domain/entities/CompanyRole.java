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
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbcompany_role", schema="public")
@SQLDelete(sql = SQLQueryUtils.SOFT_DELETE_COMPANY_ROLE, check = ResultCheckStyle.COUNT)
@Where(clause = SQLQueryUtils.IS_ACTIVE_FILTER)
public class CompanyRole extends BaseEntityAudit {
	
	private static final long serialVersionUID = 365761000832319330L;

	@Id
	@Column(name = "idcompany_role")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "idclient")
	private Client client;
	
	@Column(name = "is_active")
	private Boolean isActive;
	
}
