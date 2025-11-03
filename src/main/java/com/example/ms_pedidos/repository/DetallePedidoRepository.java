package com.example.ms_pedidos.repository;

import com.example.ms_pedidos.entity.DetallePedido;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DetallePedidoRepository extends R2dbcRepository<DetallePedido, Long> {

    Flux<DetallePedido> findByPedidoId(Long pedidoId);

    Mono<Void> deleteByPedidoId(Long pedidoId);
}