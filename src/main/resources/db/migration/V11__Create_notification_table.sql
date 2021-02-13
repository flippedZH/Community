create table notification
(
    id BIGINT auto_increment,
    notifier BIGINT not null,
    receiver BIGINT not null,
    "outerId" BIGINT not null,
    type int not null,
    gmt_create BIGINT not null,
    status int default 0 not null
);

create unique index NOTIFICATION_ID_UINDEX
	on notification (id);

alter table notification
    add constraint NOTIFICATION_PK
        primary key (id);

