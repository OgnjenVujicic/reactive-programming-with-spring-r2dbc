package com.example.reactiveprogramming.controller;

import com.example.reactiveprogramming.dto.SportDto;
import com.example.reactiveprogramming.service.SportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1/sports")
@RequiredArgsConstructor
public class SportController {
    private final SportService sportService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SportDto> save(@RequestBody SportDto sport) {
        return sportService.saveSport(sport);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<SportDto> findByName(@RequestParam("name") String name) {
        return sportService.findByName(name);
    }
}
