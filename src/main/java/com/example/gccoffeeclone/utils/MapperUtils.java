package com.example.gccoffeeclone.utils;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

public class MapperUtils {

    private static final ModelMapper modelMapper = new ModelMapper();

    private MapperUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <S, T> List<T> mapList(List<S> source,
        Class<T> targetClass) {
        return source
            .stream()
            .map(element -> modelMapper.map(element, targetClass))
            .collect(Collectors.toList());
    }

    public static ModelMapper getModelMapper() {
        return modelMapper;
    }
}
