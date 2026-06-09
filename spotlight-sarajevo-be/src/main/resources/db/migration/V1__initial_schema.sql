create table if not exists ss_spot_category (
	id SERIAL primary key,
	spot_category_name_bs VARCHAR(255) not null unique,
	spot_category_name_en VARCHAR(255) not null unique,
	spot_category_description_bs VARCHAR(255) not null,
	spot_category_description_en VARCHAR(255) not null,
	spot_category_color_code VARCHAR(9)
);

create table if not exists ss_event_category (
	id SERIAL primary key,
	event_category_name_bs VARCHAR(255) not null unique,
	event_category_name_en VARCHAR(255) not null unique,
	event_category_description_bs VARCHAR(255) not null,
	event_category_description_en VARCHAR(255) not null,
	event_category_color_code VARCHAR(9)
);

create table if not exists ss_tag (
	id SERIAL primary key,
	tag_name_bs VARCHAR(255) not null unique,
	tag_name_en VARCHAR(255) not null unique,
	tag_description_bs VARCHAR(255) null,
	tag_description_en VARCHAR(255) null
);

create table if not exists ss_media_store (
	id SERIAL primary key,
	item_id INT not null,
	item_category VARCHAR(255) not null,
	image_url text null,
	image_delete_url text null,
	is_thumbnail bool default false,
	created TIMESTAMP null,
	created_by TIMESTAMP null,
	constraint ss_media_store_item_category check ( item_category in ('SPOT', 'EVENT', 'SECTION_GUIDE', 'TRANSPORT'))
);

create table if not exists ss_user (
	id SERIAL primary key,
	locale varchar(10) null,
	first_name varchar(255) not null,
	last_name varchar (255) not null,
	is_premium bool default false,
	is_admin bool default false,
	category_01 int not null,
	category_02 int not null,
	category_03 int not null,
	created_at timestamp null,
	updated_at timestamp null
);

create table if not exists ss_user_auth_details (
	id serial primary key,
	user_id int not null,
	registration_type varchar not null,
	sys_email varchar(255) null,
	sys_password varchar(15) null,
	google_id varchar(255) null,
	google_email varchar(255) null,
	email_verified bool default false,
	otp_tfa_setup bool default false,
	otp_tfa_code int null,
	email_tfa_setup bool default false,
	email_tfa_code int null,
	constraint ss_user_registration_type check ( registration_type in ('SYSTEM', 'GOOGLE')),
	constraint fk_user_auth_details_user foreign key (user_id) references ss_user(id) on delete cascade
);

create table if not exists ss_collection (
	id serial primary key,
	collection_name varchar(255) not null,
	collection_type varchar(255) not null,
	user_id int not null,
	constraint fk_collection_user foreign key (user_id) references ss_user(id) on delete cascade
);

create table if not exists ss_community_request (
	id serial primary key,
	user_id int not null,
	user_full_name varchar(255) not null,
	request_type varchar(255) not null,
	object_type varchar(255) not null,
	request_header varchar(30) not null,
	request_description varchar(255) not null,
	constraint fk_community_request_user_id foreign key (user_id) references ss_user(id) on delete cascade
);

create table if not exists ss_event_organiser (
	id serial primary key,
	organiser_name varchar(255) not null,
	organiser_creation_date timestamp not null,
	organiser_category_id varchar(255) not null,
	organiser_phone varchar(20) null,
	organiser_email varchar(255) null,
	organiser_website varchar(255) null
);

create table if not exists ss_event (
	id serial primary key,
	slug varchar(255) not null unique,
	official_name varchar(255) not null,
	small_description_bs varchar(255) not null,
	small_description_en varchar(255) not null,
	full_description_bs text not null,
	full_description_en text not null,
	category_id int null,
	start_date timestamp not null,
	end_date timestamp not null,
	entry_price numeric null,
	age_limit numeric null,
	reservation bool null,
	cancel_refund varchar(255) null,
	event_language varchar(255) null,
	organiser_id int not null,
	constraint fk_event_category_id foreign key (category_id) references ss_event_category(id) on delete set null,
	constraint fk_event_organiser foreign key (organiser_id) references ss_event_organiser(id) on delete cascade
);

create table if not exists ss_event_tags (
	id serial not null,
	event_id int not null,
	tag_id int not null,
	constraint fk_event_tags_event foreign key (event_id) references ss_event(id) on delete cascade,
	constraint fk_event_tags_tag foreign key (tag_id) references ss_tag(id) on delete cascade
);

