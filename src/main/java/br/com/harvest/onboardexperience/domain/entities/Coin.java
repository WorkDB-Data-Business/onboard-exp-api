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
@Table(name = "tbcoin", schema="public")
@SQLDelete(sql = SQLQueryUtils.SOFT_DELETE_COIN, check = ResultCheckStyle.COUNT)
@Where(clause = SQLQueryUtils.IS_ACTIVE_FILTER)
public class Coin extends BaseEntityAudit {

	private static final long serialVersionUID = -1137244643968971239L;
	
	@Id
	@Column(name = "idcoin")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "image_path")
	private String imagePath;
	
	@Column(name = "name")
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "idclient")
	private Client client;
	
	@Column(name = "is_active")
	private Boolean isActive;

}
