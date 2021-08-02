package br.com.harvest.onboardexperience.domain.entities;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.ObjectUtils;
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
@Entity(name = "tbuser")
@SQLDelete(sql = SQLQueryUtils.SOFT_DELETE_USER, check = ResultCheckStyle.COUNT)
@Where(clause = SQLQueryUtils.IS_ACTIVE_FILTER)
public class User extends BaseEntityAudit {

	private static final long serialVersionUID = -4309141727418094357L;

	@Id
	@Column(name = "iduser")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "cpf")
	private String cpf;
	
	@ManyToOne
	@JoinColumn(name="idcompany_role")
	private CompanyRole companyRole;
	
	@Column(name = "is_active")
	private Boolean isActive;
	
	@Column(name = "is_blocked")
	private Boolean isBlocked;
	
	@Column(name = "is_expired")
	private Boolean isExpired;
	
	@Column(name = "is_first_login")
	private Boolean isFirstLogin;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(
	  name = "tbuser_role", 
	  joinColumns = @JoinColumn(name = "iduser"), 
	  inverseJoinColumns = @JoinColumn(name = "idrole"))
	private Set<Role> roles;
	
	@ManyToOne
	@JoinColumn(name = "idclient")
	private Client client;
	
	@Column(name = "is_client")
	private Boolean isClient;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(
	  name = "tbuser_coin", 
	  joinColumns = @JoinColumn(name = "iduser"), 
	  inverseJoinColumns = @JoinColumn(name = "idcoin"))
	private List<Coin> coins;
	
	public Integer getAmountOfCoins() {
		if(ObjectUtils.isEmpty(this.coins)) {
			return 0;
		}
		
		return this.coins.size();
	}
	
}
