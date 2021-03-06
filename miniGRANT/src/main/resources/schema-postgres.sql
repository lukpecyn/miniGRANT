--DROP TABLE IF EXISTS authorities CASCADE;
--DROP TABLE IF EXISTS users CASCADE;

--DROP TABLE IF EXISTS beneficiaries CASCADE;
--DROP TABLE IF EXISTS coworkers CASCADE;
--DROP TABLE IF EXISTS donors CASCADE;
--DROP TABLE IF EXISTS grants CASCADE;
--DROP TABLE IF EXISTS cost_types CASCADE;
--DROP TABLE IF EXISTS budgets CASCADE;
--DROP TABLE IF EXISTS documents CASCADE;
--DROP TABLE IF EXISTS payments CASCADE;


CREATE TABLE IF NOT EXISTS users(
	username varchar(20) NOT NULL PRIMARY KEY,
	password varchar(60) NOT NULL,
	fullname varchar(50) NOT NULL,
	email varchar(50) NOT NULL,
	registration_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	guid UUID,
	confirmed BOOLEAN DEFAULT FALSE NOT NULL,
	enabled BOOLEAN DEFAULT FALSE NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_users_username ON users(username);
CREATE UNIQUE INDEX IF NOT EXISTS ix_users_email ON users(email);

CREATE TABLE IF NOT EXISTS authorities(
	username varchar(50) NOT NULL,
	authority varchar(50) not null,
	CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username) ON DELETE CASCADE 
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_auth_username ON authorities (username,authority);


CREATE TABLE IF NOT EXISTS beneficiaries(
	id SERIAL PRIMARY KEY,
	name VARCHAR(128) NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_beneficiaries_name ON beneficiaries(name);

CREATE TABLE IF NOT EXISTS coworkers( --coworkers
	username VARCHAR(50) NOT NULL,
	beneficiary_id INTEGER NOT NULL,
	
	CONSTRAINT fk_coworkers_user FOREIGN KEY(username) REFERENCES users(username) ON DELETE CASCADE,
	CONSTRAINT fk_coworkers_beneficiary FOREIGN KEY(beneficiary_id) REFERENCES beneficiaries(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_coworkers ON coworkers(username,beneficiary_id);

CREATE TABLE IF NOT EXISTS donors(
	id SERIAL PRIMARY KEY,
	name VARCHAR(128) NOT NULL,
	beneficiary_id INTEGER NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_donors_name_beneficiary ON donors(name,beneficiary_id);
/*
-- AKtualizacja struktury 'donors'
IF NOT EXISTS (SELECT * FROM information_schema.system_columns WHERE table_name='donors' AND column_name='beneficiary_id')
BEGIN
	ALTER TABLE donors ADD beneficiary_id INTEGER;
	ALTER TABLE donors ADD CONSTRAINT fk_donors_beneficiary FOREIGN KEY(beneficiary_id) REFERENCES beneficiaries(id);
	DROP INDEX ix_donors_name ON donors;
	CREATE UNIQUE INDEX IF NOT EXISTS ix_donors_name ON donors(name,beneficiary_id);
END;
*/

CREATE TABLE IF NOT EXISTS grants(
	id SERIAL PRIMARY KEY,
	donor_id INTEGER,
	beneficiary_id INTEGER,
	name VARCHAR(128) NOT NULL,
	date_begin DATE NOT NULL,
	date_end DATE NOT NULL,
	description TEXT,
	status INTEGER NOT NULL,
	CONSTRAINT fk_grants_donor FOREIGN KEY(donor_id) REFERENCES donors(id),
	CONSTRAINT fk_grants_beneficiary FOREIGN KEY(beneficiary_id) REFERENCES beneficiaries(id)
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_grants_date_begin_name_beneficiary ON grants(date_begin, date_end, name, beneficiary_id);

CREATE TABLE IF NOT EXISTS cost_types (
	id SERIAL PRIMARY KEY,
	name VARCHAR(64) NOT NULL,
	description TEXT,
	beneficiary_id INTEGER NOT NULL,
	
	CONSTRAINT fk_cost_type_beneficiary FOREIGN KEY(beneficiary_id) REFERENCES beneficiaries(id)
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_cost_types_name_beneficiary ON cost_types(name,beneficiary_id);

CREATE TABLE IF NOT EXISTS budgets (
	id SERIAL PRIMARY KEY,
	grant_id INTEGER NOT NULL,
	cost_type_id INTEGER NOT NULL,
	description TEXT,
	dotation DECIMAL(12,2),
	contribution_own DECIMAL(12,2),
	contribution_personal DECIMAL(12,2),
	contribution_inkind DECIMAL(12,2),
	CONSTRAINT fk_budgets_grant FOREIGN KEY(grant_id) REFERENCES grants(id),
	CONSTRAINT fk_budgets_cost_type FOREIGN KEY (cost_type_id) REFERENCES cost_types(id)
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_budgets_grant_cost_type ON budgets(grant_id,cost_type_id);

CREATE TABLE IF NOT EXISTS documents (
	id SERIAL PRIMARY KEY,
	grant_id INTEGER,
	name VARCHAR(64),
	description TEXT,
	value DECIMAL(12,2),
	
	CONSTRAINT fk_documents_grant FOREIGN KEY(grant_id) REFERENCES grants(id)
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_documents_grant_name ON documents(grant_id,name);

CREATE TABLE IF NOT EXISTS payments (
	id SERIAL PRIMARY KEY,
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
