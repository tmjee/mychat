-- \c mychat  -- connect to mychat db
-- \du  -- list users
-- add user user1 with password 'password'  -- add user1 with password


-- TABLE MYCHAT_USER
DROP TABLE IF EXISTS MYCHAT_USER CASCADE;
DROP SEQUENCE IF EXISTS SEQ_MYCHAT_USER CASCADE;
CREATE SEQUENCE SEQ_MYCHAT_USER INCREMENT 1 MINVALUE 100 START 100;
CREATE TABLE IF NOT EXISTS MYCHAT_USER (
  MYCHAT_USER_ID INTEGER PRIMARY KEY DEFAULT NEXTVAL('SEQ_MYCHAT_USER'),
  IDENTIFICATION_TYPE VARCHAR(50) NOT NULL, -- EMAIL or PHONE_NUMBER
  IDENTIFICATION VARCHAR(50) NOT NULL, -- ACTUAL EMAIL OR PHONE NUMBER
  PASSWORD VARCHAR(255),
  SALT VARCHAR(255),
  STATUS VARCHAR(50) NOT NULL,  -- ACTIVE, INACTIVE, PENDING, PENDING_EXPIRED
  ACTIVATION_TOKEN VARCHAR(100) NOT NULL, -- when PENDING can be used to activate user
  CREATION_DATE TIMESTAMP NOT NULL,
  MODIFICATION_DATE TIMESTAMP NOT NULL
);

-- TABLE CHAT
DROP TABLE IF EXISTS CHAT CASCADE;
DROP SEQUENCE IF EXISTS SEQ_CHAT CASCADE;
CREATE SEQUENCE SEQ_CHAT INCREMENT 1 MINVALUE 100 START 100;
CREATE TABLE IF NOT EXISTS CHAT (
  CHAT_ID INTEGER PRIMARY KEY DEFAULT NEXTVAL('SEQ_CHAT'),
  CREATOR_MYCHAT_USER_ID INTEGER REFERENCES MYCHAT_USER(MYCHAT_USER_ID),
  NAME VARCHAR(200),
  CREATION_DATE TIMESTAMP NOT NULL
);

-- TABLE CHAT_MEMBERS
DROP TABLE IF EXISTS CHAT_MEMBER CASCADE;
DROP SEQUENCE IF EXISTS SEQ_CHAT_MEMBER CASCADE;
CREATE SEQUENCE SEQ_CHAT_MEMBER INCREMENT 1 MINVALUE 100 START 100;
CREATE TABLE IF NOT EXISTS CHAT_MEMBER (
  CHAT_MEMBER_ID INTEGER PRIMARY KEY DEFAULT NEXTVAL('SEQ_CHAT_MEMBER'),
  CHAT_MEMBER_MYCHAT_USER_ID INTEGER REFERENCES MYCHAT_USER(MYCHAT_USER_ID),
  CHAT_ID INTEGER REFERENCES CHAT(CHAT_ID),
  STATUS VARCHAR(50) NOT NULL   -- join / left
);

-- TABLE CHAT_MESSAGES
DROP TABLE IF EXISTS CHAT_MESSAGE CASCADE;
DROP SEQUENCE IF EXISTS SEQ_CHAT_MESSAGE CASCADE;
CREATE SEQUENCE SEQ_CHAT_MESSAGE INCREMENT 1 MINVALUE 100 START 100;
CREATE TABLE IF NOT EXISTS CHAT_MESSAGE (
  CHAT_MESSAGE_ID INTEGER PRIMARY KEY DEFAULT NEXTVAL('SEQ_CHAT_MESSAGE'),
  CHAT_MEMBER_ID INTEGER REFERENCES CHAT_MEMBER(CHAT_MEMBER_ID),
  CHAT_ID INTEGER REFERENCES CHAT(CHAT_ID),
  CHAT_MESSAGE VARCHAR(255) NOT NULL,
  CREATION_DATE TIMESTAMP NOT NULL
);


