CREATE TABLE IF NOT EXISTS public.tbtrail(
	idtrail BIGSERIAL NOT NULL,
	name_trail CHARACTER VARYING NOT NULL,
	description_trail CHARACTER NOT NULL,
	iduser BIGSERIAL NOT NULL,
	is_active BOOLEAN NOT NULL,
    is_available BOOLEAN NOT NULL,
    idcoin BIGINT NOT NULL,
    idstage BIGINT NOT NULL,


	CONSTRAINT idtrail_pk PRIMARY KEY (idtrail)
);




