INSERT INTO cost_types(name) VALUES('Paliwo do motorówek');
INSERT INTO cost_types(name) VALUES('Wynagrodzenia');
INSERT INTO cost_types(name) VALUES('Strona internetowa');

INSERT INTO grants(name,date_begin,date_end,status) VALUES('Grant testowy nr 1','2018-01-01','2018-06-30',10);
INSERT INTO grants(name,date_begin,date_end,status) VALUES('Grant testowy nr 2','2018-02-11','2018-12-31',0);

INSERT INTO budgets(grant_id,cost_type_id,description,dotation,own,volunteerism) VALUES(0,0,'Jakiś opis 1',1000.01,200.1,0);
INSERT INTO budgets(grant_id,cost_type_id,description,dotation,own,volunteerism) VALUES(0,1,'Jakiś opis 2',2000.02,0,1000.2);
INSERT INTO budgets(grant_id,cost_type_id,description,dotation,own,volunteerism) VALUES(1,1,'Jakiś opis 3',5000.03,900.30,0);
INSERT INTO budgets(grant_id,cost_type_id,description,dotation,own,volunteerism) VALUES(1,0,'Jakiś opis 4',3000.04,100,6000.4);

INSERT INTO documents(grant_id,name,description,value) VALUES(0,'123/2018','Jakaś faktura',100.00);
INSERT INTO documents(grant_id,name,description,value) VALUES(1,'124/2018','Jakaś inna faktura',250.00);
