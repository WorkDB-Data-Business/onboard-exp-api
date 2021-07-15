package br.com.harvest.onboardexperience.domain.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import br.com.harvest.onboardexperience.domain.enumerators.PermissionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbpermission")
public class Permission {

	@Id
	@Column(name = "idpermission")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToMany(mappedBy = "permissions")
	private List<Role> roles;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "permission")
	private PermissionEnum permission;
	
}
