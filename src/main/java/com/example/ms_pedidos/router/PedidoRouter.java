package com.example.ms_pedidos.router;

import com.example.ms_pedidos.handler.PedidoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class PedidoRouter {

    @Bean
    public RouterFunction<ServerResponse> route(PedidoHandler handler) {
        return RouterFunctions.route()
                .GET("/api/pedidos", handler::getAll)
                .GET("/api/pedidos/{id}", handler::getById)
                .POST("/api/pedidos", handler::create)
                .PUT("/api/pedidos/{id}/estado", handler::updateEstado)
                .DELETE("/api/pedidos/{id}", handler::delete)
                .build();
    }
}
