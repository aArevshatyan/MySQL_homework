--postgres
drop table if exists users;
drop table if exists transactions;

create table users (
  id serial unique,
  first_name text not null,
  last_name text not null,
  balance real not null default 0
);

create table transactions (
  id serial unique,
  user_from int,
  user_to int,
  transaction_amount real not null,
  transaction_date timestamp not null default now()
);
-- mysql
create database paypal;
use paypal;
create table users(
  id int not null auto_increment,
  first_name varchar(255) not null,
  last_name varchar(255) not null,
  balance real not null default 0,
  primary key (id)
);


create table transactions (
  id int not null auto_increment,
  user_from int,
  user_to int,
  transaction_amount real not null,
  transaction_date timestamp not null default now(),
  primary key (id)
);