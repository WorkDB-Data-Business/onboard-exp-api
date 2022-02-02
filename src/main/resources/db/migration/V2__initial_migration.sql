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

CREATE TABLE IF NOT EXISTS public.tbtrail(
	idtrail BIGSERIAL NOT NULL,
	name_trail CHARACTER VARYING NOT NULL,
	description_trail CHARACTER NOT NULL,
	iduser BIGINT NOT NULL,
	is_active BOOLEAN NOT NULL,
    is_available BOOLEAN NOT NULL,
    idcoin BIGINT NOT NULL,
    idstage BIGINT NOT NULL,

    FOREIGN KEY (iduser) REFERENCES tbuser(iduser),
    FOREIGN KEY (idcoin) REFERENCES tbcoin(idcoin),

	CONSTRAINT tbtrail_pk PRIMARY KEY (idtrail)
);

CREATE TABLE IF NOT EXISTS public.tbtrail_stage(
	idstage bigint NOT NULL,
	idtrail bigint NOT NULL,
	FOREIGN KEY (idstage) REFERENCES tbstage(idstage),
	FOREIGN KEY (idtrail) REFERENCES tbtrail(idtrail),

	CONSTRAINT tbtrail_stage_pk PRIMARY KEY (idstage, idtrail)
);

CREATE TABLE IF NOT EXISTS public.tbevent(
	idevent BIGSERIAL NOT NULL,
	name CHARACTER VARYING NOT NULL,
	description CHARACTER NOT NULL,
	is_active BOOLEAN NOT NULL,
    type BOOLEAN NOT NULL,
    idclient BIGINT NOT NULL,

    FOREIGN KEY (idclient) REFERENCES tbclient(idclient),

	CONSTRAINT tbevent_pk PRIMARY KEY (idevent)
);

CREATE TABLE public.tbquestion (
  idquestion BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name CHARACTER VARYING(255),
  description CHARACTER VARYING,
  score_question BIGINT,
  is_active BOOLEAN,
  is_multiplechoice BOOLEAN,
  author BIGINT,

  FOREIGN KEY(author) references tbuser(iduser),
  CONSTRAINT pk_tbquestion PRIMARY KEY (idquestion)
);

CREATE TABLE IF NOT EXISTS public.tbtext(
	idtext BIGSERIAL NOT NULL,
	title CHARACTER VARYING NOT NULL,
	description CHARACTER VARYING NOT NULL,
	text CHARACTER VARYING NOT NULL,
	is_active BOOLEAN NOT NULL,
	idevent BIGINT,
	idclient BIGINT,

	FOREIGN KEY (idevent) REFERENCES tbevent(idevent),
	FOREIGN KEY (idclient) REFERENCES tbclient(idclient),

	CONSTRAINT tbtext_pk PRIMARY KEY (idtext)

);

