package com.epam.esm.mapper;

import com.epam.esm.dto.BoughtCertificateDTO;
import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.entity.impl.BoughtCertificate;
import com.epam.esm.entity.impl.Certificate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BoughtCertificateModelMapperTest {

    private static final Long ID = 1L;
    private static final Certificate CERTIFICATE = new Certificate();
    private static final CertificateDTO CERTIFICATE_DTO = new CertificateDTO();
    private static final BigDecimal PRICE = new BigDecimal("22.2");

    @InjectMocks
    private BoughtCertificateModelMapper boughtCertificateModelMapper;
    @Mock
    private ModelMapper modelMapper;
    private BoughtCertificate boughtCertificate;
    private BoughtCertificateDTO boughtCertificateDTO;
    private Class<BoughtCertificate> entity = BoughtCertificate.class;
    private Class<BoughtCertificateDTO> entityDTO = BoughtCertificateDTO.class;

    @Before
    public void init() {
        boughtCertificate = getBoughtCertificate(ID, CERTIFICATE, PRICE);
        boughtCertificateDTO = getBoughtCertificateDto(ID, CERTIFICATE_DTO, PRICE);
    }

    @Test
    public void toEntityFromDto_DtoObjectSupplied_Entity() {
        when(modelMapper.map(boughtCertificateDTO, (Type) entity)).thenReturn(boughtCertificate);

        BoughtCertificate actual = boughtCertificateModelMapper.toEntity(boughtCertificateDTO);

        assertEquals(boughtCertificate, actual);
    }

    @Test
    public void toDtoFromEntity_EntityObjectSupplied_Dto() {
        when(modelMapper.map(boughtCertificate, (Type) entityDTO)).thenReturn(boughtCertificateDTO);

        BoughtCertificateDTO actual = boughtCertificateModelMapper.toDto(boughtCertificate);

        assertEquals(boughtCertificateDTO, actual);
    }

    private BoughtCertificate getBoughtCertificate(Long id, Certificate certificate, BigDecimal price) {
        BoughtCertificate local = new BoughtCertificate();
        local.setId(id);
        local.setCertificate(certificate);
        local.setPrice(price);
        return local;
    }

    private BoughtCertificateDTO getBoughtCertificateDto(Long id, CertificateDTO certificateDTO, BigDecimal price) {
        BoughtCertificateDTO local = new BoughtCertificateDTO();
        local.setId(id);
        local.setCertificate(certificateDTO);
        local.setPrice(price);
        return local;
    }
}
