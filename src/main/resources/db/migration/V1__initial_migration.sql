CREATE TABLE IF NOT EXISTS public.tbclient(
	idclient BIGSERIAL NOT NULL,
	cnpj CHARACTER VARYING NOT NULL,
	name CHARACTER VARYING NOT NULL,
	is_active BOOLEAN default true,
	is_expired BOOLEAN default false,
	is_blocked BOOLEAN default false,
	tenant CHARACTER VARYING NOT NULL,
	is_master BOOLEAN default false,
	created_by CHARACTER VARYING,
	updated_by CHARACTER VARYING,
	created_at TIMESTAMP default now(),
	updated_at TIMESTAMP,
	
	CONSTRAINT tbclient_pk PRIMARY KEY (idclient)
);

CREATE TABLE IF NOT EXISTS public.tbcompany_role(
	idcompany_role bigserial NOT NULL,
	name CHARACTER VARYING NOT NULL,
	idclient BIGINT NOT NULL, 
	created_by CHARACTER VARYING,
	updated_by CHARACTER VARYING,
	created_at TIMESTAMP default now(),
	updated_at TIMESTAMP,
	FOREIGN KEY (idclient) REFERENCES tbclient(idclient),
	CONSTRAINT tbcompany_role_pk PRIMARY KEY (idcompany_role)
);

CREATE TABLE IF NOT EXISTS public.tbuser(
	iduser bigserial NOT NULL,
	first_name CHARACTER VARYING NOT NULL,
	last_name CHARACTER VARYING NOT NULL,
	password CHARACTER VARYING NOT NULL,
	username CHARACTER VARYING NOT NULL,
	email CHARACTER VARYING NOT NULL,
	cpf CHARACTER VARYING,
	idcompany_role BIGINT NOT NULL,
	is_active BOOLEAN default true,
	is_expired BOOLEAN default false,
	is_blocked BOOLEAN default false,
	is_client BOOLEAN default false,
	idclient BIGINT NOT NULL,
	created_by CHARACTER VARYING,
	updated_by CHARACTER VARYING,
	created_at TIMESTAMP default now(),
	updated_at TIMESTAMP,
	
	FOREIGN KEY (idcompany_role) REFERENCES tbcompany_role(idcompany_role),
	FOREIGN KEY (idclient) REFERENCES tbclient(idclient),
	
	CONSTRAINT tbuser_pk PRIMARY KEY (iduser)
);

		

CREATE TABLE IF NOT EXISTS public.tbrole(
	idrole bigserial NOT NULL,
	role CHARACTER VARYING NOT NULL,
	
	CONSTRAINT tbrole_pk PRIMARY KEY (idrole)
);

CREATE TABLE IF NOT EXISTS public.tbpermission(
	idpermission bigserial NOT NULL,
	permission CHARACTER VARYING NOT NULL,
	
	CONSTRAINT tbpermission_pk PRIMARY KEY (idpermission)
);

CREATE TABLE IF NOT EXISTS public.tbrole_permission(
	idpermission bigint NOT NULL,
	idrole bigint NOT NULL,
	FOREIGN KEY (idpermission) REFERENCES tbpermission(idpermission),
	FOREIGN KEY (idrole) REFERENCES tbrole(idrole),
	
	CONSTRAINT tbrole_permissao_pk PRIMARY KEY (idrole, idpermission)
);

CREATE TABLE IF NOT EXISTS public.tbuser_role(
	iduser bigint NOT NULL,
	idrole bigint NOT NULL,
	FOREIGN KEY (iduser) REFERENCES tbuser(iduser),
	FOREIGN KEY (idrole) REFERENCES tbrole(idrole),
	
	CONSTRAINT tbuser_role_pk PRIMARY KEY (iduser, idrole)
);

