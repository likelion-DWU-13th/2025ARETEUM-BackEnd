-- 기존 테이블 및 Flyway 기록 테이블 삭제
-- Flyway는 DROP 명령어를 실행한 후, 이 마이그레이션을 새롭게 기록합니다.
DROP TABLE IF EXISTS set_menu_variant_items;
DROP TABLE IF EXISTS set_menu_variant;
DROP TABLE IF EXISTS set_menu;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS chat_entity;
DROP TABLE IF EXISTS booth_schedule;
DROP TABLE IF EXISTS booth;

-- 새로 생성할 테이블 (개발팀 DDL 전체)
create table booth (
    id integer not null auto_increment,
    created_at datetime(6) not null,
    updated_at datetime(6),
    location varchar(60),
    detail_location varchar(120),
    organizer varchar(120),
    name varchar(150) not null,
    description varchar(500),
    map_image_url varchar(255),
    time_note varchar(255),
    category enum ('BOOTH','EXPERIENCE','FOOD_TRUCK','MARKET','PERFORMANCE','PUB') not null,
    sub_category enum ('BUSKING','FLEA_MARKET','GENERAL_MARKET','MAIN_PERFORMANCE','NONE') not null,
    primary key (id)
) engine=InnoDB;

create table booth_schedule (
    booth_id integer not null,
    end_time time(6) not null,
    event_date date not null,
    id integer not null auto_increment,
    start_time time(6) not null,
    primary key (id)
) engine=InnoDB;

create table chat_entity (
    created_at datetime(6),
    id bigint not null auto_increment,
    client_id varchar(255),
    content varchar(255),
    primary key (id)
) engine=InnoDB;

create table menu (
    booth_id integer not null,
    id integer not null auto_increment,
    price integer not null,
    name varchar(100) not null,
    note varchar(200),
    category enum ('ALCOHOL','ANJU','DRINK','ETC','EVENT','FT','SET') not null,
    primary key (id)
) engine=InnoDB;

create table product (
    booth_id integer not null,
    id integer not null auto_increment,
    name varchar(100) not null,
    primary key (id)
) engine=InnoDB;

create table set_menu (
    booth_id integer not null,
    id integer not null auto_increment,
    name varchar(100) not null,
    primary key (id)
) engine=InnoDB;

create table set_menu_variant (
    id integer not null auto_increment,
    price integer not null,
    set_menu_id integer not null,
    note varchar(200),
    primary key (id)
) engine=InnoDB;

create table set_menu_variant_items (
    variant_id integer not null,
    item_name varchar(255)
) engine=InnoDB;

alter table booth
    add constraint UKe4fuqg9mlaiw1hb1wsnfvvulf unique (name);

create index idx_schedule_date
    on booth_schedule (event_date);

alter table booth_schedule
    add constraint uk_booth_date_time unique (booth_id, event_date, start_time, end_time);

alter table booth_schedule
    add constraint FK3pwugh8pn0b8p44nptnv0xjp8
        foreign key (booth_id)
            references booth (id);

alter table menu
    add constraint FKb3vqi5lmdacpud33kncjqqfev
        foreign key (booth_id)
            references booth (id);

alter table product
    add constraint FKe4dyyjct0j6bcenubf80rklpm
        foreign key (booth_id)
            references booth (id);

alter table set_menu
    add constraint FKdwdjt6gq3w6rawg31r5r5nnwv
        foreign key (booth_id)
            references booth (id);

alter table set_menu_variant
    add constraint FKce39onfh6b2j9q1xo7fxeiyu4
        foreign key (set_menu_id)
            references set_menu (id);

alter table set_menu_variant_items
    add constraint FK5cwi0hka5q8oc2trwmfpy2i26
        foreign key (variant_id)
            references set_menu_variant (id);