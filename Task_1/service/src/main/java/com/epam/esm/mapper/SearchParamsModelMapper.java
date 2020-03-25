package com.epam.esm.mapper;

import com.epam.esm.dto.SearchParamsDTO;
import com.epam.esm.entity.impl.SearchParams;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchParamsModelMapper extends CommonModelMapper<SearchParams, SearchParamsDTO> {

    @Autowired
    public SearchParamsModelMapper(ModelMapper modelMapper) {
        super(modelMapper, SearchParams.class, SearchParamsDTO.class);
    }
}
