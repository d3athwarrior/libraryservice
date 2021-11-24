## Thought process
The initial requirement only talks about users and books so the focus is on having books and users only.

There is a chance that in future the requirement may arise of having books viewed by authors in which case the book table will have to be split and another author table will have to be added. Considering this, the current design isn't the best but for the sake of convenience, this liberty has been taken to ignore the future requirement.

## Tables
1. **book**  
   Contains the detail of all available books
2. **app_user**  
   1. Choice of name because user is system table in postgres
   2. Will contain detail about all the users of the library
3. **user_borrowed_book**  
   This table will contain references to the books a particular user has borrowed. This table will also help in identifying how many copies of a book are left.

## Table Schema
1. **book**
   ```
   create table book
   (
   id            bigint       not null
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
    user_id    bigint default nextval('user_user_id_seq'::regclass) not null
    constraint user_pk
    primary key,
    first_name varchar                                              not null,
    last_name  varchar                                              not null
    );
   ```

3. **user_borrowed_book**
    ```
   create table user_borrowed_book
    (
    user_id bigint not null
    constraint user_borrowed_book_book_id_fk
    references book
    on update cascade on delete cascade
    constraint user_borrowed_book_user_user_id_fk
    references app_user
    on update cascade on delete cascade,
    book_id bigint not null,
    constraint user_borrowed_book_pk
    primary key (user_id, book_id)
    );
   ```