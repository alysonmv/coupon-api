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

## Cobertura de testes (JaCoCo)

O [JaCoCo](https://www.jacoco.org/jacoco/) instrumenta o código durante os testes e mede quais linhas e branches foram efetivamente exercitados. O relatório é gerado automaticamente na fase `test`.

```bash
./mvnw test          # gera o relatório em target/site/jacoco/index.html
./mvnw verify        # além de gerar, valida o gate mínimo de cobertura
```

O relatório é um **arquivo local** (não uma URL `http`). Abra `target/site/jacoco/index.html` direto no navegador — no Windows, use o caminho completo com o prefixo `file:///`, por exemplo:

```
file:///C:/Projetos/spring/coupon-api/target/site/jacoco/index.html
```

Há um gate de **80% de cobertura de linhas** (`BUNDLE`) verificado na fase `verify`: se a cobertura cair abaixo desse valor, o build falha. A classe `CouponApiApplication` (bootstrap do Spring) é excluída da medição.

**Cobertura atual: 100% de linhas e branches** (148 linhas, 12 branches — 0 não cobertos).

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
