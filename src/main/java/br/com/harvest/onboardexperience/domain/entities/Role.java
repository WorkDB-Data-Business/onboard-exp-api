package br.com.harvest.onboardexperience.domain.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

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
	
	@Column(name = "name")
	private String name;
	
	@ManyToMany
	@JoinTable(
	  name = "role_permission", 
	  joinColumns = @JoinColumn(name = "idrole"), 
	  inverseJoinColumns = @JoinColumn(name = "idpermission"))
	private Set<Permission> permissions;

}
