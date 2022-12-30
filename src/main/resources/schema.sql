DROP TABLE IF EXISTS contribution_category CASCADE;
CREATE TABLE contribution_category(
    id SERIAL,
    name varchar(20),
    points int,
    description varchar(64),
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS profiles CASCADE;
CREATE TABLE profiles(
    id SERIAL,
    profile varchar(64),
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS experience CASCADE;
CREATE TABLE experience(
    id SERIAL,
    level varchar(64),
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS technologies CASCADE;
CREATE TABLE technologies(
  id SERIAL,
  technology varchar(64),
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS month CASCADE;
CREATE TABLE month(
    id SERIAL,
    month varchar(64),
    PRIMARY KEY (id)

);

DROP TABLE IF EXISTS event_type CASCADE;
CREATE TABLE event_type(
    id SERIAL,
    event_type varchar(64),
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS admin CASCADE;
CREATE TABLE admin(
    id varchar(64),
    PRIMARY KEY (id)
);