## Thought process

The initial requirement only talks about users and books so the focus is on having books and users only.

There is a chance that in future the requirement may arise of having books viewed by authors in which case the book
table will have to be split and another author table will have to be added. Considering this, the current design isn't
the best but for the sake of convenience, this liberty has been taken to ignore the future requirement.

The tables below are mapped to JPA objects of same name in the project

## Tables

1. **book**  
   Contains the detail of all available books
2. **app_user**
   1. Choice of name because user is system table in postgres
   2. Will contain detail about all the users of the library
3. **issue**  
   This table will contain references to the books a particular user has borrowed. This table will also help in
   identifying how many copies of a book are left.

## Table Schema
1. **book**
   ```
   create table book
   (
   book_id       bigserial
   constraint book_pkey
   primary key,
   author_name   varchar(255),
   name          varchar(255) not null,
   num_of_copies integer      not null
   );
   ```
2. **app_user**
    ```
   create table app_user
   (
   user_id    bigserial
   constraint app_user_pkey
   primary key,
   first_name varchar(255) not null,
   last_name  varchar(255) not null
   );
   ```

3. **issue**
    ```
   create table issue
   (
   id      bigserial
   constraint issue_pkey
   primary key,
   book_id bigint not null
   constraint fkdd74m7xyg0vx5f9dps7lfv2sv
   references book,
   user_id bigint not null
   constraint fk8ypwg7wcvyivls5tsbtmh2st1
   references app_user,
   constraint user_borrowed_book_pk
   unique (user_id, book_id)
   );
   ```
