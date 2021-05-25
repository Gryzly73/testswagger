create table swaggertask.products(
                                      id uuid primary key,
                                      customer_id uuid CONSTRAINT address_user_fk REFERENCES swaggertask.customers (id),
                                      title varchar(255) not null,
                                      description varchar(1024),
                                      price decimal not null,
                                      is_deleted boolean not null,
                                      created_at timestamp not null,
                                      modified_at timestamp
)