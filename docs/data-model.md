# Data Model

Entity-relationship diagram of the `hotel_pms` database. It mirrors the Flyway
migration `V1__create_tables.sql` and the constraints added in
`V2__constraints_and_indexes.sql`.

## ER diagram

```mermaid
erDiagram
    HOTEL_CHAIN ||--o{ PROPERTY : owns
    ROLE ||--o{ APP_USER : classifies
    PROPERTY ||--o{ ROOM : houses
    ROOM_TYPE ||--o{ ROOM : defines
    APP_USER ||--o{ RESERVATION : books
    RESERVATION ||--o{ RESERVATION_ROOM : includes
    ROOM ||--o{ RESERVATION_ROOM : "assigned to"
    RESERVATION ||--o{ RESERVATION_AMENITY : adds
    AMENITY ||--o{ RESERVATION_AMENITY : details
    RESERVATION ||--|| PAYMENT : settles
    PAYMENT ||--|| INVOICE : backs

    HOTEL_CHAIN {
        bigint id PK
        varchar name
        varchar description
        varchar status "ACTIVE|INACTIVE"
    }
    PROPERTY {
        bigint id PK
        bigint hotel_chain_id FK
        varchar name
        varchar address
        varchar city
        varchar phone
        varchar email
        varchar category
        varchar status "ACTIVE|INACTIVE"
    }
    ROLE {
        bigint id PK
        varchar name UK
        varchar description
    }
    APP_USER {
        bigint id PK
        bigint role_id FK
        varchar name
        varchar last_name
        varchar email UK
        varchar password
        varchar phone
        varchar document_number UK
        timestamp created_at
    }
    ROOM_TYPE {
        bigint id PK
        varchar name UK
        varchar description
    }
    ROOM {
        bigint id PK
        bigint property_id FK
        bigint room_type_id FK
        varchar room_number
        int floor
        int capacity
        numeric price_per_night
        varchar status "AVAILABLE|OCCUPIED|MAINTENANCE|CLEANING"
        boolean available
    }
    RESERVATION {
        bigint id PK
        bigint guest_id FK
        date check_in_date
        date check_out_date
        varchar status "PENDING|CONFIRMED|CANCELLED|COMPLETED"
        numeric total
        timestamp created_at
    }
    RESERVATION_ROOM {
        bigint id PK
        bigint reservation_id FK
        bigint room_id FK
    }
    AMENITY {
        bigint id PK
        varchar name
        varchar type "RESTAURANT|SPA|LAUNDRY|TRANSPORT"
        varchar description
        numeric price
        boolean active
    }
    RESERVATION_AMENITY {
        bigint id PK
        bigint reservation_id FK
        bigint amenity_id FK
        int quantity
        numeric unit_price
        numeric subtotal
    }
    PAYMENT {
        bigint id PK
        bigint reservation_id FK "UNIQUE (1:1)"
        numeric amount
        varchar payment_method "CASH|CARD|TRANSFER|DIGITAL_WALLET"
        varchar status "PENDING|COMPLETED|FAILED|REFUNDED"
        timestamp paid_at
    }
    INVOICE {
        bigint id PK
        bigint payment_id FK "UNIQUE (1:1)"
        varchar invoice_number UK
        numeric subtotal
        numeric taxes
        numeric total
        timestamp issued_at
        varchar description
    }
```

## Relationships

| From | Cardinality | To | Description |
|---|---|---|---|
| `hotel_chain` | 1 : N | `property` | A chain operates several properties |
| `role` | 1 : N | `app_user` | A role groups several users |
| `property` | 1 : N | `room` | A property houses several rooms |
| `room_type` | 1 : N | `room` | A type classifies several rooms |
| `app_user` (guest) | 1 : N | `reservation` | A guest books several reservations |
| `reservation` ↔ `room` | N : M | through `reservation_room` | Several rooms per reservation |
| `reservation` ↔ `amenity` | N : M | through `reservation_amenity` | Extra services per reservation |
| `reservation` | 1 : 1 | `payment` | One payment per reservation (`uq_payment_reservation`) |
| `payment` | 1 : 1 | `invoice` | One invoice per payment (`uq_invoice_payment`) |

## Normalisation and constraints

- **3NF**: separate catalogues (`role`, `room_type`, `amenity`) and bridge tables
  (`reservation_room`, `reservation_amenity`) remove redundancy and direct N:M
  relationships.
- **Uniqueness**: `app_user.email`, `app_user.document_number`, `role.name`,
  `room_type.name`, `(room.property_id, room_number)`, `invoice.invoice_number`.
- **Controlled domains**: `CHECK` constraints mirroring the application enums
  (statuses, payment methods, amenity types).
- **Business rules**: `check_out_date > check_in_date`, non-negative amounts and
  quantities.
- **Indexes** on every foreign key for query scalability.

## A note on the `app_user` table

The table is called `app_user` rather than `user` because `USER` is a reserved
word in PostgreSQL. The JPA entity is still named `User`.
