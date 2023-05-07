package com.example.reactiveprogramming.utils;

import com.example.reactiveprogramming.dto.SportDto;
import com.example.reactiveprogramming.model.Sport;
import org.springframework.beans.BeanUtils;

public class SportMapper {
    private SportMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static SportDto entityToDto(Sport sport) {
        SportDto sportDto = new SportDto();
        BeanUtils.copyProperties(sport, sportDto);
        return sportDto;
    }

    public static Sport dtoToEntity(SportDto sportDto) {
        Sport sport = new Sport();
        BeanUtils.copyProperties(sportDto, sport);
        return sport;
    }
}
