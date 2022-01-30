ALTER TABLE tbfile
ADD description CHARACTER VARYING;

--CREATE TABLE IF NOT EXISTS public.tbanswerquestion(
--	id_answer BIGSERIAL NOT NULL,
--	idclient BIGINT NOT NULL,
--	is_correct BOOLEAN NOT NULL,
--    answer   CHARACTER NOT NULL,
--    id_question BIGINT NOT NULL,
--
--    FOREIGN KEY (id_question) REFERENCES tbquestion(id_question),
--
--    CONSTRAINT tbanswerquestion_pk PRIMARY KEY (id_answer)
--);

CREATE TABLE public.tbanswerquestion (
  id_answer BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  answer VARCHAR(255),
  id_question BIGINT,
  is_correct BOOLEAN,
  CONSTRAINT pk_tbanswerquestion PRIMARY KEY (id_answer)
);

ALTER TABLE public.tbanswerquestion ADD CONSTRAINT FK_TBANSWERQUESTION_ON_ID_QUESTION FOREIGN KEY (id_question) REFERENCES public.tbquestion (id_question);

--
--CREATE TABLE IF NOT EXISTS public.tbanswer_question(
--	id_question bigint NOT NULL,
--	id_answer bigint NOT NULL,
--    FOREIGN KEY (id_question) REFERENCES tbquestion(id_question),
--	FOREIGN KEY (id_answer) REFERENCES tbanswerquestion(id_answer),
--
--
--	CONSTRAINT tbanswer_question_pk PRIMARY KEY (id_question, id_answer)
--);