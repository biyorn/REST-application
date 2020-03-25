package com.epam.esm.mapper;

import com.epam.esm.dto.RoleDTO;
import com.epam.esm.entity.impl.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleModelMapper extends CommonModelMapper<Role, RoleDTO> {

    @Autowired
    public RoleModelMapper(ModelMapper modelMapper) {
        super(modelMapper, Role.class, RoleDTO.class);
    }
}
