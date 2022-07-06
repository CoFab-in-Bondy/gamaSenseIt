SET FOREIGN_KEY_CHECKS = 0;

DROP TRIGGER IF EXISTS sensor.trig_sensor_last_capture_date;
DROP EVENT IF EXISTS remove_expired_refresh_token;
DROP VIEW IF EXISTS view_access_user_sensor;

DROP TABLE IF EXISTS hibernate_sequence;
DROP TABLE IF EXISTS interact_user;
DROP TABLE IF EXISTS interact_sensor;
DROP TABLE IF EXISTS interact_access;
DROP TABLE IF EXISTS parameter;
DROP TABLE IF EXISTS parameter_metadata;
DROP TABLE IF EXISTS sensor;
DROP TABLE IF EXISTS sensor_metadata;
DROP TABLE IF EXISTS sensored_bulk_data;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS refresh_token;
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
    id BIGINT NOT NULL AUTO_INCREMENT,
    capture_date DATETIME,
    data LONGBLOB,
    parameter_metadata_id BIGINT NOT NULL,
    sensor_id BIGINT NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB;

CREATE TABLE parameter_metadata (
    id BIGINT NOT NULL AUTO_INCREMENT,
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
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    hidden_message VARCHAR(255),
    is_hidden BIT,
    last_capture_date DATETIME,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    token VARCHAR(255) UNIQUE,
    sensor_metadata_id BIGINT NOT NULL,
    indications VARCHAR(255),
    notified BIT NOT NULL DEFAULT TRUE,
    photo MEDIUMBLOB,
    description TEXT,
    maintenance_description TEXT,
    PRIMARY KEY (id)
) engine = InnoDB;

CREATE TABLE sensored_bulk_data (
    sensored_bulk_data_id BIGINT NOT NULL AUTO_INCREMENT,
    capture_date DATETIME,
    contents VARCHAR(255),
    receiving_date DATETIME,
    token BIGINT NOT NULL,
    sensor_id BIGINT NOT NULL,
    PRIMARY KEY (sensored_bulk_data_id)
) engine = InnoDB;

CREATE TABLE sensor_metadata (
    id BIGINT NOT NULL AUTO_INCREMENT,
    data_separator VARCHAR(255) DEFAULT ':',
    description VARCHAR(255) DEFAULT '',
    name VARCHAR(255) NOT NULL,
    version VARCHAR(255) NOT NULL DEFAULT 'v0.0.1',
    icon MEDIUMBLOB,
    PRIMARY KEY (id)
) engine = InnoDB;

CREATE TABLE user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    firstname VARCHAR(60) NOT NULL,
    lastname VARCHAR(60) NOT NULL,
    mail VARCHAR(200) NOT NULL,
    password VARCHAR(255) NOT NULL,
    privilege INTEGER DEFAULT 1 NOT NULL,
    PRIMARY KEY (id)
) engine = InnoDB;

CREATE TABLE refresh_token (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL,
    issued_at DATETIME NOT NULL,
    expiration DATETIME NOT NULL,
    PRIMARY KEY (id)
)engine = InnoDB;

CREATE TABLE access (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    privilege INTEGER NOT NULL,
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

CREATE TABLE interact_user (
    user_id BIGINT NOT NULL,
    target_id BIGINT NOT NULL,
    date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id, target_id)
) engine=InnoDB;

CREATE TABLE interact_sensor (
    user_id BIGINT NOT NULL,
    sensor_id BIGINT NOT NULL,
    date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id, sensor_id)
) engine=InnoDB;

CREATE TABLE interact_access (
     user_id BIGINT NOT NULL,
     access_id BIGINT NOT NULL,
     date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
     PRIMARY KEY (user_id, access_id)
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
    refresh_token
ADD
    CONSTRAINT fk_rf_to_u FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE
    refresh_token
ADD
    CONSTRAINT cu_rf_on_token UNIQUE (token);

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

ALTER TABLE
    interact_sensor
ADD
    CONSTRAINT fk_is_to_u FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE
    interact_sensor
ADD
    CONSTRAINT fk_is_to_s FOREIGN KEY (sensor_id) REFERENCES sensor (id);

ALTER TABLE
    interact_user
ADD
    CONSTRAINT fk_iu_to_u FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE
    interact_user
ADD
    CONSTRAINT fk_iu_to_t FOREIGN KEY (target_id) REFERENCES user (id);

ALTER TABLE
    interact_access
    ADD
        CONSTRAINT fk_ia_to_u FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE
    interact_access
    ADD
        CONSTRAINT fk_ia_to_t FOREIGN KEY (access_id) REFERENCES access (id);


CREATE TRIGGER trig_sensor_last_capture_date AFTER INSERT ON parameter
    FOR EACH ROW
    UPDATE sensor
    SET last_capture_date = NEW.capture_date, notified = (NEW.capture_date > DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 day))
    WHERE id = NEW.sensor_id AND (last_capture_date < NEW.capture_date OR last_capture_date IS NULL);

CREATE EVENT remove_expired_refresh_token
    ON SCHEDULE EVERY 1 HOUR DO
    DELETE FROM refresh_token
        WHERE expiration < DATE_ADD(NOW(), INTERVAL 1 DAY); -- Add interval for prevent timezone differance

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
