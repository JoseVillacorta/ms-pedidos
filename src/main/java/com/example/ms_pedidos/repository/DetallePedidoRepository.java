package com.example.ms_pedidos.repository;

import com.example.ms_pedidos.entities.DetallePedido;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends ReactiveCrudRepository<DetallePedido, Long> {
}
