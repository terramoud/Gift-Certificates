-- -----------------------------------------------------
-- Schema gift_certificates
-- -----------------------------------------------------
DROP DATABASE IF EXISTS gift_certificates;
CREATE DATABASE IF NOT EXISTS gift_certificates DEFAULT CHARACTER SET utf8;
USE gift_certificates;


-- -----------------------------------------------------
-- Table certificate
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS certificate
(
    PRIMARY KEY (id),
    id               INT           NOT NULL AUTO_INCREMENT,
    name             VARCHAR(32)   NOT NULL UNIQUE,
    description      VARCHAR(256)  NOT NULL,
    price            DECIMAL(9, 2) NOT NULL,
    duration         INT UNSIGNED  NOT NULL,
    create_date      DATETIME      NOT NULL,
    last_update_date DATETIME      NOT NULL
);

CREATE TRIGGER do_immutable_create_date
    BEFORE UPDATE
    ON certificate
    FOR EACH ROW
BEGIN
    SET NEW.create_date = OLD.create_date;
END;


-- -----------------------------------------------------
-- Table tag
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS tag
(
    PRIMARY KEY (id),
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(32) NOT NULL UNIQUE
);


-- -----------------------------------------------------
-- Table certificates_tags
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS certificates_tags
(
    PRIMARY KEY (certificate_id, tag_id),
    certificate_id INT NOT NULL,
    tag_id         INT NOT NULL,
    FOREIGN KEY (certificate_id)
        REFERENCES certificate (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (tag_id)
        REFERENCES tag (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- fill certificate
-- -----------------------------------------------------
INSERT INTO certificate
VALUES (DEFAULT, 'english course', 'online english course', 999.99, 180, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO certificate
VALUES (DEFAULT, 'italian course', 'online italian course', 699.99, 120, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO certificate
VALUES (DEFAULT, 'Breakdancing course', 'Breakdancing Online course', 1099.99, 120, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- -----------------------------------------------------
-- fill tag
-- -----------------------------------------------------
INSERT INTO tag
VALUES (DEFAULT, 'language courses');
INSERT INTO tag
VALUES (DEFAULT, 'dancing courses');

-- -----------------------------------------------------
-- fill certificates_tags
-- -----------------------------------------------------
INSERT INTO certificates_tags VALUES (1, 1);
INSERT INTO certificates_tags VALUES (2, 1);
INSERT INTO certificates_tags VALUES (3, 2);
