ALTER TABLE tbfile
ADD description CHARACTER VARYING;

CREATE TABLE public.tbanswerquestion (
  id_answer BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  answer VARCHAR(255),
  id_question BIGINT,
  is_correct BOOLEAN,
  CONSTRAINT pk_tbanswerquestion PRIMARY KEY (id_answer)
);

ALTER TABLE public.tbanswerquestion ADD CONSTRAINT FK_TBANSWERQUESTION_ON_ID_QUESTION FOREIGN KEY (id_question) REFERENCES public.tbquestion (id_question);