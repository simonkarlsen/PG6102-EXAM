-- From https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/auth/src/main/resources/db/migration/V1.0__createDB.sql
create table users
(
    username varchar(50) not null primary key,
    password varchar(100) not null,
    enabled  boolean     not null
);
create table authorities
(
    username  varchar(50) not null,
    authority varchar(50) not null,
    constraint fk_authorities_users foreign key (username) references users (username)
);
create unique index ix_auth_username on authorities (username, authority);