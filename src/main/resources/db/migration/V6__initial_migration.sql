CREATE TABLE IF NOT EXISTS public.tbharvest_library_scorm(
    idscorm BIGINT,
    idclient BIGINT,

	FOREIGN KEY (idscorm) REFERENCES tbscorm(idscorm),
	FOREIGN KEY (idclient) REFERENCES tbclient(idclient),

    CONSTRAINT tbharvest_library_scorm_pk PRIMARY KEY (idscorm, idclient)
);

ALTER TABLE tbscorm
    DROP idclient;

ALTER TABLE tbscorm
    ADD CONSTRAINT author_fk FOREIGN KEY (author)
          REFERENCES tbuser (iduser);

ALTER TABLE tbscorm
    ADD preview_image_path CHARACTER VARYING;