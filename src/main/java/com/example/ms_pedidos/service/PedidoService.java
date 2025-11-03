package com.example.ms_pedidos.service;

import com.example.ms_pedidos.client.ProductoClient;
import com.example.ms_pedidos.dto.ProductoDTO;
import com.example.ms_pedidos.entity.DetallePedido;
import com.example.ms_pedidos.entity.Pedido;
import com.example.ms_pedidos.repository.DetallePedidoRepository;
import com.example.ms_pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final ProductoClient productoClient;

    public Flux<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    public Mono<Pedido> obtenerPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public Mono<Pedido> crear(Pedido pedido) {
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        pedido.setTotal(BigDecimal.ZERO);

        // Calcular total y validar stock
        return validarYCalcularTotal(pedido.getDetalles())
                .flatMap(total -> {
                    pedido.setTotal(total);
                    return pedidoRepository.save(pedido);
                })
                .flatMap(pedidoGuardado -> {
                    // Guardar detalles
                    List<DetallePedido> detalles = pedido.getDetalles();
                    detalles.forEach(detalle -> detalle.setPedidoId(pedidoGuardado.getId()));

                    return Flux.fromIterable(detalles)
                            .flatMap(detallePedidoRepository::save)
                            .then(Mono.just(pedidoGuardado));
                })
                .flatMap(pedidoGuardado -> {
                    // Actualizar stock de productos
                    return Flux.fromIterable(pedido.getDetalles())
                            .flatMap(detalle ->
                                    productoClient.actualizarStock(detalle.getProductoId(), -detalle.getCantidad())
                            )
                            .then(Mono.just(pedidoGuardado));
                });
    }

    private Mono<BigDecimal> validarYCalcularTotal(List<DetallePedido> detalles) {
        return Flux.fromIterable(detalles)
                .flatMap(detalle ->
                        productoClient.obtenerProducto(detalle.getProductoId())
                                .map(producto -> {
                                    // Validar stock disponible
                                    if (producto.getStock() < detalle.getCantidad()) {
                                        throw new RuntimeException("Stock insuficiente para producto: " + producto.getNombre());
                                    }
                                    // Calcular precio
                                    detalle.setPrecioUnitario(producto.getPrecio());
                                    return detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad()));
                                })
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Mono<Pedido> actualizarEstado(Long id, String estado) {
        return pedidoRepository.findById(id)
                .flatMap(pedido -> {
                    pedido.setEstado(estado);
                    return pedidoRepository.save(pedido);
                });
    }

    public Mono<Boolean> eliminar(Long id) {
        return pedidoRepository.findById(id)
                .flatMap(pedido -> {
                    // Solo permitir eliminar pedidos PENDIENTES
                    if (!"PENDIENTE".equals(pedido.getEstado())) {
                        return Mono.just(false);
                    }

                    // Devolver stock a productos
                    return Flux.fromIterable(pedido.getDetalles())
                            .flatMap(detalle ->
                                    productoClient.actualizarStock(detalle.getProductoId(), detalle.getCantidad())
                            )
                            .then(detallePedidoRepository.deleteByPedidoId(id))
                            .then(pedidoRepository.deleteById(id))
                            .then(Mono.just(true));
                })
                .defaultIfEmpty(false);
    }

    public Flux<Pedido> obtenerPorCliente(String cliente) {
        return pedidoRepository.findByCliente(cliente);
    }

    public Flux<Pedido> obtenerPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }
}
