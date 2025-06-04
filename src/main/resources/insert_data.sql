INSERT INTO data_jpa.company(name) VALUES ('Cinimex');
INSERT INTO data_jpa.company(name) VALUES ('Demis Group');
INSERT INTO data_jpa.company(name) VALUES ('Avikey');
INSERT INTO data_jpa.company(name) VALUES ('SEO');
INSERT INTO data_jpa.company(name) VALUES ('ALT');

INSERT INTO data_jpa.company_locales(company_id, lang, description) VALUES (1,'RU', 'Last company');
INSERT INTO data_jpa.company_locales(company_id, lang, description) VALUES (2,'EN', 'WEB company');
INSERT INTO data_jpa.company_locales(company_id, lang, description) VALUES (3,'EN', 'WEB studio');
INSERT INTO data_jpa.company_locales(company_id, lang, description) VALUES (4,'RU', 'SEO studio');
INSERT INTO data_jpa.company_locales(company_id, lang, description) VALUES (5,'RU', 'DevOps studio');


INSERT INTO data_jpa.users(username, firstname, lastname, birth_date, role, company_id) VALUES ('ivanov_ivan','Иван', 'Иванов', '12/15/1989', NULL, 1);
INSERT INTO data_jpa.users(username, firstname, lastname, birth_date, role, company_id) VALUES ('petrov_petr','Петр', 'Петров', '05/17/1996', NULL, 1);
INSERT INTO data_jpa.users(username, firstname, lastname, birth_date, role, company_id) VALUES ('user3','Тест1', 'Тест', '09/15/1989', NULL, 2);
INSERT INTO data_jpa.users(username, firstname, lastname, birth_date, role, company_id) VALUES ('user4','Тест2', 'Тест', '09/16/1989', NULL, 3);
INSERT INTO data_jpa.users(username, firstname, lastname, birth_date, role, company_id) VALUES ('user5','Тест3', 'Тест', '09/17/1989', NULL, 4);
INSERT INTO data_jpa.users(username, firstname, lastname, birth_date, role, company_id) VALUES ('user6','Тест4', 'Тест', '09/12/1989', NULL, 5);


INSERT INTO data_jpa.payment(amount, receiver_id) VALUES (200,1);
INSERT INTO data_jpa.payment(amount, receiver_id) VALUES (200,2);
INSERT INTO data_jpa.payment(amount, receiver_id) VALUES (200,3);
INSERT INTO data_jpa.payment(amount, receiver_id) VALUES (200,4);
INSERT INTO data_jpa.payment(amount, receiver_id) VALUES (200,5);
INSERT INTO data_jpa.payment(amount, receiver_id) VALUES (200,6);


INSERT INTO data_jpa.chat(name) VALUES ('работа');
INSERT INTO data_jpa.chat(name) VALUES ('учеба');
INSERT INTO data_jpa.chat(name) VALUES ('детский сад');

INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (1,1);
INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (1,2);
INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (2,2);
INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (2,3);
INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (3,1);
INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (3,1);
INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (3,1);
INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (6,1);
INSERT INTO data_jpa.users_chat(user_id, chat_id) VALUES (6,2);

INSERT INTO data_jpa.passwords(user_id, password) VALUES (13, 'pass123');

select * from data_jpa.users



