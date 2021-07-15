package br.com.harvest.onboardexperience.domain.entities;

import java.util.List;
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
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbrole")
public class Role {
	
	@Id
	@Column(name = "idrole")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private RoleEnum role;
	
	@ManyToMany(mappedBy = "roles")
	private List<User> users;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	  name = "tbrole_permission", 
	  joinColumns = @JoinColumn(name = "idrole"), 
	  inverseJoinColumns = @JoinColumn(name = "idpermission"))
	private Set<Permission> permissions;

}
