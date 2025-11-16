# MS Pedidos

Microservicio reactivo para gestión de pedidos con arquitectura event-driven y estados.

- **Arquitectura Reactiva**: Programación reactiva con Spring WebFlux
- **Event-Driven**: Consume eventos de productos via Kafka
- **Base de Datos Reactiva**: R2DBC con PostgreSQL
- **Service Discovery**: Registro automático en Eureka
- **Estados de Pedido**: Gestión completa del ciclo de vida

## Funcionalidades

### Gestión de Pedidos
- **Crear**: Registro de nuevos pedidos con validación
- **Consultar**: Búsqueda por ID y listado completo
- **Actualizar**: Modificación de estados de pedido
- **Eliminar**: Cancelación de pedidos

### Estados de Pedido
- **PENDIENTE**: Pedido creado, esperando confirmación
- **CONFIRMADO**: Pedido confirmado por el sistema
- **ENVIADO**: Pedido en proceso de envío
- **ENTREGADO**: Pedido entregado al cliente
- **CANCELADO**: Pedido cancelado

### Integración
- **MS Productos**: Validación de stock y precios
- **Kafka**: Consumir eventos de productos

## Dependencias

### Base de Datos
- **PostgreSQL**: Base de datos reactiva con R2DBC
- **Base de datos**: `db_pedidos_dev`, `db_pedidos_qa`, `db_pedidos_prod`
- **Script**: Ejecutar `database/script.sql`

### Servicios de Infraestructura
- **Registry Service**: http://localhost:8761 (Eureka)
- **Config Server**: http://localhost:8888
- **Kafka**: Event streaming platform
- **Gateway**: API Gateway en puerto 8080
- **MS Productos**: Validación de productos

### API REST - Pedidos

#### GET /api/pedidos
Obtener todos los pedidos
```bash
curl http://localhost:8080/api/pedidos
```

#### GET /api/pedidos/{id}
Obtener pedido por ID
```bash
curl http://localhost:8080/api/pedidos/1
```

#### POST /api/pedidos
Crear nuevo pedido
```bash
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "cliente": {"nombre": "Juan Pérez", "email": "juan@email.com"},
    "direccionEntrega": {"calle": "Av. Principal 123", "ciudad": "Lima"},
    "detalles": [
      {"productoId": 1, "cantidad": 2, "precioUnitario": 1299.99}
    ]
  }'
```
