# Online Museum Ticket Booking System (Pune)

Full-stack portfolio project built from the ER diagram: **User, Museum, Ticket Booking, Transport, Payment**
with the relationships **Search, Issues, Includes, Confirms**.

## Folders

| Folder | What is inside |
|---|---|
| `frontend/` | The website (HTML, CSS, JavaScript) and museum photos in `images/` |
| `backend/` | Spring Boot REST API (Java 17, Spring Security, JWT, Google login, JPA) |
| `database/` | `schema.sql` with the five tables |

## Run it

1. Install Java 17, Maven and PostgreSQL. Create a database: `CREATE DATABASE museum;`
2. Edit `backend/src/main/resources/application.properties` (database password, JWT secret, Google keys).
3. Start the backend: `cd backend && mvn spring-boot:run` (runs on http://localhost:8080).
   The tables and the four Pune museums are created automatically.
4. Open the website: `frontend/index.html` in a browser, or serve it with `npx serve frontend`.

## API

| Method and path | What it does | Login needed |
|---|---|---|
| `POST /api/auth/signup` | Create an account (name, phone, email, password) | No |
| `POST /api/auth/login` | Log in, returns a JWT token | No |
| `GET /api/museums?q=` | Search museums | No |
| `GET /api/museums/{id}` | One museum with timings and prices | No |
| `GET /api/transport` | Transport options and availability | No |
| `POST /api/bookings` | Book tickets and pay | Yes |
| `GET /api/bookings` | My bookings (Admin sees all) | Yes |
| `DELETE /api/bookings/{id}` | Cancel a booking | Yes |

Example request body for `POST /api/bookings`:

```json
{ "museumId": 1, "visitDate": "2026-11-05", "ticketType": "Adult",
  "quantity": 2, "transportId": 1, "paymentMethod": "UPI" }
```

## Notes

- The website in `frontend/` keeps data in the browser (localStorage) so it runs without a server.
  To connect it to the backend, replace the `book()`, signup and login functions with `fetch()` calls
  to the API above and send the token in the `Authorization: Bearer <token>` header.
- The payment step is simulated. A real project would use a payment gateway such as Razorpay.
- Museum timings and prices marked "(sample)" are placeholders. Confirm them with each museum.
