package com.epam.esm.mapper;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.impl.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagModelMapper extends CommonModelMapper<Tag, TagDTO> {

    @Autowired
    public TagModelMapper(ModelMapper modelMapper) {
        super(modelMapper, Tag.class, TagDTO.class);
    }
}
