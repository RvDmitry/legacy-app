create role legacy_db_owner;

create user legacy_user with password 'legacy_pass' in role legacy_db_owner;

grant connect on database legacy_db to legacy_user;
