DROP TABLE IF EXISTS donors CASCADE;
DROP TABLE IF EXISTS beneficiaries CASCADE;
DROP TABLE IF EXISTS grants CASCADE;
DROP TABLE IF EXISTS cost_types CASCADE;
DROP TABLE IF EXISTS budgets CASCADE;
DROP TABLE IF EXISTS documents CASCADE;
DROP TABLE IF EXISTS payments CASCADE;

CREATE TABLE IF NOT EXISTS donors(
	id INTEGER IDENTITY PRIMARY KEY,
	name VARCHAR_IGNORECASE(128) NOT NULL,
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_donors_name ON donors(name);

CREATE TABLE IF NOT EXISTS beneficiaries(
	id INTEGER IDENTITY PRIMARY KEY,
	name VARCHAR_IGNORECASE(128) NOT NULL,
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_beneficiaries_name ON beneficiaries(name);

CREATE TABLE IF NOT EXISTS grants(
	id INTEGER IDENTITY PRIMARY KEY,
	donor_id INTEGER,
	beneficiary_id INTEGER,
	name VARCHAR_IGNORECASE(128) NOT NULL,
	date_begin DATE NOT NULL,
	date_end DATE NOT NULL,
	description LONGVARCHAR,
	status INTEGER NOT NULL,
	CONSTRAINT fk_grants_donor FOREIGN KEY(donor_id) REFERENCES donors(id),
	CONSTRAINT fk_grants_beneficiary FOREIGN KEY(beneficiary_id) REFERENCES beneficiaries(id),
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_grants_date_begin_name ON grants(date_begin, date_end, name);

CREATE TABLE IF NOT EXISTS cost_types (
	id INTEGER IDENTITY PRIMARY KEY,
	name VARCHAR_IGNORECASE(64) NOT NULL,
	description LONGVARCHAR
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_cost_types_name ON cost_types(name);

CREATE TABLE IF NOT EXISTS budgets (
	id INTEGER IDENTITY PRIMARY KEY,
	grant_id INTEGER NOT NULL,
	cost_type_id INTEGER NOT NULL,
	description LONGVARCHAR,
	dotation DECIMAL(12,2),
	contribution_own DECIMAL(12,2),
	contribution_personal DECIMAL(12,2),
	contribution_inkind DECIMAL(12,2),
	CONSTRAINT fk_budgets_grant FOREIGN KEY(grant_id) REFERENCES grants(id),
	CONSTRAINT fk_budgets_cost_type FOREIGN KEY (cost_type_id) REFERENCES cost_types(id)
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_budgets_grant_cost_type ON budgets(grant_id,cost_type_id);

CREATE TABLE IF NOT EXISTS documents (
	id INTEGER IDENTITY PRIMARY KEY,
	grant_id INTEGER,
	name VARCHAR_IGNORECASE(64),
	description LONGVARCHAR,
	value DECIMAL(12,2),
	
	CONSTRAINT fk_documents_grant FOREIGN KEY(grant_id) REFERENCES grants(id)
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_documents_grant_name ON documents(grant_id,name);

CREATE TABLE IF NOT EXISTS payments (
	id INTEGER IDENTITY PRIMARY KEY,
	budget_id INTEGER,
	document_id INTEGER,
	dotation DECIMAL(12,2), --dotacja
	contribution_own DECIMAL(12,2), --wkład własny 
	contribution_personal DECIMAL(12,2), --wkład osobowy (wolontariat, itp.)
	contribution_inkind DECIMAL(12,2), --wkłąd rzeczowy (użyczenie sprzętu własnego lub obcego)

	CONSTRAINT fk_payments_budget FOREIGN KEY(budget_id) REFERENCES budgets(id),
	CONSTRAINT fk_payments_document FOREIGN KEY(document_id) REFERENCES documents(id)	
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_payments_budget_document ON payments(budget_id,document_id);

