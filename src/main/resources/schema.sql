SET FOREIGN_KEY_CHECKS = 0;

DROP TRIGGER IF EXISTS sensor.trig_sensor_last_capture_date;

DROP TABLE IF EXISTS hibernate_sequence;

DROP TABLE IF EXISTS parameter;

DROP TABLE IF EXISTS parameter_metadata;

DROP TABLE IF EXISTS role;

DROP TABLE IF EXISTS sensor;

DROP TABLE IF EXISTS sensored_bulk_data;

DROP TABLE IF EXISTS sensor_metadata;

DROP TABLE IF EXISTS user;

DROP TABLE IF EXISTS user_group;

DROP TABLE IF EXISTS user_group_roles;

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

CREATE TABLE role (
    role_id BIGINT NOT NULL,
    role INTEGER,
    user_id BIGINT,
    group_id BIGINT,
    PRIMARY KEY (role_id)
) engine = InnoDB;

CREATE TABLE sensor (
    id BIGINT NOT NULL,
    display_name VARCHAR(255),
    hidden_message VARCHAR(255),
    is_hidden BIT,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    name VARCHAR(255),
    sensor_metadata_id BIGINT NOT NULL,
    sub_display_name VARCHAR(255),
    last_capture_date DATETIME,
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
    id_user BIGINT NOT NULL,
    firstname VARCHAR(60),
    last_name VARCHAR(60),
    mail VARCHAR(200),
    password VARCHAR(255),
    privilege INTEGER,
    PRIMARY KEY (id_user)
) engine = InnoDB;

CREATE TABLE user_group (
    group_id BIGINT NOT NULL,
    name VARCHAR(60),
    PRIMARY KEY (group_id)
) engine = InnoDB;

CREATE TABLE user_group_roles (
    user_group_group_id BIGINT NOT NULL,
    roles_role_id BIGINT NOT NULL
) engine = InnoDB;

ALTER TABLE
    parameter_metadata
ADD
    CONSTRAINT cu_pmd_on_smd_idx UNIQUE (sensor_metadata_id, idx);

ALTER TABLE
    user
ADD
    CONSTRAINT cu_user_on_mail UNIQUE (mail);

ALTER TABLE
    user_group_roles
ADD
    CONSTRAINT cu_user_group_roles_roles_role_id UNIQUE (roles_role_id);

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
    role
ADD
    CONSTRAINT fk_role_to_user FOREIGN KEY (user_id) REFERENCES user (id_user);

ALTER TABLE
    role
ADD
    CONSTRAINT fk_role_to_group FOREIGN KEY (group_id) REFERENCES user_group (group_id);

ALTER TABLE
    sensor
ADD
    CONSTRAINT fk_s_to_smd FOREIGN KEY (sensor_metadata_id) REFERENCES sensor_metadata (id);

ALTER TABLE
    sensored_bulk_data
ADD
    CONSTRAINT fk_sbd_to_s FOREIGN KEY (sensor_id) REFERENCES sensor (id);

ALTER TABLE
    user_group_roles
ADD
    CONSTRAINT fk_user_group_roles_to_roles_role FOREIGN KEY (roles_role_id) REFERENCES role (role_id);

ALTER TABLE
    user_group_roles
ADD
    CONSTRAINT fk_user_group_roles_to_user_group FOREIGN KEY (user_group_group_id) REFERENCES user_group (group_id);

CREATE TRIGGER trig_sensor_last_capture_date AFTER INSERT ON parameter
    FOR EACH ROW
    UPDATE sensor
        SET last_capture_date = NEW.capture_date
        WHERE id = NEW.sensor_id AND (last_capture_date < NEW.capture_date OR last_capture_date IS NULL);
