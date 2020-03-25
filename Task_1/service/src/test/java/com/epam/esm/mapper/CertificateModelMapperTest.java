package com.epam.esm.mapper;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.entity.impl.Certificate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Type;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CertificateModelMapperTest {

    private static final Long ID = 1L;
    private static final String NAME = "Test";

    @InjectMocks
    private CertificateModelMapper certificateModelMapper;
    @Mock
    private ModelMapper modelMapper;
    private Certificate certificate;
    private CertificateDTO certificateDTO;
    private Class<Certificate> entity = Certificate.class;
    private Class<CertificateDTO> entityDTO = CertificateDTO.class;

    @Before
    public void init() {
        certificate = getCertificate(ID, NAME);
        certificateDTO = getCertificateDTO(ID, NAME);
    }

    @Test
    public void toEntityFromDto_DtoObjectSupplied_Entity() {
        when(modelMapper.map(certificateDTO, (Type) entity)).thenReturn(certificate);

        Certificate actual = certificateModelMapper.toEntity(certificateDTO);

        assertEquals(certificate, actual);
    }

    @Test
    public void toDtoFromEntity_EntityObjectSupplied_Dto() {
        when(modelMapper.map(certificate, (Type) entityDTO)).thenReturn(certificateDTO);

        CertificateDTO actual = certificateModelMapper.toDto(certificate);

        assertEquals(certificateDTO, actual);
    }

    private Certificate getCertificate(Long id, String name) {
        Certificate local = new Certificate();
        local.setId(id);
        local.setName(name);
        return local;
    }

    private CertificateDTO getCertificateDTO(Long id, String name) {
        CertificateDTO local = new CertificateDTO();
        local.setId(id);
        local.setName(name);
        return local;
    }
}
