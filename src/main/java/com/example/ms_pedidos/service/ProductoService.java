package com.example.ms_pedidos.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

@Service
public class ProductoService {
    private final WebClient webClient;

    public ProductoService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8083").build();
    }

    public Mono<Map<String, Object>> obtenerProducto(Long id) {
        return webClient.get().uri("/api/productos/{id}", id)
                .retrieve().bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Mono<Void> actualizarStock(Long id, Integer cantidad) {
        return webClient.put().uri("/api/productos/{id}/stock", id)
                .bodyValue(Map.of("stock", cantidad))
                .retrieve().toBodilessEntity().then();
    }
}
