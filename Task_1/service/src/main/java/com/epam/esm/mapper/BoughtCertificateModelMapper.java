package com.epam.esm.mapper;

import com.epam.esm.dto.BoughtCertificateDTO;
import com.epam.esm.entity.impl.BoughtCertificate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoughtCertificateModelMapper extends CommonModelMapper<BoughtCertificate, BoughtCertificateDTO> {

    @Autowired
    public BoughtCertificateModelMapper(ModelMapper modelMapper) {
        super(modelMapper, BoughtCertificate.class, BoughtCertificateDTO.class);
    }
}
