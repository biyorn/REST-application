package com.epam.esm.mapper;

import com.epam.esm.dto.BoughtCertificatePaginationDTO;
import com.epam.esm.entity.impl.BoughtCertificatePagination;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoughtCertificatePaginationModelMapper extends
        CommonModelMapper<BoughtCertificatePagination, BoughtCertificatePaginationDTO> {

    @Autowired
    public BoughtCertificatePaginationModelMapper(ModelMapper modelMapper) {
        super(modelMapper, BoughtCertificatePagination.class, BoughtCertificatePaginationDTO.class);
    }
}
