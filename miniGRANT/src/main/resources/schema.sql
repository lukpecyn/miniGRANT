DROP TABLE IF EXISTS grants CASCADE;

CREATE TABLE IF NOT EXISTS grants(
	id_grant INTEGER IDENTITY PRIMARY KEY,
	name VARCHAR(128) NOT NULL,
	date_begin DATE NOT NULL,
	date_end DATE NOT NULL,
	status INTEGER NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS ix_grants_date_begin_name ON grants(date_begin, name);
