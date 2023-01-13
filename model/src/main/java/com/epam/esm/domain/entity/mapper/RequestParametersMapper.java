package com.epam.esm.domain.entity.mapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestParametersMapper {
    public String mapSortParameters(List<String> sortParameters) {
        return sortParameters.stream()
                .map(p -> p.replace(':', ' '))
                .collect(Collectors.joining(","));
    }
}
