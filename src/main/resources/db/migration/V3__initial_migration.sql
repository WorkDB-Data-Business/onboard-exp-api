ALTER TABLE tbfile
ADD description CHARACTER VARYING;

CREATE TABLE IF NOT EXISTS public.tbanswerquestion(
	id_answer BIGSERIAL NOT NULL,
	idclient BIGINT NOT NULL,
    answer   CHARACTER NOT NULL,

    CONSTRAINT tbanswerquestion_pk PRIMARY KEY (id_answer)
);

CREATE TABLE IF NOT EXISTS public.tbanswer_question(
	id_question bigint NOT NULL,
	id_answer bigint NOT NULL,
    FOREIGN KEY (id_question) REFERENCES tbquestion(id_question),
	FOREIGN KEY (id_answer) REFERENCES tbanswerquestion(id_answer),


	CONSTRAINT tbanswer_question_pk PRIMARY KEY (id_question, id_answer)
);