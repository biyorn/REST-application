package com.epam.esm.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public abstract class CommonModelMapper<T, R> {

    private ModelMapper modelMapper;
    private Class<T> entity;
    private Class<R> entityDTO;

    @Autowired
    public CommonModelMapper(ModelMapper modelMapper, Class<T> entity, Class<R> entityDTO) {
        this.modelMapper = modelMapper;
        this.entity = entity;
        this.entityDTO = entityDTO;
    }

    public T toEntity(R object) {
        return modelMapper.map(object, (Type) entity);
    }

    public R toDto(T object) {
        return modelMapper.map(object, (Type) entityDTO);
    }
}
