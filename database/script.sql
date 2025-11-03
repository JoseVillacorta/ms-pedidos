-- =========================================
-- SCRIPTS PARA MS-PEDIDOS
-- =========================================

-- Crear tabla pedidos
CREATE TABLE IF NOT EXISTS pedidos (
    id BIGSERIAL PRIMARY KEY,
    cliente VARCHAR(255) NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL DEFAULT 0,
    estado VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE'
);

-- Crear tabla detalle_pedidos
CREATE TABLE IF NOT EXISTS detalle_pedidos (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE
);

-- Insertar datos de prueba
INSERT INTO pedidos (cliente, fecha, total, estado) VALUES
('Juan Pérez', CURRENT_TIMESTAMP, 1200.00, 'PENDIENTE'),
('María García', CURRENT_TIMESTAMP, 350.00, 'PROCESADO'),
('Carlos López', CURRENT_TIMESTAMP, 80.00, 'CANCELADO');

-- Insertar detalles de prueba
INSERT INTO detalle_pedidos (pedido_id, producto_id, cantidad, precio_unitario) VALUES
(1, 1, 1, 1200.00),  -- Laptop Dell
(2, 4, 1, 350.00),   -- Monitor Samsung
(3, 3, 1, 80.00);    -- Teclado Mecánico

-- Verificar datos
SELECT 'Pedidos:' as tabla, COUNT(*) as total FROM pedidos
UNION ALL
SELECT 'Detalles:', COUNT(*) FROM detalle_pedidos;
