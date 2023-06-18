create schema legacy;

grant all on schema legacy to legacy_db_owner;

alter user legacy_user set search_path = legacy;
