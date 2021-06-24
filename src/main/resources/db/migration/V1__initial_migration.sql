CREATE TABLE IF NOT EXISTS public.tbuser(
	iduser bigserial NOT NULL,
	username character varying,

	CONSTRAINT tbuser_pk PRIMARY KEY (iduser)
);