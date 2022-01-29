CREATE TABLE IF NOT EXISTS public.tbharvest_library_scorm(
    idscorm CHARACTER VARYING,
    idclient BIGINT,

	FOREIGN KEY (idscorm) REFERENCES tbscorm(idscorm),
	FOREIGN KEY (idclient) REFERENCES tbclient(idclient),

    CONSTRAINT tbharvest_library_scorm_pk PRIMARY KEY (idscorm, idclient)
);

ALTER TABLE tbscorm
    DROP idclient;

ALTER TABLE tbscorm
    ADD author BIGINT NOT NULL;

ALTER TABLE tbscorm
    ADD is_active BOOLEAN NOT NULL;

ALTER TABLE tbscorm
    ADD CONSTRAINT author_fk FOREIGN KEY (author)
          REFERENCES tbuser (iduser);

ALTER TABLE tbscorm
    ADD preview_image_path CHARACTER VARYING;

ALTER TABLE tbfile
    ADD is_image_preview BOOLEAN NOT NULL;