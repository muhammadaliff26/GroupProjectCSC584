-- =========================================================
-- Uni-Run: Marathon & Virtual Run Coordinator
-- Database: JavaDB (Apache Derby) - Database name: UniRunDB
-- Run these statements in NetBeans: Services > Databases > Java DB
--   1) Right click "Java DB" > Create Database... name it UniRunDB
--      user: app   password: app
--   2) Connect to jdbc:derby://localhost:1527/UniRunDB
--   3) Right click connection > Execute Command... paste this whole file > Run
-- =========================================================

-- Drop tables if they already exist (ignore errors on first run)
-- DROP TABLE REGISTRATIONS;
-- DROP TABLE RUN_EVENTS;
-- DROP TABLE CATEGORIES;
-- DROP TABLE USERS;

-- =========================================================
-- ALREADY HAVE THE DATABASE SET UP? Just run this one line
-- instead of recreating everything, to add the new column
-- used for recording a runner's finish time:
--
--   ALTER TABLE REGISTRATIONS ADD COLUMN FINISH_TIME VARCHAR(20);
--
-- =========================================================

CREATE TABLE USERS (
    USER_ID        INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    USERNAME       VARCHAR(50) NOT NULL,
    PASSWORD       VARCHAR(100) NOT NULL,
    FULL_NAME      VARCHAR(100) NOT NULL,
    EMAIL          VARCHAR(100) NOT NULL,
    PHONE          VARCHAR(20),
    ROLE           VARCHAR(20) NOT NULL DEFAULT 'PARTICIPANT',
    REGISTER_DATE  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (USER_ID),
    UNIQUE (USERNAME)
);

CREATE TABLE CATEGORIES (
    CATEGORY_ID    INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    CATEGORY_NAME  VARCHAR(50) NOT NULL,
    DISTANCE_KM    DOUBLE NOT NULL,
    FEE            DOUBLE NOT NULL,
    PRIMARY KEY (CATEGORY_ID)
);

CREATE TABLE RUN_EVENTS (
    EVENT_ID       INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    EVENT_NAME     VARCHAR(100) NOT NULL,
    EVENT_DATE     DATE NOT NULL,
    LOCATION       VARCHAR(150) NOT NULL,
    CATEGORY_ID    INTEGER NOT NULL,
    DESCRIPTION    VARCHAR(1000),
    STATUS         VARCHAR(20) DEFAULT 'UPCOMING',
    CREATED_BY     INTEGER NOT NULL,
    PRIMARY KEY (EVENT_ID),
    FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORIES(CATEGORY_ID),
    FOREIGN KEY (CREATED_BY) REFERENCES USERS(USER_ID)
);

CREATE TABLE REGISTRATIONS (
    REGISTRATION_ID   INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    USER_ID           INTEGER NOT NULL,
    EVENT_ID          INTEGER NOT NULL,
    BIB_NUMBER        VARCHAR(20) NOT NULL,
    REGISTRATION_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    STATUS            VARCHAR(20) DEFAULT 'PENDING',
    FINISH_TIME       VARCHAR(20),
    PRIMARY KEY (REGISTRATION_ID),
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID),
    FOREIGN KEY (EVENT_ID) REFERENCES RUN_EVENTS(EVENT_ID)
);

-- Sample seed data
INSERT INTO USERS (USERNAME, PASSWORD, FULL_NAME, EMAIL, PHONE, ROLE) VALUES
('admin', 'admin123', 'System Administrator', 'admin@unirun.com', '0123456789', 'ADMIN'),
('john', 'john123', 'John Tan', 'john@mail.com', '0111234567', 'PARTICIPANT');

INSERT INTO CATEGORIES (CATEGORY_NAME, DISTANCE_KM, FEE) VALUES
('Fun Run', 5.0, 20.00),
('Half Marathon', 21.1, 45.00),
('Full Marathon', 42.2, 80.00),
('Virtual Run', 10.0, 15.00);

INSERT INTO RUN_EVENTS (EVENT_NAME, EVENT_DATE, LOCATION, CATEGORY_ID, DESCRIPTION, STATUS, CREATED_BY) VALUES
('Uni-Run Campus Fun Run 2026', '2026-08-15', 'University Main Stadium', 1, 'A fun 5km run open to all students and staff.', 'UPCOMING', 1),
('Uni-Run Virtual Marathon 2026', '2026-09-01', 'Virtual (Run Anywhere)', 4, 'Run 10km anywhere and submit your time via the app.', 'UPCOMING', 1);
