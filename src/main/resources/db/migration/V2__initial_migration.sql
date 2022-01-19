CREATE TABLE IF NOT EXISTS public.tb_trail(
	id_trail LONG NOT NULL,
	name_trail CHARACTER VARYING NOT NULL,
	description_trail CHARACTER NOT NULL,
	user_id lONG NOT NULL,
	arquivo_trilha ARCHIVE NOT NULL,
	arquivo_trilha_nome CHARACTER VARYING NOT NULL,
    is_active BOOLEAN NOT NULL,
    is_available BOOLEAN NOT NULL,

	CONSTRAINT tbclient_pk PRIMARY KEY (idclient)
