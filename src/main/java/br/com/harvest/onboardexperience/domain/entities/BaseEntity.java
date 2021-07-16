package br.com.harvest.onboardexperience.domain.entities;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = -9198400569089110984L;

}
