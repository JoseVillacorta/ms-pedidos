package com.example.ms_pedidos.handler;

import com.example.ms_pedidos.entity.Pedido;
import com.example.ms_pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PedidoHandler {

    private final PedidoService pedidoService;

    public Mono<ServerResponse> obtenerTodos(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(pedidoService.obtenerTodos(), Pedido.class);
    }

    public Mono<ServerResponse> obtenerPorId(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return pedidoService.obtenerPorId(id)
                .flatMap(pedido -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(pedido))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> crear(ServerRequest request) {
        return request.bodyToMono(Pedido.class)
                .flatMap(pedidoService::crear)
                .flatMap(pedido -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(pedido))
                .onErrorResume(e -> ServerResponse.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue("Error: " + e.getMessage()));
    }

    public Mono<ServerResponse> actualizarEstado(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        String estado = request.queryParam("estado").orElse("PENDIENTE");

        return pedidoService.actualizarEstado(id, estado)
                .flatMap(pedido -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(pedido))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> eliminar(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return pedidoService.eliminar(id)
                .flatMap(eliminado -> eliminado ?
                        ServerResponse.noContent().build() :
                        ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> obtenerPorCliente(ServerRequest request) {
        String cliente = request.queryParam("cliente").orElse("");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(pedidoService.obtenerPorCliente(cliente), Pedido.class);
    }

    public Mono<ServerResponse> obtenerPorEstado(ServerRequest request) {
        String estado = request.queryParam("estado").orElse("PENDIENTE");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(pedidoService.obtenerPorEstado(estado), Pedido.class);
    }
}
