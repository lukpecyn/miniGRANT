DROP TABLE IF EXISTS grants CASCADE;

CREATE TABLE IF NOT EXISTS grants(
	id INTEGER IDENTITY PRIMARY KEY,
	name VARCHAR(128) NOT NULL,
	date_begin DATE NOT NULL,
	date_end DATE NOT NULL,
	description LONGVARCHAR,
	status INTEGER NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS ix_grants_date_begin_name ON grants(date_begin, name);

--INSERT INTO grants(name,date_begin,date_end,status) VALUES('Grant testowy nr 1','2018-01-01','2018-06-30',0);
--INSERT INTO grants(name,date_begin,date_end,status) VALUES('Grant testowy nr 2','2018-02-11','2018-12-31',0);