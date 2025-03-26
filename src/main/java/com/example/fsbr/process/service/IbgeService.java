package com.example.fsbr.process.service;

import com.example.fsbr.process.dto.IbgeUF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IbgeService {

    private final WebClient webClient;

    @Autowired
    public IbgeService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://servicodados.ibge.gov.br/api/v1/localidades").build();
    }

    public List<IbgeUF> listarUfs() {
        Flux<IbgeUF> ufFlux = webClient.get()
                .uri("/estados")
                .retrieve()
                .bodyToFlux(IbgeUF.class);

        return ufFlux.collect(Collectors.toList()).block();
    }

    public List<String> listarMunicipiosPorUf(String uf) {
        Flux<Object> municipioFlux = webClient.get()
                .uri("/estados/{uf}/municipios", uf)
                .retrieve()
                .bodyToFlux(Object.class);

        return municipioFlux.map(Object::toString).collect(Collectors.toList()).block();
    }

}
