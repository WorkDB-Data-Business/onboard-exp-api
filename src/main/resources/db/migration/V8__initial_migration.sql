CREATE TABLE IF NOT EXISTS public.tbstage_questionnaire(
	idquestionnaire BIGINT NOT NULL,
	idstage BIGINT NOT NULL,

	created_by CHARACTER VARYING,
    updated_by CHARACTER VARYING,
    created_at TIMESTAMP default now(),
    updated_at TIMESTAMP,

	FOREIGN KEY (idquestionnaire) REFERENCES tbquestionnaire(idquestionnaire) ON DELETE CASCADE,
	FOREIGN KEY (idstage) REFERENCES tbstage(idstage) ON DELETE CASCADE,

	CONSTRAINT tbstage_questionnaire_pk PRIMARY KEY (idquestionnaire, idstage)
);

CREATE TABLE IF NOT EXISTS public.tbstage_questionnaire_user(
    idquestionnaire BIGINT NOT NULL,
	idstage BIGINT NOT NULL,
	is_completed BOOLEAN NOT NULL,
	iduser BIGINT NOT NULL,
	completed_at TIMESTAMP,

	FOREIGN KEY (iduser) REFERENCES tbuser(iduser) ON DELETE CASCADE,
	FOREIGN KEY (idquestionnaire, idstage) REFERENCES tbstage_questionnaire(idquestionnaire, idstage) ON DELETE CASCADE,

	CONSTRAINT tbstage_questionnaire_user_pk PRIMARY KEY (idquestionnaire, idstage, iduser)
);