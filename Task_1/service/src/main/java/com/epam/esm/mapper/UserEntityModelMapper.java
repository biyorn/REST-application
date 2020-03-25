package com.epam.esm.mapper;

import com.epam.esm.dto.UserEntityDTO;
import com.epam.esm.entity.impl.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEntityModelMapper extends CommonModelMapper<UserEntity, UserEntityDTO> {

    @Autowired
    public UserEntityModelMapper(ModelMapper modelMapper) {
        super(modelMapper, UserEntity.class, UserEntityDTO.class);
    }
}
