CREATE TABLE IF NOT EXISTS public.tbuser(
	iduser bigserial NOT NULL,
	first_name CHARACTER VARYING NOT NULL,
	last_name CHARACTER VARYING NOT NULL,
	password CHARACTER VARYING NOT NULL,
	username CHARACTER VARYING NOT NULL,
	email CHARACTER VARYING NOT NULL,
	is_active boolean default true,
	is_expired boolean default false,
	is_blocked boolean default false,
	
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