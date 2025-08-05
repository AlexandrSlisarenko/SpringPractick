--liquibase formatted sql

--changeset aslisarenko:1
INSERT INTO data_jpa.company(id, name) VALUES (1,'Cinimex');
INSERT INTO data_jpa.company(id, name) VALUES (2,'Demis Group');
INSERT INTO data_jpa.company(id, name) VALUES (3,'Avikey');
INSERT INTO data_jpa.company(id, name) VALUES (4,'SEO');
INSERT INTO data_jpa.company(id, name) VALUES (5,'ALT');
INSERT INTO data_jpa.company(id, name) VALUES (6,'APPLE');
SELECT SETVAL('data_jpa.company_id_seq', (SELECT MAX(id) FROM data_jpa.company));
-- rollback TRUNCATE TABLE data_jpa.company;

--changeset aslisarenko:2
INSERT INTO data_jpa.company_locales(company_id, lang, description) VALUES (1,'RU', 'Last company');
INSERT INTO data_jpa.company_locales(company_id, lang, description) VALUES (2,'EN', 'WEB company');
INSERT INTO data_jpa.company_locales(company_id, lang, description) VALUES (3,'EN', 'WEB studio');
INSERT INTO data_jpa.company_locales(company_id, lang, description) VALUES (4,'RU', 'SEO studio');
INSERT INTO data_jpa.company_locales(company_id, lang, description) VALUES (5,'RU', 'DevOps studio');
-- rollback TRUNCATE TABLE data_jpa.company_locales;

--changeset aslisarenko:3
INSERT INTO data_jpa.users(username, firstname, lastname, birth_date, role, company_id, id) VALUES ('ivanov_ivan','Иван', 'Иванов', '12/15/1989', 'USER_ROLE', 1,1);
INSERT INTO data_jpa.users(username, firstname, lastname, birth_date, role, company_id, id) VALUES ('petrov_petr','Петр', 'Петров', '05/17/1996', NULL, 1,2);
INSERT INTO data_jpa.users(username, firstname, lastname, birth_date, role, company_id, id) VALUES ('user3','Тест1', 'Тест', '09/15/1989', NULL, 2,3);
INSERT INTO data_jpa.users(username, firstname, lastname, birth_date, role, company_id, id) VALUES ('user4','Тест2', 'Тест', '09/16/1989', NULL, 3,4);
INSERT INTO data_jpa.users(username, firstname, lastname, birth_date, role, company_id, id) VALUES ('user5','Тест3', 'Тест', '09/17/1989', NULL, 4,5);
INSERT INTO data_jpa.users(username, firstname, lastname, birth_date, role, company_id, id) VALUES ('user6','Тест4', 'Тест', '09/12/1989', NULL, 5,6);
SELECT SETVAL('data_jpa.users_id_seq', (SELECT MAX(id) FROM data_jpa.users));
-- rollback TRUNCATE TABLE data_jpa.users;

--changeset aslisarenko:4
INSERT INTO data_jpa.payment(amount, receiver_id) VALUES (200,1);
INSERT INTO data_jpa.payment(amount, receiver_id) VALUES (200,2);
INSERT INTO data_jpa.payment(amount, receiver_id) VALUES (200,3);
INSERT INTO data_jpa.payment(amount, receiver_id) VALUES (200,4);
INSERT INTO data_jpa.payment(amount, receiver_id) VALUES (200,5);
INSERT INTO data_jpa.payment(amount, receiver_id) VALUES (200,6);
-- rollback TRUNCATE TABLE data_jpa.payment;

--changeset aslisarenko:5
INSERT INTO data_jpa.chat(name, id) VALUES ('работа', 1);
INSERT INTO data_jpa.chat(name, id) VALUES ('учеба',2);
INSERT INTO data_jpa.chat(name, id) VALUES ('детский сад',3);
SELECT SETVAL('data_jpa.chat_id_seq', (SELECT MAX(id) FROM data_jpa.chat));
-- rollback TRUNCATE TABLE data_jpa.chat;

--changeset aslisarenko:6
INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (1,1);
INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (1,2);
INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (2,2);
INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (2,3);
INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (3,1);
INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (3,1);
INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (3,1);
INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (6,1);
INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (6,2);
-- rollback TRUNCATE TABLE data_jpa.users_chat;

--changeset aslisarenko:7
INSERT INTO data_jpa.passwords(user_id, password) VALUES (1, 'pass123');
-- rollback TRUNCATE TABLE data_jpa.passwords;


