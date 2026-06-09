create table if not exists ss_user_favourite_spots(
    id serial primary key,
    user_id int not null,
    spot_id int not null,
    constraint fk_user_favourite_spots_spot_id foreign key(spot_id) references ss_spot(id)
);

create table if not exists ss_user_favourite_events(
    id serial primary key,
    user_id int not null,
    event_id int not null,
    constraint fk_user_favourite_events_event_id foreign key(event_id) references ss_event(id)
);

alter table ss_user
    drop column category_01,
    drop column category_02,
    drop column category_03,
    add column question_one boolean not null,
    add column question_two boolean not null,
    add column question_three boolean not null,
    add column question_four boolean not null;
