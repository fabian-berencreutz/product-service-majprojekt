# Product Service

Mikrotjänst för hantering av produkter och lagersaldo i webbshoppen.

## Funktioner

- Produkthantering (hämta, skapa och ta bort produkter)
- Lagerhantering (minska lagersaldo vid köp)
- Rollbaserad auktorisering med JWT (USER och ADMIN)
- Swagger/OpenAPI dokumentation

## Teknologier

- Java 17
- Spring Boot
- Spring Security (OAuth2 / JWT)
- Spring Data JPA (H2 / PostgreSQL)
- Maven & Docker

## Starta projektet

Kör med lokal profil (använder inbyggd H2-databas):

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Tjänsten körs på port `8081`.

## API Endpoints

- `GET /products` – Hämta alla produkter (USER/ADMIN)
- `GET /products/{id}` – Hämta produkt via id (USER/ADMIN)
- `POST /products` – Skapa ny produkt (ADMIN)
- `DELETE /products/{id}` – Ta bort produkt (ADMIN)
- `POST /products/stock/decrease` – Minska lagersaldo (USER/ADMIN)

Swagger UI finns på: `http://localhost:8081/swagger-ui.html`
