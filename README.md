# Microservicio Capacidad

Gestión de capacidades técnicas del sistema.

## Requisitos

- Java 25+
- MySQL 9.0+
- Gradle 9.3.0+

## Configuración

### 1. Variables de entorno

Copia el archivo `.env.example` a `.env`:

```bash
cp .env.example .env
```

Las variables por defecto están configuradas para conectar con MySQL en Docker.

### 2. Levantar la base de datos

Inicia MySQL desde Docker:

```bash
docker-compose up -d
```

Verifica que el contenedor esté corriendo:

```bash
docker-compose ps
```

### 3. Levantar el servicio

```bash
./gradlew bootRun
```

El servicio estará disponible en `http://localhost:8083`

### 4. Documentación API

**OpenAPI (Swagger):** `http://localhost:8083/swagger-ui.html`

**Credenciales predeterminadas:**

- Usuario: `admin`
- Contraseña: `admin123`

## Ver más

Para información del proyecto completo, consulta el [repositorio raíz](https://github.com/Nekstoreo/programacion-reactiva).
