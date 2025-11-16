package com.example.ms_pedidos.repository;

import com.example.ms_pedidos.entities.Pedido;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends ReactiveCrudRepository<Pedido, Long> {
}