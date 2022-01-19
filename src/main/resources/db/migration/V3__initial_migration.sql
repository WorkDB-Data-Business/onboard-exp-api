CREATE TABLE IF NOT EXISTS public.tbstage(
	idstage BIGSERIAL NOT NULL,
	name CHARACTER VARYING NOT NULL,
	description CHARACTER VARYING NOT NULL,
	amount_coins BIGINT NOT NULL,
	idclient BIGINT NOT NULL,
	is_active BOOLEAN NOT NULL,
	is_available BOOLEAN NOT NULL,
	is_prerequisite BOOLEAN NOT NULL,
	idcoin BIGINT NOT NULL,
    is_muted BOOLEAN NOT NULL,
    created_by CHARACTER VARYING,
    updated_by CHARACTER VARYING,
    created_at TIMESTAMP default now(),
    updated_at TIMESTAMP,

    FOREIGN KEY (idclient) REFERENCES tbclient(idclient),
    FOREIGN KEY (idcoin) REFERENCES tbcoin(idcoin),

	CONSTRAINT tbstage_pk PRIMARY KEY (idstage)
);