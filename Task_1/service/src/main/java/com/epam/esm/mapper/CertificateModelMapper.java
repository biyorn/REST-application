package com.epam.esm.mapper;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.entity.impl.Certificate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CertificateModelMapper extends CommonModelMapper<Certificate, CertificateDTO> {

    @Autowired
    public CertificateModelMapper(ModelMapper modelMapper) {
        super(modelMapper, Certificate.class, CertificateDTO.class);
    }
}
