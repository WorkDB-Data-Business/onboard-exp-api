package br.com.harvest.onboardexperience.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
@Entity(name = "tbcoin")
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

}
