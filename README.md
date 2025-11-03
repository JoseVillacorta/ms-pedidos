# MS Pedidos

Microservicio de gestión de pedidos del Sistema de Gestión de Pedidos.

## Descripción

API REST para gestión completa de pedidos con integración al microservicio de productos. Implementa validación de stock, cálculo de totales y comunicación entre servicios.

## Tecnologías

- **Java**: 21
- **Spring Boot**: 3.3.3
- **WebFlux**: Programación reactiva
- **R2DBC**: Base de datos reactiva
- **PostgreSQL**: Base de datos
- **Maven**: Gestor de dependencias

## Base de Datos

### Tablas
```sql
-- Pedidos
CREATE TABLE pedidos (
    id BIGSERIAL PRIMARY KEY,
    cliente VARCHAR(255) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) DEFAULT 0,
    estado VARCHAR(50) DEFAULT 'PENDIENTE'
);

-- Detalles de pedido
CREATE TABLE detalle_pedidos (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id)
);
```

## Configuración

### Variables de Entorno
- `DB_URL`: URL PostgreSQL (r2dbc:postgresql://...)
- `DB_USERNAME`: Usuario BD
- `DB_PASSWORD`: Contraseña BD
- `MS_PRODUCTOS_URL`: URL del microservicio productos

### Perfiles
- **dev**: Desarrollo (puerto 8082)
- **qa**: QA (puerto 8082)
- **prd**: Producción (puerto 8082)

## Ejecución

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## API Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/pedidos` | Listar pedidos |
| GET | `/api/pedidos/{id}` | Obtener pedido |
| POST | `/api/pedidos` | Crear pedido |
| PUT | `/api/pedidos/{id}/estado` | Actualizar estado |
| DELETE | `/api/pedidos/{id}` | Eliminar pedido |
| GET | `/api/pedidos/buscar/cliente` | Buscar por cliente |
| GET | `/api/pedidos/buscar/estado` | Buscar por estado |

## Funcionalidades

### Validación de Stock
- Verifica disponibilidad antes de crear pedido
- Actualiza stock automáticamente al crear pedido
- Devuelve stock al cancelar pedido

### Cálculo Automático
- Calcula totales basado en productos
- Aplica precios unitarios actuales

### Estados de Pedido
- **PENDIENTE**: Pedido creado
- **PROCESADO**: Pedido completado
- **CANCELADO**: Pedido cancelado

## Ejemplos

### Crear pedido
```bash
POST /api/pedidos
{
  "cliente": "Juan Pérez",
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2
    }
  ]
}
```

### Actualizar estado
```bash
PUT /api/pedidos/1/estado?estado=PROCESADO
```

### Buscar por cliente
```bash
GET /api/pedidos/buscar/cliente?cliente=Juan
```

## 🔗 Integración

Requiere que `ms-productos` esté ejecutándose para:
- Validar stock disponible
- Obtener precios actualizados
- Actualizar inventario