# --- !Ups

create table parent (
  id                        bigint auto_increment not null PRIMARY KEY,
  name                      varchar(255) not null,
  gender                    varchar(255) not null,
  birth_date                date not null,
  create_date               datetime not null,
  update_date               datetime not null)
 engine=InnoDB;

create table child (
  id                        bigint auto_increment not null PRIMARY KEY,
  name                      varchar(255) not null,
  parent_id                 bigint not null,
  create_date               datetime not null,
  update_date               datetime not null)
 engine=InnoDB;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table child;

drop table parent;

SET FOREIGN_KEY_CHECKS=1;

