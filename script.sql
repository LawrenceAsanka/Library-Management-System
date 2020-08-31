create table Book
(
    book_id      varchar(20)               not null
        primary key,
    isbn         varchar(20)               not null,
    title        varchar(20)               not null,
    author       varchar(50)               not null,
    price        double(10, 2)             not null,
    availability varchar(10) default 'YES' not null
);

create table Member
(
    member_id varchar(20) not null
        primary key,
    nic       varchar(20) null,
    name      varchar(20) not null,
    address   varchar(50) not null,
    contact   varchar(20) not null
);

create table Issue
(
    member_id varchar(20)                  null,
    book_id   varchar(20)                  null,
    issue_id  int auto_increment
        primary key,
    date      date                         not null,
    detail    varchar(10) default 'ISSUED' not null,
    constraint Issue_ibfk_1
        foreign key (member_id) references Member (member_id),
    constraint Issue_ibfk_2
        foreign key (book_id) references Book (book_id)
);

create index book_id
    on Issue (book_id);

create index member_id
    on Issue (member_id);

create table `Return`
(
    date     date                      not null,
    issue_id int                       not null,
    fine_fee double(5, 2) default 0.00 not null,
    primary key (date, issue_id),
    constraint Return_ibfk_1
        foreign key (issue_id) references Issue (issue_id)
);

create index issue_id
    on `Return` (issue_id);

create table User
(
    user_id  int auto_increment
        primary key,
    name     varchar(20) not null,
    password varchar(10) not null
);


