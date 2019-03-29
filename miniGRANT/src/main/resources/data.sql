/*
INSERT INTO donors(name) VALUES('Urząd Miasta w Krakowie');
INSERT INTO donors(name) VALUES('Wojewódzki Urząd Ochrony Środowiska');

INSERT INTO beneficiaries(name) VALUES('Uczniowski Klub Żeglarski Horn Kraków');
INSERT INTO beneficiaries(name) VALUES('Stowarzyszenie "Ekologia i Żagle"');

INSERT INTO cost_types(name) VALUES('Paliwo do motorówek');
INSERT INTO cost_types(name) VALUES('Wynagrodzenia');
INSERT INTO cost_types(name) VALUES('Strona internetowa');

INSERT INTO grants(donor_id,beneficiary_id,name,date_begin,date_end,status) VALUES(0,0,'Grant testowy nr 1','2018-01-01','2018-06-30',10);
INSERT INTO grants(donor_id,beneficiary_id,name,date_begin,date_end,status) VALUES(1,1,'Grant testowy nr 2','2018-02-11','2018-12-31',0);

INSERT INTO budgets(grant_id,cost_type_id,description,dotation,contribution_own,contribution_personal,contribution_inkind) VALUES(0,0,'Jakiś opis 1',1000.01,200.1,0,10.00);
INSERT INTO budgets(grant_id,cost_type_id,description,dotation,contribution_own,contribution_personal,contribution_inkind) VALUES(0,1,'Jakiś opis 2',2000.02,0,1000.2,10.00);
INSERT INTO budgets(grant_id,cost_type_id,description,dotation,contribution_own,contribution_personal,contribution_inkind) VALUES(1,1,'Jakiś opis 3',5000.03,900.30,0,10.00);
INSERT INTO budgets(grant_id,cost_type_id,description,dotation,contribution_own,contribution_personal,contribution_inkind) VALUES(1,0,'Jakiś opis 4',3000.04,100,6000.4,10.00);

INSERT INTO documents(grant_id,name,description,value) VALUES(0,'123/2018','Jakaś faktura',100.00);
INSERT INTO documents(grant_id,name,description,value) VALUES(1,'124/2018','Jakaś inna faktura',250.00);

INSERT INTO payments(budget_id,document_id,dotation,contribution_own,contribution_personal,contribution_inkind) VALUES(0,0,10.00,10.00,0.00,10.00);
INSERT INTO payments(budget_id,document_id,dotation,contribution_own,contribution_personal,contribution_inkind) VALUES(1,0,20.00,30.00,0.00,10.00);
*/
