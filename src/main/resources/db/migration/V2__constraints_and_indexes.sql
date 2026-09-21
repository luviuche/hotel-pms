-- Integrity constraints, controlled domains and indexes.

-- 1. Uniqueness
alter table app_user  add constraint uq_app_user_email          unique (email);
alter table app_user  add constraint uq_app_user_document       unique (document_number);
alter table role      add constraint uq_role_name               unique (name);
alter table room_type add constraint uq_room_type_name          unique (name);
alter table room      add constraint uq_room_property_number    unique (property_id, room_number);
alter table invoice   add constraint uq_invoice_number          unique (invoice_number);

-- 2. Controlled domains (aligned with the application enums)
alter table hotel_chain add constraint chk_hotel_chain_status
    check (status in ('ACTIVE', 'INACTIVE'));
alter table property add constraint chk_property_status
    check (status in ('ACTIVE', 'INACTIVE'));
alter table room add constraint chk_room_status
    check (status in ('AVAILABLE', 'OCCUPIED', 'MAINTENANCE', 'CLEANING'));
alter table reservation add constraint chk_reservation_status
    check (status in ('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED'));
alter table payment add constraint chk_payment_method
    check (payment_method in ('CASH', 'CARD', 'TRANSFER', 'DIGITAL_WALLET'));
alter table payment add constraint chk_payment_status
    check (status in ('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED'));
alter table amenity add constraint chk_amenity_type
    check (type in ('RESTAURANT', 'SPA', 'LAUNDRY', 'TRANSPORT'));

-- 3. Basic business rules
alter table reservation add constraint chk_reservation_dates
    check (check_out_date > check_in_date);
alter table reservation add constraint chk_reservation_total
    check (total >= 0);
alter table room add constraint chk_room_price
    check (price_per_night >= 0);
alter table room add constraint chk_room_capacity
    check (capacity > 0);
alter table room add constraint chk_room_floor
    check (floor >= 0);
alter table payment add constraint chk_payment_amount
    check (amount > 0);
alter table invoice add constraint chk_invoice_amounts
    check (subtotal >= 0 and taxes >= 0 and total >= 0);
alter table amenity add constraint chk_amenity_price
    check (price >= 0);
alter table reservation_amenity add constraint chk_reservation_amenity_quantity
    check (quantity > 0);
alter table reservation_amenity add constraint chk_reservation_amenity_amounts
    check (unit_price >= 0 and subtotal >= 0);

-- 4. Indexes on foreign keys (query scalability)
create index idx_property_hotel_chain             on property (hotel_chain_id);
create index idx_app_user_role                    on app_user (role_id);
create index idx_room_property                    on room (property_id);
create index idx_room_room_type                   on room (room_type_id);
create index idx_reservation_guest                on reservation (guest_id);
create index idx_reservation_room_reservation     on reservation_room (reservation_id);
create index idx_reservation_room_room            on reservation_room (room_id);
create index idx_reservation_amenity_reservation  on reservation_amenity (reservation_id);
create index idx_reservation_amenity_amenity      on reservation_amenity (amenity_id);
