create table swaggertask.customers(
id uuid primary key,
title varchar(255) not null,
is_deleted boolean not null,
created_at timestamp not null,
modified_at timestamp
)