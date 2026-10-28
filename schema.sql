-- Tables for the ER diagram (Spring Boot creates these automatically with ddl-auto=update).
-- Use this file to explain the design in your report or to create the tables by hand.

CREATE TABLE app_user (                 -- User
  user_id        BIGSERIAL PRIMARY KEY,
  name           VARCHAR(80),
  phone          VARCHAR(10),
  email          VARCHAR(120) UNIQUE NOT NULL,
  password_hash  VARCHAR(100),          -- empty for Google users
  user_type      VARCHAR(10) DEFAULT 'Visitor',
  provider       VARCHAR(10) DEFAULT 'LOCAL'
);

CREATE TABLE museum (                   -- Museum
  museum_id      BIGSERIAL PRIMARY KEY,
  name           VARCHAR(100),
  location       VARCHAR(100),
  open_days      VARCHAR(60),
  open_time      VARCHAR(15),
  close_time     VARCHAR(15),
  closed_on      VARCHAR(40),
  description    VARCHAR(500),
  adult_price    INT,
  child_price    INT,
  student_price  INT
);

CREATE TABLE transport (                -- Transport
  transport_id   BIGSERIAL PRIMARY KEY,
  transport_type VARCHAR(40),
  price          INT,
  available      BOOLEAN               -- Availability Status
);

CREATE TABLE ticket_booking (           -- Ticket Booking
  ticket_id      BIGSERIAL PRIMARY KEY,
  user_id        BIGINT NOT NULL REFERENCES app_user(user_id),
  museum_id      BIGINT NOT NULL REFERENCES museum(museum_id),      -- Issues
  transport_id   BIGINT REFERENCES transport(transport_id),         -- Includes
  price          INT,
  quantity       INT,
  total          INT,
  booking_date   DATE,
  visit_date     DATE,
  ticket_type    VARCHAR(15)
);

CREATE TABLE payment (                  -- Payment
  payment_id     BIGSERIAL PRIMARY KEY,
  ticket_id      BIGINT UNIQUE NOT NULL REFERENCES ticket_booking(ticket_id),   -- Confirms
  payment_method VARCHAR(20),
  amount         INT,
  transaction    VARCHAR(30),
  payment_status VARCHAR(10)
);
