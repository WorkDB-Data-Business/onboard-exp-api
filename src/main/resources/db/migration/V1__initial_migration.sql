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