DROP TABLE IF EXISTS grants CASCADE;
DROP TABLE IF EXISTS cost_types CASCADE;
DROP TABLE IF EXISTS budgets CASCADE;
DROP TABLE IF EXISTS documents CASCADE;

CREATE TABLE IF NOT EXISTS grants(
	id INTEGER IDENTITY PRIMARY KEY,
	name VARCHAR_IGNORECASE(128) NOT NULL,
	date_begin DATE NOT NULL,
	date_end DATE NOT NULL,
	description LONGVARCHAR,
	status INTEGER NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_grants_date_begin_name ON grants(date_begin, date_end, name);

CREATE TABLE IF NOT EXISTS cost_types (
	id INTEGER IDENTITY PRIMARY KEY,
	name VARCHAR_IGNORECASE(64) NOT NULL,
	description LONGVARCHAR
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_cost_types_name ON cost_types(name);

CREATE TABLE budgets (
	id INTEGER IDENTITY PRIMARY KEY,
	grant_id INTEGER NOT NULL,
	cost_type_id INTEGER NOT NULL,
	description LONGVARCHAR,
	dotation DECIMAL(12,2),
	own DECIMAL(12,2),
	volunteerism DECIMAL(12,2),
	CONSTRAINT fk_budgets_grant FOREIGN KEY(grant_id) REFERENCES grants(id),
	CONSTRAINT fk_budgets_cost_type FOREIGN KEY (cost_type_id) REFERENCES cost_types(id)
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_budgets_grant_cost_type ON budgets(grant_id,cost_type_id);

CREATE TABLE documents (
	id INTEGER IDENTITY PRIMARY KEY,
	grant_id INTEGER,
	name VARCHAR_IGNORECASE(64),
	description LONGVARCHAR,
	value DECIMAL(12,2),
	CONSTRAINT fk_documents_grant FOREIGN KEY(grant_id) REFERENCES grants(id)
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_documents_grant_name ON documents(grant_id,name);
