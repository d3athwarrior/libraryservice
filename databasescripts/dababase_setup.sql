create table app_user
(
    user_id    bigserial
        constraint app_user_pkey
            primary key,
    first_name varchar(255) not null,
    last_name  varchar(255) not null
);

create table book
(
    book_id       bigserial
        constraint book_pkey
            primary key,
    author_name   varchar(255),
    name          varchar(255) not null,
    num_of_copies integer      not null
);

create table issue
(
    id      bigserial
        constraint issue_pkey
            primary key,
    book_id bigint not null
        constraint book_book_id_fk
            references book,
    user_id bigint not null
        constraint app_user_user_id_fk
            references app_user,
    constraint user_borrowed_book_pk
        unique (user_id, book_id)
);

