# soporte-api

API REST en memoria para gestionar solicitudes de soporte del laboratorio de Servicios Web.
Pensada para ser consumida desde React + Axios (Vite, puerto 5173).

## Requisitos

- Java 17 (Maven incluido mediante el wrapper `./mvnw`)

## Cómo ejecutar

```bash
./mvnw spring-boot:run   # http://localhost:8080
./mvnw test              # pruebas con MockMvc
```

Los datos viven solo en memoria: se reinician al apagar la aplicación.

## Endpoints (base: `/api/solicitudes`)

| Método | Ruta | Respuesta |
|--------|------|-----------|
| GET | `/api/solicitudes` | 200 + arreglo de solicitudes |
| POST | `/api/solicitudes` | 201 + `Location` + solicitud creada |

Solicitud: `{ "id": 1, "titulo": "...", "prioridad": "Alta|Media|Baja", "estado": "Pendiente|En proceso|Resuelta" }`

POST recibe `{ "titulo", "prioridad" }`. El `estado` siempre queda en `Pendiente` y el `id` lo genera el servidor.
Validaciones: `titulo` no vacío (se recorta) y máximo 120 caracteres; `prioridad` debe ser `Alta`, `Media` o `Baja`.
Si es inválido responde 400:

```json
{ "status": 400, "error": "Bad Request", "messages": ["titulo is required"] }
```

Datos iniciales: `1 Equipo no enciende (Alta, Pendiente)` y `2 Instalar software (Media, En proceso)`.

## CORS

Orígenes permitidos en `/api/**` (métodos GET, POST, OPTIONS) mediante la propiedad
`app.cors.allowed-origins` (lista separada por comas). Por defecto:
`http://localhost:5173,http://127.0.0.1:5173`.

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--app.cors.allowed-origins=http://localhost:3000
```

## Ejemplos con curl

```bash
curl -i http://localhost:8080/api/solicitudes

curl -i -X POST http://localhost:8080/api/solicitudes \
  -H 'Content-Type: application/json' \
  -d '{"titulo":"Red caída","prioridad":"Alta"}'

# inválido -> 400
curl -i -X POST http://localhost:8080/api/solicitudes \
  -H 'Content-Type: application/json' \
  -d '{"titulo":" ","prioridad":"Urgente"}'
```

También hay una colección en `postman/soporte-api.postman_collection.json`.
