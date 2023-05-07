package com.example.reactiveprogramming.service;

import com.example.reactiveprogramming.dto.SportDto;
import com.example.reactiveprogramming.repository.SportRepository;
import com.example.reactiveprogramming.utils.SportMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class SportService {
    private final SportRepository sportRepository;

    public Mono<SportDto> saveSport(SportDto sport) {
       return sportRepository.findByName(sport.getName())
                .doOnNext(s -> {
                    if (s != null && !s.getName().isEmpty()) {
                        throw new IllegalArgumentException("Sport Already exist. " + s.getName());
                    }
                })
               .then(sportRepository.save(SportMapper.dtoToEntity(sport))
                        .map(SportMapper::entityToDto));
    }

    public Mono<SportDto> findByName(String name) {
        return sportRepository.findByName(name).map(SportMapper::entityToDto);
    }

}
