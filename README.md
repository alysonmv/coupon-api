# coupon-api

API REST de cupons de desconto — desafio técnico Java Spring.

## Como rodar localmente

```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

## Como rodar com Docker

```bash
docker compose up --build
```

## Como rodar os testes

```bash
./mvnw test
```

## Endpoints

| Método | Rota | Descrição | Resposta |
|--------|------|-----------|----------|
| POST | `/coupon` | Cria um cupom | `201 Created` |
| GET | `/coupon/{id}` | Busca um cupom por ID | `200 OK` / `404 Not Found` |
| DELETE | `/coupon/{id}` | Soft delete de um cupom | `204 No Content` |

## Links úteis

- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:coupondb`)

## Decisões de arquitetura

### Clean Architecture e separação domínio/infra

O domínio (`Coupon`, `CouponCode`) não depende de nenhuma biblioteca externa — nem Spring, nem JPA. Isso garante que as regras de negócio possam ser testadas de forma rápida e isolada, sem subir contexto de framework.

A inversão de dependência é feita via `CouponRepositoryPort`: o domínio define a interface, a infra implementa. Trocar o banco de dados não afeta nenhuma linha de código de domínio ou de use case.

### Value Objects (`CouponCode`)

`CouponCode` encapsula toda a lógica de sanitização e validação do código do cupom. O valor é imutável e sempre consistente — qualquer código que tenha um `CouponCode` em mãos sabe que ele é válido e está no formato correto (6 chars alfanuméricos maiúsculos). Isso elimina validações espalhadas pelo sistema.

### Use cases focados em vez de service genérico

Cada use case (`CreateCoupon`, `GetCoupon`, `DeleteCoupon`) é uma classe com um único método `execute()`. Isso segue o princípio de responsabilidade única de forma explícita: cada classe tem exatamente um motivo para mudar, seus testes são simples e diretos, e o código não esconde comportamento em métodos privados de um service monolítico.
