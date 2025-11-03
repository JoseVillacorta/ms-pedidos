package com.example.ms_pedidos.router;

import com.example.ms_pedidos.handler.PedidoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class PedidoRouter {

    @Bean
    public RouterFunction<ServerResponse> pedidoRoutes(PedidoHandler handler) {
        return RouterFunctions.route()
                .GET("/api/pedidos", handler::obtenerTodos)
                .GET("/api/pedidos/{id}", handler::obtenerPorId)
                .POST("/api/pedidos", handler::crear)
                .PUT("/api/pedidos/{id}/estado", handler::actualizarEstado)
                .DELETE("/api/pedidos/{id}", handler::eliminar)
                .GET("/api/pedidos/buscar/cliente", handler::obtenerPorCliente)
                .GET("/api/pedidos/buscar/estado", handler::obtenerPorEstado)
                .build();
    }
}
