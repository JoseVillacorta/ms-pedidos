package com.example.ms_pedidos.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Table("pedidos")
public class Pedido {
    @Id
    private Long id;
    private String cliente;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String estado; // PENDIENTE, PROCESADO, CANCELADO

    // Para R2DBC, las relaciones se manejan diferente
    private List<DetallePedido> detalles;
}
