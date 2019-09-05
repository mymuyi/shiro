drop table if exists sys_user;
drop table if exists sys_role;
drop table if exists sys_permission;
drop table if exists sys_user_role;
drop table if exists sys_role_permission;

create table sys_user (
  id bigint auto_increment,
  username varchar(100),
  password varchar(100),
  salt varchar(100),
  locked bool default false,
  constraint pk_sys_users primary key(id)
) charset=utf8 ENGINE=InnoDB;
create unique index idx_sys_user_username on sys_user(username);

create table sys_role (
  id bigint auto_increment,
  role varchar(100),
  description varchar(100),
  available bool default false,
  constraint pk_sys_roles primary key(id)
) charset=utf8 ENGINE=InnoDB;
create unique index idx_sys_role_role on sys_role(role);

create table sys_permission (
  id bigint auto_increment,
  permission varchar(100),
  description varchar(100),
  available bool default false,
  constraint pk_sys_permission primary key(id)
) charset=utf8 ENGINE=InnoDB;
create unique index idx_sys_permission_permission on sys_permission(permission);

create table sys_user_role (
  user_id bigint,
  role_id bigint,
  constraint pk_sys_user_role primary key(user_id, role_id)
) charset=utf8 ENGINE=InnoDB;

create table sys_role_permission (
  role_id bigint,
  permission_id bigint,
  constraint pk_sys_role_permission primary key(role_id, permission_id)
) charset=utf8 ENGINE=InnoDB;
