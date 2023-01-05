package com.epam.esm.domain.entity.mapper;

import java.util.List;
import java.util.stream.Collectors;

public class RequestParametersMapper {
    public String mapSortParameters(List<String> sortParameters) {
        return sortParameters.stream()
                .map(p -> p.replace(':', ' '))
                .collect(Collectors.joining(","));
    }
}
