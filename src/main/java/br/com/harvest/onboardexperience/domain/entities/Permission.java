package br.com.harvest.onboardexperience.domain.entities;

import javax.persistence.*;

import br.com.harvest.onboardexperience.domain.enumerators.PermissionEnum;
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
@Table(name = "tbpermission", schema="public")
public class Permission extends BaseEntity {

	private static final long serialVersionUID = -7823902513795684090L;

	@Id
	@Column(name = "idpermission")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "permission")
	private PermissionEnum permission;
	
}
