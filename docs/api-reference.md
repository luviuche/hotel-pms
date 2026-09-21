# REST API Reference

Every resource is exposed under `/api` and speaks JSON.

## Layered architecture

```
Controller (REST)  ->  Service (business logic)  ->  Repository (JpaRepository)  ->  Database
        |                       |                              |
   /api/...             validation, foreign keys,        generated CRUD
   GET/POST/PUT/DELETE  business rules                   + derived queries
```

| Layer      | Package                              | Responsibility                                   |
|------------|--------------------------------------|--------------------------------------------------|
| Entity     | `io.github.luviuche.hotel.entity`     | JPA model (object-relational mapping).           |
| Repository | `io.github.luviuche.hotel.repository` | Data access through `JpaRepository`.             |
| Service    | `io.github.luviuche.hotel.service`    | Business logic, validation, foreign key lookups. |
| Controller | `io.github.luviuche.hotel.controller` | REST endpoints (`@RestController`).              |
| Exception  | `io.github.luviuche.hotel.exception`  | Global error handling (`@RestControllerAdvice`). |

## Conventions

- **Format:** JSON (`Content-Type: application/json`).
- **Foreign keys are plain ids.** Relations are sent and returned as a simple id
  rather than a nested object: creating a room takes `"propertyId": 1` and
  `"roomTypeId": 1`, and responses use the same shape.
- **The password is never returned.** It is accepted when creating or updating a
  user, but never included in a response.
- **Server-side defaults.** Some fields are filled in when omitted, such as
  `status`, `available`, `paidAt`, `issuedAt`, and the `subtotal` of an amenity line.

### HTTP status codes

| Code  | When it is used                                                |
|-------|----------------------------------------------------------------|
| `200` | Successful read or update (GET, PUT).                          |
| `201` | Resource created (POST).                                       |
| `204` | Resource deleted (DELETE), no body.                            |
| `400` | Business rule broken (duplicate data, inconsistent dates, ...). |
| `404` | The resource, or a referenced foreign key, does not exist.     |
| `409` | Data integrity conflict in the database.                       |

### Error body

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "User with id 99 was not found.",
  "timestamp": "2026-05-22T10:30:00"
}
```

## CRUD operations (identical for every resource)

For a resource at `/api/{resource}`:

| Method   | Path                    | Operation | Response              |
|----------|-------------------------|-----------|-----------------------|
| `GET`    | `/api/{resource}`       | List      | `200`                 |
| `GET`    | `/api/{resource}/{id}`  | Read one  | `200` / `404`         |
| `POST`   | `/api/{resource}`       | Create    | `201` / `400` / `404` |
| `PUT`    | `/api/{resource}/{id}`  | Update    | `200` / `400` / `404` |
| `DELETE` | `/api/{resource}/{id}`  | Delete    | `204` / `404`         |

## Available resources

| Entity             | Base path                     |
|--------------------|-------------------------------|
| Role               | `/api/roles`                  |
| User               | `/api/users`                  |
| Hotel chain        | `/api/hotel-chains`           |
| Property           | `/api/properties`             |
| Room type          | `/api/room-types`             |
| Room               | `/api/rooms`                  |
| Reservation        | `/api/reservations`           |
| Reservation room   | `/api/reservation-rooms`      |
| Amenity            | `/api/amenities`              |
| Reservation amenity| `/api/reservation-amenities`  |
| Payment            | `/api/payments`               |
| Invoice            | `/api/invoices`               |

## Request body examples (POST)

### Role — `/api/roles`
```json
{ "name": "GUEST", "description": "Books their own stays" }
```

### User — `/api/users`
```json
{
  "roleId": 1,
  "name": "Ana",
  "lastName": "Perez",
  "email": "ana@mail.com",
  "password": "secret123",
  "phone": "3001234567",
  "documentNumber": "CC-100"
}
```

### Hotel chain — `/api/hotel-chains`
```json
{ "name": "ACM Hotels", "description": "Nationwide chain", "status": "ACTIVE" }
```

### Property — `/api/properties`
```json
{
  "hotelChainId": 1,
  "name": "ACM Downtown",
  "address": "Cra 1 # 2-3",
  "city": "Bogota",
  "phone": "6011234567",
  "email": "downtown@acm.com",
  "category": "4 stars",
  "status": "ACTIVE"
}
```

### Room type — `/api/room-types`
```json
{ "name": "DOUBLE", "description": "Two beds" }
```

### Room — `/api/rooms`
```json
{
  "propertyId": 1,
  "roomTypeId": 1,
  "number": "101",
  "floor": 1,
  "capacity": 2,
  "pricePerNight": 150.00,
  "status": "AVAILABLE",
  "available": true
}
```

### Reservation — `/api/reservations`
```json
{
  "guestId": 1,
  "checkInDate": "2026-06-01",
  "checkOutDate": "2026-06-05",
  "status": "PENDING",
  "total": 600.00
}
```

### Reservation room — `/api/reservation-rooms`
```json
{ "reservationId": 1, "roomId": 1 }
```

### Amenity — `/api/amenities`
```json
{ "name": "Buffet breakfast", "type": "RESTAURANT", "description": "Drinks included", "price": 20.00, "active": true }
```

### Reservation amenity — `/api/reservation-amenities`
```json
{ "reservationId": 1, "amenityId": 1, "quantity": 2, "unitPrice": 20.00 }
```
> `subtotal` is worked out on the server (`quantity x unitPrice`). When
> `unitPrice` is omitted, the amenity's own price is used.

### Payment — `/api/payments`
```json
{ "reservationId": 1, "amount": 600.00, "paymentMethod": "CARD", "status": "COMPLETED" }
```
> One payment per reservation (1:1). `paidAt` is stamped automatically when omitted.

### Invoice — `/api/invoices`
```json
{ "paymentId": 1, "invoiceNumber": "F-0001", "subtotal": 600.00, "taxes": 114.00, "description": "Stay jun-2026" }
```
> One invoice per payment (1:1). `total` (= subtotal + taxes) and `issuedAt`
> are computed when omitted.

## Enum values

| Enum                | Values                                          |
|---------------------|-------------------------------------------------|
| `ActivationStatus`  | `ACTIVE`, `INACTIVE`                            |
| `RoomStatus`        | `AVAILABLE`, `OCCUPIED`, `MAINTENANCE`, `CLEANING` |
| `ReservationStatus` | `PENDING`, `CONFIRMED`, `CANCELLED`, `COMPLETED` |
| `PaymentStatus`     | `PENDING`, `COMPLETED`, `FAILED`, `REFUNDED`    |
| `PaymentMethod`     | `CASH`, `CARD`, `TRANSFER`, `DIGITAL_WALLET`    |
| `AmenityType`       | `RESTAURANT`, `SPA`, `LAUNDRY`, `TRANSPORT`     |

## Quick tour with `curl`

```bash
# Create a role
curl -X POST http://localhost:8080/api/roles \
  -H "Content-Type: application/json" \
  -d '{"name":"GUEST","description":"Books their own stays"}'

# List users
curl http://localhost:8080/api/users

# Update a room
curl -X PUT http://localhost:8080/api/rooms/1 \
  -H "Content-Type: application/json" \
  -d '{"propertyId":1,"roomTypeId":1,"number":"101","floor":1,"capacity":3,"pricePerNight":180.00,"status":"AVAILABLE","available":true}'

# Delete a reservation
curl -X DELETE http://localhost:8080/api/reservations/1
```