-- TABLE CONTACTS
DROP TABLE IF EXISTS CONTACT CASCADE;
DROP SEQUENCE IF EXISTS SEQ_CONTACT CASCADE;
CREATE SEQUENCE SEQ_CONTACT INCREMENT 1 MINVALUE 100 START 100;
CREATE TABLE IF NOT EXISTS CONTACT (
  CONTACT_ID INTEGER PRIMARY KEY DEFAULT NEXTVAL('SEQ_CONTACT'),
  MYCHAT_USER_ID INTEGER REFERENCES MYCHAT_USER(MYCHAT_USER_ID),
  CONTACT_MYCHAT_USER_ID INTEGER REFERENCES MYCHAT_USER(MYCHAT_USER_ID),
  STATUS VARCHAR(30) NOT NULL,  -- PENDING_CONFIRMATION, PENDING_ACCEPTANCE, ACCEPTED
  CREATION_DATE TIMESTAMP NOT NULL
);

-- TABLE PROFILE
DROP TABLE IF EXISTS PROFILE CASCADE;
DROP SEQUENCE IF EXISTS SEQ_PROFILE CASCADE;
CREATE SEQUENCE SEQ_PROFILE INCREMENT 1 MINVALUE 100 START 100;
CREATE TABLE IF NOT EXISTS PROFILE (
  PROFILE_ID INTEGER PRIMARY KEY DEFAULT NEXTVAL('SEQ_PROFILE'),
  MYCHAT_USER_ID INTEGER REFERENCES  MYCHAT_USER(MYCHAT_USER_ID),
  WHATSUP VARCHAR(255),
  FULLNAME VARCHAR(255),
  GENDER VARCHAR(10),
  CREATION_DATE TIMESTAMP NOT NULL,
  MODIFICATION_DATE TIMESTAMP
);

-- TABLE AVATAR
DROP TABLE IF EXISTS AVATAR;
DROP SEQUENCE IF EXISTS SEQ_AVATAR;
CREATE SEQUENCE SEQ_AVATAR INCREMENT 1 MINVALUE 100 START 100;
CREATE TABLE IF NOT EXISTS AVATAR (
  AVATAR_ID INTEGER PRIMARY KEY DEFAULT NEXTVAL('SEQ_AVATAR'),
  MYCHAT_USER_ID INTEGER REFERENCES MYCHAT_USER(MYCHAT_USER_ID),
  BYTES BYTEA NOT NULL,
  ENCODED BYTEA NOT NULL,
  MIME_TYPE VARCHAR(10) NOT NULL,
  FILENAME VARCHAR(50) NOT NULL,
  CREATION_DATE TIMESTAMP NOT NULL,
  MODIFICATION_DATE TIMESTAMP
);


-- TABLE SETTINGS
DROP TABLE IF EXISTS SETTINGS;
DROP SEQUENCE IF EXISTS SEQ_SETTINGS;
CREATE SEQUENCE SEQ_SETTINGS INCREMENT 1 MINVALUE 100 START 100;
CREATE TABLE IF NOT EXISTS SETTINGS (
  SETTINGS_ID INTEGER PRIMARY KEY DEFAULT NEXTVAL('SEQ_SETTINGS'),
  MYCHAT_USER_ID INTEGER REFERENCES MYCHAT_USER(MYCHAT_USER_ID),
  NOTIFICATION VARCHAR(10),
  CREATION_DATE TIMESTAMP NOT NULL,
  MODIFICATION_DATE TIMESTAMP
);


-- TABLE CHANNEL
DROP TABLE IF EXISTS CHANNEL CASCADE;
DROP SEQUENCE IF EXISTS SEQ_CHANNEL CASCADE;
CREATE SEQUENCE SEQ_CHANNEL INCREMENT 1 MINVALUE 100 START 100;
CREATE TABLE IF NOT EXISTS CHANNEL (
  CHANNEL_ID INTEGER PRIMARY KEY DEFAULT NEXTVAL('SEQ_CHANNEL'),
  MYCHAT_USER_ID INTEGER REFERENCES MYCHAT_USER(MYCHAT_USER_ID),
  TYPE VARCHAR(20),       -- facebook, twitter, mychat etc.
  TYPE_ID VARCHAR(20),    -- facebook id, twitter id, mychat id etc.
  CREATION_DATE TIMESTAMP NOT NULL
);


-- TABLE LOG
DROP TABLE IF EXISTS LOG CASCADE;
DROP SEQUENCE IF EXISTS SEQ_LOG CASCADE;
CREATE SEQUENCE SEQ_LOG INCREMENT 1 MINVALUE 100 START 100;
CREATE TABLE IF NOT EXISTS LOG (
  LOG_ID INTEGER PRIMARY KEY DEFAULT NEXTVAL('SEQ_LOG'),
  LOG_TYPE VARCHAR(50) NOT NULL,
  CREATION_DATE TIMESTAMP NOT NULL,
  LOG_TYPE_ID INTEGER NOT NULL,
  LOG_MESSAGE VARCHAR(100) NOT NULL
);


