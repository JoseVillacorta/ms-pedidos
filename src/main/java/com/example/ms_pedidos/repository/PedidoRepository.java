package com.example.ms_pedidos.repository;

import com.example.ms_pedidos.entity.Pedido;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PedidoRepository extends R2dbcRepository<Pedido, Long> {

    @Query("SELECT * FROM pedidos WHERE cliente = :cliente")
    Flux<Pedido> findByCliente(@Param("cliente") String cliente);

    @Query("SELECT * FROM pedidos WHERE estado = :estado")
    Flux<Pedido> findByEstado(@Param("estado") String estado);

    @Query("SELECT * FROM pedidos WHERE estado = 'PENDIENTE'")
    Flux<Pedido> findPedidosPendientes();
}