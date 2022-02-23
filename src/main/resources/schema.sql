SET FOREIGN_KEY_CHECKS = 0;

DROP TRIGGER IF EXISTS sensor.trig_sensor_last_capture_date;
DROP VIEW IF EXISTS view_access_user_sensor;

DROP TABLE IF EXISTS hibernate_sequence;
DROP TABLE IF EXISTS parameter;
DROP TABLE IF EXISTS parameter_metadata;
DROP TABLE IF EXISTS sensor;
DROP TABLE IF EXISTS sensor_metadata;
DROP TABLE IF EXISTS sensored_bulk_data;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS access_user;
DROP TABLE IF EXISTS access_sensor;
DROP TABLE IF EXISTS access;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE hibernate_sequence (next_val BIGINT) engine = InnoDB;

INSERT INTO
    hibernate_sequence
VALUES
    (1);

CREATE TABLE parameter (
    id BIGINT NOT NULL,
    capture_date DATETIME,
    data LONGBLOB,
    parameter_metadata_id BIGINT NOT NULL,
    sensor_id BIGINT NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB;

CREATE TABLE parameter_metadata (
    id BIGINT NOT NULL,
    data_type INTEGER,
    depreciated_parameter INTEGER,
    icon VARCHAR(255),
    idx INTEGER,
    name VARCHAR(255),
    sensor_metadata_id BIGINT NOT NULL,
    unit VARCHAR(255),
    PRIMARY KEY (id)
) engine = InnoDB;

CREATE TABLE sensor (
    id BIGINT NOT NULL,
    display_name VARCHAR(255),
    hidden_message VARCHAR(255),
    is_hidden BIT,
    last_capture_date DATETIME,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    name VARCHAR(255) UNIQUE,
    sensor_metadata_id BIGINT NOT NULL,
    sub_display_name VARCHAR(255),
    notifier BIT NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
) engine = InnoDB;

CREATE TABLE sensored_bulk_data (
    sensored_bulk_data_id BIGINT NOT NULL,
    capture_date DATETIME,
    contents VARCHAR(255),
    receiving_date DATETIME,
    token BIGINT NOT NULL,
    sensor_id BIGINT NOT NULL,
    PRIMARY KEY (sensored_bulk_data_id)
) engine = InnoDB;

CREATE TABLE sensor_metadata (
    id BIGINT NOT NULL,
    data_separator VARCHAR(255),
    description VARCHAR(255),
    name VARCHAR(255),
    version VARCHAR(255),
    PRIMARY KEY (id)
) engine = InnoDB;

CREATE TABLE user (
    id BIGINT NOT NULL,
    firstname VARCHAR(60),
    last_name VARCHAR(60),
    mail VARCHAR(200),
    password VARCHAR(255),
    privilege INTEGER DEFAULT 1 NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB;

CREATE TABLE access (
    id BIGINT NOT NULL,
    category INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) engine=InnoDB;

CREATE TABLE access_sensor (
    access_id BIGINT NOT NULL,
    sensor_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (access_id, sensor_id)
) engine=InnoDB;

CREATE TABLE access_user (
    access_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    privilege INTEGER NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (access_id, user_id)
) engine=InnoDB;


ALTER TABLE
    parameter_metadata
ADD
    CONSTRAINT cu_pmd_on_smd_idx UNIQUE (sensor_metadata_id, idx);

ALTER TABLE
    user
ADD
    CONSTRAINT cu_user_on_mail UNIQUE (mail);

ALTER TABLE
    parameter
ADD
    CONSTRAINT fk_p_to_pmd FOREIGN KEY (parameter_metadata_id) REFERENCES parameter_metadata (id);

ALTER TABLE
    parameter
ADD
    CONSTRAINT fk_p_to_s FOREIGN KEY (sensor_id) REFERENCES sensor (id);

ALTER TABLE
    parameter_metadata
ADD
    CONSTRAINT fk_pmd_to_smd FOREIGN KEY (sensor_metadata_id) REFERENCES sensor_metadata (id);

ALTER TABLE
    sensor
ADD
    CONSTRAINT fk_s_to_smd FOREIGN KEY (sensor_metadata_id) REFERENCES sensor_metadata (id);

ALTER TABLE
    sensored_bulk_data
ADD
    CONSTRAINT fk_sbd_to_s FOREIGN KEY (sensor_id) REFERENCES sensor (id);


ALTER TABLE
    access_sensor
ADD
    CONSTRAINT fk_as_to_s FOREIGN KEY (sensor_id) REFERENCES sensor (id);

ALTER TABLE
    access_sensor
ADD
    CONSTRAINT fk_as_to_a FOREIGN KEY (access_id) REFERENCES access (id);

ALTER TABLE
    access_user
ADD
    CONSTRAINT fk_au_to_u FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE
    access_user
ADD
    CONSTRAINT fk_au_to_a FOREIGN KEY (access_id) REFERENCES access (id);

CREATE TRIGGER trig_sensor_last_capture_date AFTER INSERT ON parameter
    FOR EACH ROW
    UPDATE sensor
        SET last_capture_date = NEW.capture_date, notifier = true
        WHERE id = NEW.sensor_id AND (last_capture_date < NEW.capture_date OR last_capture_date IS NULL);


-- CREATE OR REPLACE VIEW view_access_user_sensor AS
--    SELECT DISTINCT user_id, sensor_id FROM (
--        SELECT acu.user_id, acs.sensor_id FROM access ac
--            JOIN access_sensor acs ON (acs.access_id = ac.id)
--            JOIN access_user acu ON (acu.access_id = ac.id)
--        UNION
--        SELECT ac.owner_id as user_id, acs.sensor_id FROM access ac
--            JOIN access_sensor acs ON (acs.access_id = ac.id)
--        UNION
--        SELECT u.id as user_id, acs.sensor_id FROM access ac
--            JOIN access_sensor acs ON (acs.access_id = ac.id)
--            CROSS JOIN user u
--            WHERE ac.pub = 1
--    ) as t;
