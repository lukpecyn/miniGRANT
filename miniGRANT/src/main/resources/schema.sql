DROP TABLE IF EXISTS grants CASCADE;
DROP TABLE IF EXISTS cost_types CASCADE;
DROP TABLE IF EXISTS budgets CASCADE;

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
	grant_id INTEGER,
	cost_type_id INTEGER,
	budget_dotation DECIMAL(12,2),
	budget_own DECIMAL(12,2),
	budget_volunteerism DECIMAL(12,2),
	CONSTRAINT fk_budgets_grant FOREIGN KEY(grant_id) REFERENCES grants(id),
	CONSTRAINT fk_budgets_cost_type FOREIGN KEY (cost_type_id) REFERENCES cost_types(id)
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_budgets_grant_cost_type ON budgets(grant_id,cost_type_id);

INSERT INTO cost_types(name) VALUES('Paliwo do motorówek');
INSERT INTO cost_types(name) VALUES('Wynagrodzenia');

INSERT INTO grants(name,date_begin,date_end,status) VALUES('Grant testowy nr 1','2018-01-01','2018-06-30',10);
INSERT INTO grants(name,date_begin,date_end,status) VALUES('Grant testowy nr 2','2018-02-11','2018-12-31',0);

INSERT INTO budgets(grant_id,cost_type_id,budget_dotation,budget_own,budget_volunteerism) VALUES(0,0,1000,200,0);
INSERT INTO budgets(grant_id,cost_type_id,budget_dotation,budget_own,budget_volunteerism) VALUES(0,1,2000,0,1000);
INSERT INTO budgets(grant_id,cost_type_id,budget_dotation,budget_own,budget_volunteerism) VALUES(1,0,5000,900,0);
INSERT INTO budgets(grant_id,cost_type_id,budget_dotation,budget_own,budget_volunteerism) VALUES(1,1,3000,100,6000);
