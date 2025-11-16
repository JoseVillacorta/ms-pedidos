package com.example.ms_pedidos.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("detalle_pedidos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetallePedido {
    @Id
    private Long id;
    private Long pedidoId;
    private Long productoId;
    private Integer cantidad;
    private Double precioUnitario;
}