CREATE TABLE IF NOT EXISTS public.tbanswer_descriptive (
  id_answer_descriptive BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  id_question BIGINT,
  author BIGINT,
  answer_descriptive VARCHAR(255),

  FOREIGN KEY (author) references tbuser(iduser),
  CONSTRAINT pk_tbanswer_descriptiv PRIMARY KEY (id_answer_descriptive)
);