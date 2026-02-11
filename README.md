# Tokenization Service

## Overview

This service provides simple tokenization and detokenization APIs.

Tokenization replaces sensitive account numbers with random tokens that have no intrinsic value, while detokenization reverses the process.

The implementation focuses on correctness, clarity, and extensibility rather than production-hardening.

---

## API Endpoints

### Tokenize

**POST** `/tokenize`

**Input**

```json
[
  "4111-1111-1111-1111",
  "4444-3333-2222-1111",
  "4444-1111-2222-3333"
]
```
**Output**

```json
[
  "fvMymE7X0Je1IzMDgWooV5iGBPw0yoFy",
  "L4hKuBJHxe67ENSKLVbdIH8NhFefPui2",
  "ZA5isc0kVUfvlxTE5m2dxIY8AG76KoP3"
]
```

---

## Detokenize

**POST** `/detokenize`

### Input

```json
[
  "fvMymE7X0Je1IzMDgWooV5iGBPw0yoFy",
  "L4hKuBJHxe67ENSKLVbdIH8NhFefPui2",
  "ZA5isc0kVUfvlxTE5m2dxIY8AG76KoP3"
]
```

### Output

```json
[
  "4111-1111-1111-1111",
  "4444-3333-2222-1111",
  "4444-1111-2222-3333"
]
```

---

## Design Decisions

### Technology Stack

- Java 21  
- Spring Boot (Spring MVC)  
- H2 in-memory database  
- Spring Data JPA  
- Gradle  

Spring MVC is used instead of WebFlux because the persistence layer is blocking and the problem does not require reactive semantics.

---

### Architecture

The core logic depends on interfaces rather than concrete implementations.

#### Ports

- **TokenStore** — tokenize / detokenize operations  
- **TokenGenerator** — token generation strategy  

#### Adapters

- **JpaTokenStore** — H2/JPA-backed implementation  
- **SecureRandomBase62TokenGenerator** — SecureRandom-based token generator  

This design allows alternative implementations to be added without changing the service or controller layers.