create table if not exists ss_spot (
	id serial primary key,
	slug varchar(255) not null unique,
	official_name varchar(40) not null,
	small_description_bs varchar(60) not null,
	small_description_en varchar(60) not null,
	full_description_bs text not null,
	full_description_en text not null,
	latitude numeric not null,
	longitude numeric not null,
	address varchar(255),
	category_id int not null,
	initial_overall_rating numeric null,
	initial_cleanliness numeric null,
	initial_affordability numeric null,
	initial_accessibility numeric null,
	initial_staff_kindness numeric null,
	initial_locale_quality numeric null,
	initial_atmosphere numeric null,
	created timestamp default current_timestamp null,
	created_by varchar(255) null,
	modified timestamp null,
	modified_by varchar(255) null,
	constraint fk_spot_category foreign key (category_id) references ss_spot_category (id) on delete set null
);

create table if not exists ss_spot_contact (
	id serial primary key,
	spot_contact_phopne varchar(20) null,
	spot_contact_email varchar(255) null,
	spot_contact_webpage varchar(255) null
);

create table if not exists ss_spot_tags (
	id serial primary key,
	spot_id int4 NOT NULL,
	tag_id int4 NOT NULL,
	CONSTRAINT fk_spot_tags_spot FOREIGN KEY (spot_id) REFERENCES ss_spot(id) ON DELETE CASCADE,
	CONSTRAINT fk_spot_tags_tag FOREIGN KEY (tag_id) REFERENCES ss_tag(id) ON DELETE CASCADE
);

create table if not exists ss_spot_work_hours (
	id serial primary key,
	day varchar(255) NULL,
	start_time time NULL,
	end_time time NULL,
	spot_id int NULL,
	constraint fk_spot_work_hours_ss_spot_fk FOREIGN KEY (spot_id) references ss_spot(id) ON DELETE CASCADE
);

create table if not exists ss_user_attended_events (
	id serial primary key,
	user_id int not null,
	event_id int not null,
	constraint fk_user_attended_events_user_id foreign key (user_id) references ss_user(id) on delete cascade,
	constraint fk_user_attended_events_event_id foreign key (user_id) references ss_event(id) on delete cascade
);

create table if not exists ss_user_visited_spots (
	id serial primary key,
	user_id int not null,
	spot_id int not null,
	constraint fk_user_visited_spots_user_id foreign key (user_id) references ss_user(id) on delete cascade,
	constraint fk_user_visited_spots_spot_id foreign key (spot_id) references ss_spot(id) on delete cascade
);

create table if not exists ss_collection (
	id serial primary key,
	collection_name varchar(30) not null,
	collection_description varchar(60) null,
	collection_type varchar(30) not null,
	created timestamp default current_timestamp,
	created_by varchar(255) default null,
	user_id int not null,
	constraint fk_collection_user_id foreign key (user_id) references ss_user(id) on delete cascade,
	constraint ss_collection_collection_type check (collection_type in ('SPOT', 'EVENT'))
);

create table if not exists ss_collection_spot (
	id serial primary key,
	collection_id int not null,
	spot_id int not null,
	constraint fk_collection_spot_collection_id foreign key (collection_id) references ss_collection(id) on delete cascade,
	constraint fk_collection_spot_spot_id foreign key (spot_id) references ss_spot(id) on delete cascade
);

create table if not exists ss_collection_event (
	id serial primary key,
	collection_id int not null,
	event_id int not null,
	constraint fk_collection_event_collection_id foreign key (collection_id) references ss_collection(id) on delete cascade,
	constraint fk_collection_event_event_id foreign key (event_id) references ss_event(id) on delete cascade
);

create table if not exists ss_spot_review (
	id serial primary key,
	user_id int NOT NULL,
	header varchar(255) NULL,
	body text NULL,
	user_overall_rating int4 NULL,
	user_cleanliness numeric NULL,
	user_affordability numeric NULL,
	user_accessibility numeric NULL,
	user_staff_kindness numeric NULL,
	user_locale_quality numeric NULL,
	user_atmosphere numeric NULL,
	spot_id int NULL,
	CONSTRAINT fk_spot_review_spot FOREIGN KEY (spot_id) REFERENCES ss_spot(id) ON DELETE cascade,
	CONSTRAINT fk_spot_review_user FOREIGN KEY (user_id) REFERENCES ss_user(id) ON DELETE CASCADE
);

