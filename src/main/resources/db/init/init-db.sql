create tablespace legacy_db location 'd:/pgsql/data/legacy_db';

create database legacy_db with tablespace = legacy_db;
