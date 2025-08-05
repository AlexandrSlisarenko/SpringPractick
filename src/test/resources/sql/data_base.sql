CREATE SCHEMA IF NOT EXISTS data_jpa;

CREATE TABLE IF NOT EXISTS data_jpa.company(id BIGSERIAL PRIMARY KEY,name VARCHAR(64) NOT NULL UNIQUE);

CREATE TABLE IF NOT EXISTS data_jpa.company_locales(id BIGSERIAL PRIMARY KEY,company_id BIGINT NOT NULL,lang VARCHAR(2),description VARCHAR(128),FOREIGN KEY (company_id) REFERENCES data_jpa.company (id));

CREATE TABLE IF NOT EXISTS data_jpa.users(id BIGSERIAL PRIMARY KEY,username VARCHAR(64),firstname varchar(64),lastname VARCHAR(64),birth_date DATE,role VARCHAR(32),company_id BIGINT,FOREIGN KEY (company_id) REFERENCES data_jpa.company (id));

CREATE TABLE IF NOT EXISTS data_jpa.payment(id BIGSERIAL PRIMARY KEY,amount INTEGER,receiver_id BIGINT,FOREIGN KEY (receiver_id) REFERENCES data_jpa.users (id));

CREATE TABLE IF NOT EXISTS data_jpa.chat(id BIGSERIAL PRIMARY KEY,name VARCHAR(64) NOT NULL UNIQUE);

CREATE TABLE IF NOT EXISTS data_jpa.users_chat(id BIGSERIAL PRIMARY KEY,user_id BIGINT REFERENCES data_jpa.users (id),chat_id BIGINT REFERENCES data_jpa.chat (id));

CREATE TABLE IF NOT EXISTS data_jpa.passwords(id BIGSERIAL PRIMARY KEY,user_id BIGINT REFERENCES data_jpa.users (id),password varchar(200));

CREATE TABLE IF NOT EXISTS data_jpa.deactivated_token(id uuid PRIMARY KEY,c_keep_until TIMESTAMP NOT NULL check ( c_keep_until > now() ));

ALTER TABLE data_jpa.users ADD CONSTRAINT unique_username UNIQUE (username);



