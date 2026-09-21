-- Catalogue seed data: fixed values the application relies on.

insert into role (name, description) values
    ('GUEST', 'Books and manages their own reservations'),
    ('STAFF',  'Operational personnel of a property'),
    ('ADMIN',  'Manages properties, users and the system');

insert into room_type (name, description) values
    ('SINGLE',       'Basic single room'),
    ('DOUBLE',       'Room for two guests'),
    ('SUITE',        'Suite with a separate living area'),
    ('PRESIDENTIAL', 'Luxury presidential suite');
