--liquibase formatted sql

--changeset aslisarenko:1
CREATE SCHEMA IF NOT EXISTS data_jpa;
--rollback DROP SCHEMA IF EXISTS data_jpa;

--changeset aslisarenko:2
CREATE TABLE IF NOT EXISTS data_jpa.company
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);
--rollback DROP TABLE IF EXISTS data_jpa.company;

--changeset aslisarenko:3
CREATE TABLE IF NOT EXISTS data_jpa.company_locales
(
    id          BIGSERIAL PRIMARY KEY,
    company_id  BIGINT NOT NULL,
    lang        VARCHAR(2),
    description VARCHAR(128),
    FOREIGN KEY (company_id) REFERENCES data_jpa.company (id)
);
--rollback DROP TABLE IF EXISTS data_jpa.company_locales;

--changeset aslisarenko:4
CREATE TABLE IF NOT EXISTS data_jpa.users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(64) UNIQUE,
    firstname  varchar(64),
    lastname   VARCHAR(64),
    birth_date DATE,
    role       VARCHAR(32),
    company_id BIGINT,
    FOREIGN KEY (company_id) REFERENCES data_jpa.company (id)
);
--rollback DROP TABLE IF EXISTS data_jpa.users;

--changeset aslisarenko:5
CREATE TABLE IF NOT EXISTS data_jpa.payment
(
    id          BIGSERIAL PRIMARY KEY,
    amount      INTEGER,
    receiver_id BIGINT,
    FOREIGN KEY (receiver_id) REFERENCES data_jpa.users (id)
);
--rollback DROP TABLE IF EXISTS data_jpa.payment;

--changeset aslisarenko:6
CREATE TABLE IF NOT EXISTS data_jpa.chat
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);
--rollback DROP TABLE IF EXISTS data_jpa.chat;

--changeset aslisarenko:7
CREATE TABLE IF NOT EXISTS data_jpa.users_chat
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES data_jpa.users (id),
    chat_id BIGINT REFERENCES data_jpa.chat (id)
);
--rollback DROP TABLE IF EXISTS data_jpa.users_chat;

--changeset aslisarenko:8
CREATE TABLE IF NOT EXISTS data_jpa.passwords
(
    id       BIGSERIAL PRIMARY KEY,
    user_id  BIGINT REFERENCES data_jpa.users (id),
    password varchar(200)
);
--rollback DROP TABLE IF EXISTS data_jpa.passwords;

--changeset aslisarenko:9
CREATE TABLE IF NOT EXISTS data_jpa.deactivated_token
(
    id           uuid PRIMARY KEY,
    c_keep_until TIMESTAMP NOT NULL check ( c_keep_until > now() )
);
--rollback DROP TABLE IF EXISTS data_jpa.deactivated_token;