-- TABLE APPLICATION_TOKEN
DROP TABLE IF EXISTS APPLICATION_TOKEN CASCADE;
DROP SEQUENCE IF EXISTS SEQ_APPLICATION_TOKEN CASCADE;
CREATE SEQUENCE SEQ_APPLICATION_TOKEN INCREMENT 1 MINVALUE 100 START 100;
CREATE TABLE IF NOT EXISTS APPLICATION_TOKEN (
  APPLICATION_TOKEN_ID INTEGER PRIMARY KEY DEFAULT NEXTVAL('SEQ_APPLICATION_TOKEN'),
  NAME VARCHAR(50) NOT NULL,
  DESCRIPTION VARCHAR(100),
  APPLICATION_TOKEN VARCHAR(100) NOT NULL
);


-- TABLE ACCESS_TOKEN
DROP TABLE IF EXISTS ACCESS_TOKEN CASCADE;
DROP SEQUENCE IF EXISTS SEQ_ACCESS_TOKEN CASCADE;
CREATE SEQUENCE SEQ_ACCESS_TOKEN INCREMENT 1 MINVALUE 100 START 100;
CREATE TABLE IF NOT EXISTS ACCESS_TOKEN (
  ACCESS_TOKEN_ID INTEGER PRIMARY KEY DEFAULT NEXTVAL('SEQ_ACCESS_TOKEN'),
  MYCHAT_USER_ID INTEGER REFERENCES MYCHAT_USER(MYCHAT_USER_ID),
  ACCESS_TOKEN VARCHAR(100) NOT NULL,
  STATE VARCHAR(20) NOT NULL,  -- LOGOUT, ACTIVE, TIMEOUT
  CREATION_DATE TIMESTAMP NOT NULL,
  MODIFICATION_DATE TIMESTAMP
);


-- TABLE ROLE
DROP TABLE IF EXISTS ROLE CASCADE;
DROP SEQUENCE IF EXISTS SEQ_ROLE CASCADE;
CREATE SEQUENCE SEQ_ROLE INCREMENT 1 MINVALUE 100 START 100;
CREATE TABLE IF NOT EXISTS ROLE (
  ROLE_ID INTEGER PRIMARY KEY DEFAULT NEXTVAL('SEQ_ROLE'),
  MYCHAT_USER_ID INTEGER REFERENCES MYCHAT_USER(MYCHAT_USER_ID),
  ROLE VARCHAR(20) NOT NULL
);

-- TABLE LOCATION
DROP TABLE IF EXISTS LOCATION CASCADE;
DROP SEQUENCE IF EXISTS SEQ_LOCATION CASCADE;
CREATE SEQUENCE SEQ_LOCATION INCREMENT 1 MINVALUE 100 START 100;
CREATE TABLE IF NOT EXISTS LOCATION (
  LOCATION_ID INTEGER PRIMARY KEY DEFAULT NEXTVAL('SEQ_LOCATION'),
  MYCHAT_USER_ID INTEGER REFERENCES MYCHAT_USER(MYCHAT_USER_ID),
  NAME VARCHAR(100),
  LONGITUDE FLOAT NOT NULL,
  LATITUDE FLOAT NOT NULL
);


-- TABLE MOMENT
DROP TABLE IF EXISTS MOMENT CASCADE;
DROP SEQUENCE IF EXISTS SEQ_MOMENT CASCADE;
CREATE SEQUENCE SEQ_MOMENT INCREMENT 1 MINVALUE 100 START 100;
CREATE TABLE IF NOT EXISTS MOMENT (
  MOMENT_ID INTEGER PRIMARY KEY DEFAULT NEXTVAL('SEQ_MOMENT'),
  MYCHAT_USER_ID INTEGER REFERENCES MYCHAT_USER(MYCHAT_USER_ID),
  MESSAGE VARCHAR(1000),
  BYTES BYTEA,
  ENCODED BYTEA,
  MIME_TYPE VARCHAR(10),
  TYPE VARCHAR(10) NOT NULL, -- TEXT/IMAGE/AUDIO/VIDEO/UNKNOWN
  FILENAME VARCHAR(50),
  CREATION_DATE TIMESTAMP NOT NULL
);