create table if not exists ss_event_review (
	id serial primary key,
	user_id int not null,
	header varchar(255) NULL,
	body text NULL,
	user_event_quality numeric null,
	user_event_enjoyability numeric null,
	user_event_atmosphere numeric null,
	event_id int null,
	CONSTRAINT fk_event_review_event FOREIGN KEY (event_id) REFERENCES ss_event(id) ON DELETE cascade,
	CONSTRAINT fk_event_review_user FOREIGN KEY (user_id) REFERENCES ss_user(id) ON DELETE CASCADE
);

create table if not exists ss_tourist_guide (
	id serial primary key,
	guide_title varchar(30) not null,
	guide_small_description_bs varchar(50) not null,
	guide_small_description_en varchar(50) not null
);

create table if not exists ss_tourist_guide_section (
	id serial primary key,
	guide_id int not null,
	section_title_bs varchar(50) not null,
	section_title_en varchar(50) not null,
	section_body_bs text not null,
	section_body_en text not null,
	constraint fk_tourist_guide_section_guide_id foreign key (guide_id) references ss_tourist_guide (id) on delete cascade
);

create table if not exists ss_transport_method (
	id serial primary key,
	method_name_bs varchar(30) not null,
	method_name_en varchar(30) not null,
	method_description_bs text not null,
	method_description_en text not null
);

CREATE OR REPLACE FUNCTION check_event_ended()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM ss_event
        WHERE id = NEW.event_id
          AND end_date > CURRENT_DATE
    ) THEN
        RAISE EXCEPTION 'Cannot review an event before it has ended';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_event_review_check ON ss_event_review;

create trigger trg_event_review_check
BEFORE INSERT ON ss_event_review
FOR EACH ROW
EXECUTE FUNCTION check_event_ended();

create table if not exists ss_transport_method (
	id serial primary key,
	transport_name varchar(255) not null,
	transport_description_bs text not null,
	transport_description_en text not null
);

create table if not exists ss_transport_method_operator (
	id serial primary key,
	transport_type_id int not null,
	transport_operator_name varchar(30) not null,
	transport_operator_webpage varchar(255) null,
	transport_operator_phone varchar(255) null,
	transport_operator_email varchar(255) null,
	constraint fk_transport_method_operator_type foreign key (transport_type_id) references ss_transport_method(id)
);

create table if not exists ss_transport_method_station (
	id serial primary key,
	transport_type_id int not null,
	line_number int null,
	line_station_lat numeric null,
	line_station_long numeric null,
	station_name varchar(255) null,
	constraint fk_transport_method_station_type foreign key (transport_type_id) references ss_transport_method(id)
);


CREATE OR REPLACE view ss_spot_review_stats
AS SELECT s.id AS spot_id,
    (s.initial_overall_rating + COALESCE(sum(r.user_overall_rating)::numeric, 0::numeric)) / (1 + COALESCE(count(r.user_overall_rating), 0::bigint))::numeric AS combined_rating,
    (s.initial_cleanliness + COALESCE(sum(r.user_cleanliness), 0::numeric)) / (1 + COALESCE(count(r.user_cleanliness), 0::bigint))::numeric AS combined_cleanliness,
    (s.initial_affordability + COALESCE(sum(r.user_affordability), 0::numeric)) / (1 + COALESCE(count(r.user_affordability), 0::bigint))::numeric AS combined_affordability,
    (s.initial_accessibility + COALESCE(sum(r.user_accessibility), 0::numeric)) / (1 + COALESCE(count(r.user_accessibility), 0::bigint))::numeric AS combined_accessibility,
    (s.initial_staff_kindness + COALESCE(sum(r.user_staff_kindness), 0::numeric)) / (1 + COALESCE(count(r.user_staff_kindness), 0::bigint))::numeric AS combined_staff_kindness,
    (s.initial_locale_quality + COALESCE(sum(r.user_locale_quality), 0::numeric)) / (1 + COALESCE(count(r.user_locale_quality), 0::bigint))::numeric AS combined_quality,
    (s.initial_atmosphere + COALESCE(sum(r.user_atmosphere), 0::numeric)) / (1 + COALESCE(count(r.user_atmosphere), 0::bigint))::numeric AS combined_atmosphere
FROM ss_spot s
     LEFT JOIN ss_spot_review r ON s.id = r.spot_id
GROUP BY s.id, s.initial_cleanliness, s.initial_affordability, s.initial_accessibility, s.initial_staff_kindness, s.initial_locale_quality, s.initial_atmosphere;

