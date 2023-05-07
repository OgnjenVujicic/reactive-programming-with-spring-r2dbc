package com.example.reactiveprogramming.routerfunction;

import com.example.reactiveprogramming.dto.SportDto;
import com.example.reactiveprogramming.service.SportService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@RequiredArgsConstructor
public class SportFunctionalConfig {
    private final SportService sportService;

    @Bean
    public RouterFunction<ServerResponse> saveSportRoute() {
        return RouterFunctions.route()
                .POST("/v2/sports", accept(MediaType.APPLICATION_JSON),
                request ->{
                    Mono<SportDto> sport = request.bodyToMono(SportDto.class).flatMap(sportService::saveSport);
                    return ok().body(sport, SportDto.class);
                }
        ).build();
    }

    @Bean
    public RouterFunction<ServerResponse> saveSportRouteTest() {
        return route(POST("/v2/sport-save-2"), req -> req.bodyToMono(SportDto.class)
                .flatMap(sportService::saveSport)
                .then(ok().build()));
    }

    @Bean
    public RouterFunction<ServerResponse> getStudentRoute() {
        return route(GET("/v2/sports/{name}"),
                request -> {
                    String name = request.pathVariable("name");
                    return ok().body(sportService
                            .findByName(name), SportDto.class);
                }
        );
    }

}
