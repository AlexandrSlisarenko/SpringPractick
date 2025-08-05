--liquibase formatted sql

--changeset aslisarenko:1
ALTER TABLE data_jpa.users
    ADD COLUMN created_at TIMESTAMP;
-- rollback ALTER TABLE data_jpa.users DROP COLUMN created_at;

--changeset aslisarenko:2
ALTER TABLE data_jpa.users
    ADD COLUMN modified_at TIMESTAMP;
-- rollback ALTER TABLE data_jpa.users DROP COLUMN modified_at;

--changeset aslisarenko:3
ALTER TABLE data_jpa.users
    ADD COLUMN created_by varchar(100);
-- rollback ALTER TABLE data_jpa.users DROP COLUMN created_by;

--changeset aslisarenko:4
ALTER TABLE data_jpa.users
    ADD COLUMN modified_by varchar(100);
-- rollback ALTER TABLE data_jpa.users DROP COLUMN modified_by;

--changeset aslisarenko:5
CREATE TABLE data_jpa.revision
(
    id        SERIAL PRIMARY KEY ,
    timestamp BIGINT NOT NULL
);
--rollback DROP TABLE IF EXISTS data_jpa.revision;


--changeset aslisarenko:6
create table  data_jpa.users_aud
(
    id         bigint  not null,
    rev        integer not null constraint fkmrjb3nxent1mi8jjld588s7u6 references data_jpa.revision,
    revtype    smallint,
    birth_date date,
    firstname  varchar(255),
    lastname   varchar(255),
    role       varchar(255)
        constraint users_aud_role_check
            check ((role)::text = ANY ((ARRAY ['USER'::character varying, 'ADMIN'::character varying])::text[])),
    username   varchar(255),
    primary key (rev, id)
);
--rollback DROP TABLE IF EXISTS data_jpa.users_aud;