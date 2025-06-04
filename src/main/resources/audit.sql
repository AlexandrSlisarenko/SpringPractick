ALTER TABLE data_jpa.users
    ADD COLUMN created_at TIMESTAMP;

ALTER TABLE data_jpa.users
    ADD COLUMN modified_at TIMESTAMP;

ALTER TABLE data_jpa.users
    ADD COLUMN created_by varchar(100);

ALTER TABLE data_jpa.users
    ADD COLUMN modified_by varchar(100);



