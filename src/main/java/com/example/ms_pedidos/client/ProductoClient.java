package com.example.ms_pedidos.client;

import com.example.ms_pedidos.dto.ProductoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ProductoClient {

    private final WebClient webClient;

    public ProductoClient(@Value("${ms-productos.url:http://localhost:8081}") String msProductosUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(msProductosUrl)
                .build();
    }

    public Mono<ProductoDTO> obtenerProducto(Long id) {
        return webClient.get()
                .uri("/api/productos/{id}", id)
                .retrieve()
                .bodyToMono(ProductoDTO.class);
    }

    public Mono<Void> actualizarStock(Long id, Integer cantidad) {
        return webClient.put()
                .uri("/api/productos/{id}/stock?cantidad={cantidad}", id, cantidad)
                .retrieve()
                .bodyToMono(Void.class);
    }
}