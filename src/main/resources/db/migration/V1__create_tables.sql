create table hotel_chain (
    id bigserial primary key,
    name varchar(100) not null,
    description varchar(255),
    status varchar(30) not null
);

create table property (
    id bigserial primary key,
    hotel_chain_id bigint not null,
    name varchar(100) not null,
    address varchar(150) not null,
    city varchar(80) not null,
    phone varchar(30) not null,
    email varchar(120) not null,
    category varchar(30) not null,
    status varchar(30) not null,
    constraint fk_property_hotel_chain
        foreign key (hotel_chain_id) references hotel_chain (id)
);

create table role (
    id bigserial primary key,
    name varchar(50) not null,
    description varchar(255)
);

create table app_user (
    id bigserial primary key,
    role_id bigint not null,
    name varchar(80) not null,
    last_name varchar(80) not null,
    email varchar(120) not null,
    password varchar(120) not null,
    phone varchar(30) not null,
    document_number varchar(30) not null,
    created_at timestamp not null default now(),
    constraint fk_app_user_role
        foreign key (role_id) references role (id)
);

create table room_type (
    id bigserial primary key,
    name varchar(50) not null,
    description varchar(255)
);

create table room (
    id bigserial primary key,
    property_id bigint not null,
    room_type_id bigint not null,
    room_number varchar(20) not null,
    floor int not null,
    capacity int not null,
    price_per_night numeric(12, 2) not null,
    status varchar(30) not null,
    available boolean not null,
    constraint fk_room_property
        foreign key (property_id) references property (id),
    constraint fk_room_room_type
        foreign key (room_type_id) references room_type (id)
);

create table reservation (
    id bigserial primary key,
    guest_id bigint not null,
    check_in_date date not null,
    check_out_date date not null,
    status varchar(30) not null,
    total numeric(12, 2) not null,
    created_at timestamp not null default now(),
    constraint fk_reservation_guest
        foreign key (guest_id) references app_user (id)
);

create table reservation_room (
    id bigserial primary key,
    reservation_id bigint not null,
    room_id bigint not null,
    constraint fk_reservation_room_reservation
        foreign key (reservation_id) references reservation (id),
    constraint fk_reservation_room_room
        foreign key (room_id) references room (id)
);

create table amenity (
    id bigserial primary key,
    name varchar(80) not null,
    type varchar(50) not null,
    description varchar(255),
    price numeric(12, 2) not null,
    active boolean not null
);

create table reservation_amenity (
    id bigserial primary key,
    reservation_id bigint not null,
    amenity_id bigint not null,
    quantity int not null,
    unit_price numeric(12, 2) not null,
    subtotal numeric(12, 2) not null,
    constraint fk_reservation_amenity_reservation
        foreign key (reservation_id) references reservation (id),
    constraint fk_reservation_amenity_amenity
        foreign key (amenity_id) references amenity (id)
);

create table payment (
    id bigserial primary key,
    reservation_id bigint not null,
    amount numeric(12, 2) not null,
    payment_method varchar(30) not null,
    status varchar(30) not null,
    paid_at timestamp not null,
    constraint fk_payment_reservation
        foreign key (reservation_id) references reservation (id),
    constraint uq_payment_reservation
        unique (reservation_id)
);

create table invoice (
    id bigserial primary key,
    payment_id bigint not null,
    invoice_number varchar(50) not null,
    subtotal numeric(12, 2) not null,
    taxes numeric(12, 2) not null,
    total numeric(12, 2) not null,
    issued_at timestamp not null,
    description varchar(255),
    constraint fk_invoice_payment
        foreign key (payment_id) references payment (id),
    constraint uq_invoice_payment
        unique (payment_id)
);
