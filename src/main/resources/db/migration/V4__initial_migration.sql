ALTER TABLE tbfile
RENAME COLUMN name
TO file_name;

ALTER TABLE tbfile
ADD name CHARACTER VARYING;