package com.example.reactiveprogramming.service;

import com.example.reactiveprogramming.dto.SportDto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Slf4j
public class SportConsumer {

    private final SportService sportService;

    @PostConstruct
    public void getSports() {
        WebClient webClient = WebClient.create();

        webClient.mutate()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build().get()
                .uri("https://sports.api.decathlon.com/sports")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jsonObject -> jsonObject.get("data"))
                .flatMapMany(Flux::fromIterable)
                .doOnRequest(n -> log.info("Requesting {} items from upstream", n))
                .limitRate(20)
                .doOnNext(item -> log.info("Received item {}", item))
                .doOnNext(item -> {
                    var attr = item.get("attributes");
                    if( attr != null && attr.get("name") != null) {
                        var sport = new SportDto();
                        sport.setName(attr.get("name").toString().replace("\"", ""));
                        sportService.saveSport(sport)
                                .subscribe(e -> log.info("saved sport: {}", e));
                    }
                })
                .doOnComplete(() -> log.info("Upstream stream completed"))
                .subscribe();
    }
}
