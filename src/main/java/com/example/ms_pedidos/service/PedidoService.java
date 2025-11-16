package com.example.ms_pedidos.service;

import com.example.ms_pedidos.entities.DetallePedido;
import com.example.ms_pedidos.entities.Pedido;
import com.example.ms_pedidos.repository.DetallePedidoRepository;
import com.example.ms_pedidos.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PedidoService {
    private final PedidoRepository repository;
    private final DetallePedidoRepository detalleRepository;
    private final ProductoService productoService;

    public PedidoService(PedidoRepository repository, DetallePedidoRepository detalleRepository, ProductoService productoService) {
        this.repository = repository;
        this.detalleRepository = detalleRepository;
        this.productoService = productoService;
    }

    public Flux<Pedido> findAll() {
        return repository.findAll();
    }

    public Mono<Pedido> findById(Long id) {
        return repository.findById(id);
    }

    public Mono<Pedido> save(Pedido pedido) {
        // Validación y cálculo igual
        return Flux.fromIterable(pedido.getDetalles())
                .flatMap(detalle -> productoService.obtenerProducto(detalle.getProductoId())
                        .doOnNext(producto -> {
                            Integer stock = (Integer) producto.get("stock");
                            Double precio = (Double) producto.get("precio");
                            if (stock < detalle.getCantidad()) {
                                throw new RuntimeException("Stock insuficiente");
                            }
                            detalle.setPrecioUnitario(precio);
                        }))
                .then(Mono.fromCallable(() -> {
                    double total = pedido.getDetalles().stream()
                            .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
                            .sum();
                    pedido.setTotal(total);
                    pedido.setEstado("PENDIENTE");
                    return pedido;
                }))
                .flatMap(repository::save)
                .flatMap(savedPedido -> {
                    // Setear pedidoId y guardar detalles
                    savedPedido.getDetalles().forEach(d -> d.setPedidoId(savedPedido.getId()));
                    return detalleRepository.saveAll(savedPedido.getDetalles())
                            .then(Mono.just(savedPedido));
                })
                .doOnNext(saved -> Flux.fromIterable(saved.getDetalles())
                        .flatMap(detalle -> productoService.actualizarStock(detalle.getProductoId(), -detalle.getCantidad()))
                        .subscribe());

    }

    public Mono<Pedido> updateEstado(Long id, String estado) {
        return repository.findById(id)
                .flatMap(pedido -> {
                    pedido.setEstado(estado);
                    return repository.save(pedido);
                });
    }

    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }
}
