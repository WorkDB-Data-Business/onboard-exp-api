package br.com.harvest.onboardexperience.domain.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import br.com.harvest.onboardexperience.domain.enumerators.RoleEnum;
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
@Entity(name = "tbrole")
public class Role extends BaseEntity {
	
	private static final long serialVersionUID = -8238875560346007870L;

	@Id
	@Column(name = "idrole")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private RoleEnum role;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	  name = "tbrole_permission", 
	  joinColumns = @JoinColumn(name = "idrole"), 
	  inverseJoinColumns = @JoinColumn(name = "idpermission"))
	private Set<Permission> permissions;

}
