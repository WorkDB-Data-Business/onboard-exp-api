CREATE TABLE IF NOT EXISTS public.tbclient(
	idclient BIGSERIAL NOT NULL,
	cnpj CHARACTER VARYING NOT NULL,
	name CHARACTER VARYING NOT NULL,
	is_active BOOLEAN,
	is_expired BOOLEAN,
	is_blocked BOOLEAN,
	tenant CHARACTER VARYING NOT NULL,
	is_master BOOLEAN,
	email CHARACTER VARYING NOT NULL,
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
	is_active BOOLEAN NOT NULL,
	is_expired BOOLEAN NOT NULL,
	is_blocked BOOLEAN NOT NULL,
	is_client BOOLEAN NOT NULL,
	is_first_login BOOLEAN NOT NULL,
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

CREATE TABLE IF NOT EXISTS public.tbcoin(
	idcoin BIGSERIAL NOT NULL,
	image_path CHARACTER VARYING NOT NULL,
	name CHARACTER VARYING NOT NULL,
	idclient BIGINT NOT NULL,
	is_active BOOLEAN NOT NULL,
	created_by CHARACTER VARYING,
	updated_by CHARACTER VARYING,
	created_at TIMESTAMP default now(),
	updated_at TIMESTAMP,
	
	FOREIGN KEY (idclient) REFERENCES tbclient(idclient),
	
	CONSTRAINT tbcoin_pk PRIMARY KEY (idcoin)
);

CREATE TABLE IF NOT EXISTS public.tbreward(
	idreward BIGSERIAL NOT NULL,
	image_path CHARACTER VARYING NOT NULL,
	price DECIMAL NOT NULL,
	name CHARACTER VARYING NOT NULL,
	idclient BIGINT NOT NULL,
	is_active BOOLEAN NOT NULL,
	created_by CHARACTER VARYING,
	updated_by CHARACTER VARYING,
	created_at TIMESTAMP default now(),
	updated_at TIMESTAMP,
	
	FOREIGN KEY (idclient) REFERENCES tbclient(idclient),
	
	CONSTRAINT tbreward_pk PRIMARY KEY (idreward)
);

CREATE TABLE IF NOT EXISTS public.tbuser_coin(
	iduser bigint NOT NULL,
	idcoin bigint NOT NULL,
	FOREIGN KEY (iduser) REFERENCES tbuser(iduser),
	FOREIGN KEY (idcoin) REFERENCES tbcoin(idcoin),
	
	CONSTRAINT tbuser_coin_pk PRIMARY KEY (iduser, idcoin)
);