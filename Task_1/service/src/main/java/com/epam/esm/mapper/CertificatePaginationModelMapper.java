package com.epam.esm.mapper;

import com.epam.esm.dto.CertificatePaginationDTO;
import com.epam.esm.entity.impl.CertificatePagination;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CertificatePaginationModelMapper extends CommonModelMapper<CertificatePagination, CertificatePaginationDTO> {

    @Autowired
    public CertificatePaginationModelMapper(ModelMapper modelMapper) {
        super(modelMapper, CertificatePagination.class, CertificatePaginationDTO.class);
    }
}
