CREATE TABLE IF NOT EXISTS public.tbtrail(
	idtrail BIGSERIAL NOT NULL,
	name CHARACTER VARYING NOT NULL,
	description CHARACTER VARYING NOT NULL,
	author BIGINT NOT NULL,
	is_active BOOLEAN NOT NULL,
    idcoin BIGINT NOT NULL,
    map_image_path CHARACTER VARYING NOT NULL,
    map_music_path CHARACTER VARYING,
    conclusion_date TIMESTAMP NOT NULL,
    idclient BIGINT NOT NULL,

    created_by CHARACTER VARYING,
    updated_by CHARACTER VARYING,
    created_at TIMESTAMP default now(),
    updated_at TIMESTAMP,

    FOREIGN KEY (author) REFERENCES tbuser(iduser),
    FOREIGN KEY (idcoin) REFERENCES tbcoin(idcoin),
    FOREIGN KEY (idclient) REFERENCES tbclient(idclient),

	CONSTRAINT tbtrail_pk PRIMARY KEY (idtrail)
);

CREATE TABLE IF NOT EXISTS public.tbposition(
	x_axis DECIMAL NOT NULL,
	y_axis DECIMAL NOT NULL,

	CONSTRAINT tbposition_pk PRIMARY KEY (x_axis, y_axis)
);

CREATE TABLE IF NOT EXISTS public.tbtrail_map_position_path(
	x_axis DECIMAL NOT NULL,
	y_axis DECIMAL NOT NULL,
	idtrail BIGINT NOT NULL,

	FOREIGN KEY (x_axis, y_axis) REFERENCES tbposition(x_axis, y_axis),
    FOREIGN KEY (idtrail) REFERENCES tbtrail(idtrail),

	CONSTRAINT tbtrail_map_position_path_pk PRIMARY KEY (x_axis, y_axis, idtrail)
);

CREATE TABLE IF NOT EXISTS public.tbtrail_group(
	idtrail bigint NOT NULL,
	idgroup bigint NOT NULL,
	FOREIGN KEY (idtrail) REFERENCES tbtrail(idtrail),
	FOREIGN KEY (idgroup) REFERENCES tbgroup(idgroup),

	CONSTRAINT tbtrail_group_pk PRIMARY KEY (idtrail, idgroup)
);

CREATE TABLE IF NOT EXISTS public.tbtrail_user_registration(
	idtrail bigint NOT NULL,
	iduser bigint NOT NULL,
	average_score DECIMAL,
	started_trail_date TIMESTAMP,
	finished_trail_date TIMESTAMP,
	FOREIGN KEY (idtrail) REFERENCES tbtrail(idtrail),
	FOREIGN KEY (iduser) REFERENCES tbuser(iduser),

	CONSTRAINT tbtrail_user_registration_pk PRIMARY KEY (idtrail, iduser)
);

CREATE TABLE IF NOT EXISTS public.tbstage(
	idstage BIGSERIAL NOT NULL,
	name CHARACTER VARYING NOT NULL,
	description CHARACTER VARYING NOT NULL,
	amount_coins BIGINT NOT NULL,
	minimum_score DECIMAL NOT NULL,
	idtrail BIGINT NOT NULL,
	is_pre_requisite BOOLEAN NOT NULL,
	x_axis DECIMAL NOT NULL,
	y_axis DECIMAL NOT NULL,

    created_by CHARACTER VARYING,
    updated_by CHARACTER VARYING,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP,

    FOREIGN KEY (idtrail) REFERENCES tbtrail(idtrail),
    FOREIGN KEY (x_axis, y_axis) REFERENCES tbposition(x_axis, y_axis),

	CONSTRAINT tbstage_pk PRIMARY KEY (idstage)
);

CREATE TABLE IF NOT EXISTS public.tbstage_scorm(
	idscorm CHARACTER VARYING NOT NULL,
	idstage BIGINT NOT NULL,

	created_by CHARACTER VARYING,
    updated_by CHARACTER VARYING,
    created_at TIMESTAMP default now(),
    updated_at TIMESTAMP,

	FOREIGN KEY (idscorm) REFERENCES tbscorm(idscorm) ON DELETE CASCADE,
	FOREIGN KEY (idstage) REFERENCES tbstage(idstage) ON DELETE CASCADE,

	CONSTRAINT tbstage_scorm_pk PRIMARY KEY (idscorm, idstage)
);


