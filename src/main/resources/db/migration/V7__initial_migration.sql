CREATE TABLE IF NOT EXISTS public.tbanswer_descriptive (
  idanswer_descriptive BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  idquestion BIGINT,
  author BIGINT,
  answer_descriptive VARCHAR(255),

  FOREIGN KEY (author) references tbuser(iduser),
  FOREIGN KEY (idquestion) references tbquestion(idquestion),
  CONSTRAINT pk_tbanswer_descriptiv PRIMARY KEY (idanswer_descriptive)
);

CREATE TABLE IF NOT EXISTS public.tbtext_client(
     idtext BIGINT,
     idclient  BIGINT,

      CONSTRAINT ttbtext_client_pk PRIMARY KEY (idtext, idclient)
);