package com.example.ms_pedidos.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.Transient;

@Table("pedidos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    @Id
    private Long id;
    private String cliente;
    private LocalDateTime fecha;
    private Double total;
    private String estado;
    @Transient
    private List<DetallePedido> detalles;
}
