package com.example.ms_pedidos.handler;

import com.example.ms_pedidos.entities.Pedido;
import com.example.ms_pedidos.service.PedidoService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Component
public class PedidoHandler {
    private final PedidoService service;

    public PedidoHandler(PedidoService service) {
        this.service = service;
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), Pedido.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return service.findById(id)
                .flatMap(pedido -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(pedido))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Pedido.class)
                .flatMap(service::save)
                .flatMap(pedido -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(pedido));
    }

    public Mono<ServerResponse> updateEstado(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        String estado = request.queryParam("estado").orElse("PENDIENTE");
        return service.updateEstado(id, estado)
                .flatMap(pedido -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(pedido))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return service.delete(id).then(ServerResponse.noContent().build());
    }
}