CREATE TABLE IF NOT EXISTS public.tbstage_file(
	idfile BIGINT NOT NULL,
	idstage BIGINT NOT NULL,
	created_by CHARACTER VARYING,
    updated_by CHARACTER VARYING,
    created_at TIMESTAMP default now(),
    updated_at TIMESTAMP,

	FOREIGN KEY (idfile) REFERENCES tbfile(idfile) ON DELETE CASCADE,
	FOREIGN KEY (idstage) REFERENCES tbstage(idstage) ON DELETE CASCADE,

	CONSTRAINT tbstage_file_pk PRIMARY KEY (idfile, idstage)
);


CREATE TABLE IF NOT EXISTS public.tbstage_link(
	idlink BIGINT NOT NULL,
	idstage BIGINT NOT NULL,
	created_by CHARACTER VARYING,
    updated_by CHARACTER VARYING,
    created_at TIMESTAMP default now(),
    updated_at TIMESTAMP,

	FOREIGN KEY (idlink) REFERENCES tblink(idlink) ON DELETE CASCADE,
	FOREIGN KEY (idstage) REFERENCES tbstage(idstage) ON DELETE CASCADE,

	CONSTRAINT tbstage_link_pk PRIMARY KEY (idlink, idstage)
);


CREATE TABLE IF NOT EXISTS public.tbstage_scorm_user(
	idscorm CHARACTER VARYING NOT NULL,
	idstage BIGINT NOT NULL,
	is_completed BOOLEAN NOT NULL,
	iduser BIGINT NOT NULL,
	completed_at TIMESTAMP,

	FOREIGN KEY (iduser) REFERENCES tbuser(iduser) ON DELETE CASCADE,
	FOREIGN KEY (idscorm, idstage) REFERENCES tbstage_scorm(idscorm, idstage) ON DELETE CASCADE,

	CONSTRAINT tbstage_scorm_user_pk PRIMARY KEY (idscorm, idstage, iduser)
);

CREATE TABLE IF NOT EXISTS public.tbstage_file_user(
	idfile BIGINT NOT NULL,
	idstage BIGINT NOT NULL,
	is_completed BOOLEAN NOT NULL,
	iduser BIGINT NOT NULL,
	completed_at TIMESTAMP,

	FOREIGN KEY (iduser) REFERENCES tbuser(iduser) ON DELETE CASCADE,
	FOREIGN KEY (idfile, idstage) REFERENCES tbstage_file(idfile, idstage) ON DELETE CASCADE,

	CONSTRAINT tbstage_file_user_pk PRIMARY KEY (idfile, idstage, iduser)
);

CREATE TABLE IF NOT EXISTS public.tbstage_link_user(
	idlink BIGINT NOT NULL,
	idstage BIGINT NOT NULL,
	is_completed BOOLEAN NOT NULL,
	iduser BIGINT NOT NULL,
	completed_at TIMESTAMP,

	FOREIGN KEY (iduser) REFERENCES tbuser(iduser) ON DELETE CASCADE,
	FOREIGN KEY (idlink, idstage) REFERENCES tbstage_link(idlink, idstage) ON DELETE CASCADE,

	CONSTRAINT tbstage_link_user_pk PRIMARY KEY (idlink, idstage, iduser)
);


CREATE TABLE IF NOT EXISTS public.tbstage_user(
	iduser BIGINT NOT NULL,
	idstage BIGINT NOT NULL,
	score DECIMAL,
	is_completed BOOLEAN NOT NULL,
	completed_at TIMESTAMP,
	started_at TIMESTAMP NOT NULL,

	FOREIGN KEY (iduser) REFERENCES tbuser(iduser) ON DELETE CASCADE,
    FOREIGN KEY (idstage) REFERENCES tbstage(idstage) ON DELETE CASCADE,

	CONSTRAINT tbstage_user_pk PRIMARY KEY (idstage, iduser)
);

CREATE TABLE public.tbquestion (
  idquestion BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name CHARACTER VARYING(255) NOT NULL,
  description CHARACTER VARYING,
  score_question BIGINT,
  is_active BOOLEAN,
  is_multiplechoice BOOLEAN,
  author BIGINT,

  FOREIGN KEY(author) references tbuser(iduser),
  CONSTRAINT pk_tbquestion PRIMARY KEY (idquestion)
);
