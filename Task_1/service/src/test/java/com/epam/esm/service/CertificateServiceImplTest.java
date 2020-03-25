package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.SearchParamsDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.impl.Certificate;
import com.epam.esm.entity.impl.SearchParams;
import com.epam.esm.entity.impl.Tag;
import com.epam.esm.exception.FailedAddObjectException;
import com.epam.esm.exception.IncorrectParameterException;
import com.epam.esm.exception.NotFoundObjectException;
import com.epam.esm.mapper.CommonModelMapper;
import com.epam.esm.repository.impl.CertificateRepositoryImpl;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import com.epam.esm.service.impl.CertificateServiceImpl;
import com.epam.esm.specification.predicate.impl.certificate.factory.CertificatePredicateSpecificationFactory;
import com.epam.esm.specification.predicate.impl.tag.factory.TagSpecificationFactory;
import com.epam.esm.validation.CertificateDTOPatchValidator;
import com.epam.esm.validation.CertificateDtoValidator;
import com.epam.esm.validation.CertificateValidator;
import com.epam.esm.validation.PageValidator;
import com.epam.esm.validation.TagValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CertificateServiceImplTest {

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 1;

    private static final Long FIRST_ID = 1L;
    private static final String FIRST_NAME = "first";
    private static final String FIRST_DESCRIPTION = "First description";
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2020, Month.JANUARY, 14, 19, 16);
    private static final BigDecimal PRICE = new BigDecimal(22);
    private static final int DURATION_DAYS = 30;
    private Tag tag;
    private Set<Tag> tags;
    private TagDTO tagDTO;
    private Set<TagDTO> tagDTOSet;

    private Certificate certificate;
    private CertificateDTO certificateDTO;

    @InjectMocks
    private CertificateServiceImpl service;
    @Mock
    private CertificateRepositoryImpl certificateRepository;
    @Mock
    private TagRepositoryImpl tagRepository;
    @Mock
    private CertificateDtoValidator certificateDtoValidator;
    @Mock
    private CertificateValidator certificateValidator;
    @Mock
    private TagValidator tagValidator;
    @Mock
    private PageValidator pageValidator;
    @Mock
    private CertificateDTOPatchValidator certificateDTOPatchValidator;
    @Mock
    private CommonModelMapper<SearchParams, SearchParamsDTO> searchParamsModelMapper;
    @Mock
    private CommonModelMapper<Certificate, CertificateDTO> certificateModelMapper;
    @Mock
    private CertificatePredicateSpecificationFactory certificatePredicateSpecificationFactory;
    @Mock
    private TagSpecificationFactory tagSpecificationFactory;

    @Before
    public void init() {
        tag = new Tag();
        tag.setId(FIRST_ID);
        tag.setTitle(FIRST_NAME);

        tagDTO = new TagDTO();
        tagDTO.setId(FIRST_ID);
        tagDTO.setTitle(FIRST_NAME);

        certificate = getCertificate(FIRST_ID, FIRST_NAME, FIRST_DESCRIPTION,
                PRICE, LOCAL_DATE_TIME, LOCAL_DATE_TIME, DURATION_DAYS);
        certificateDTO = getCertificateDTO(FIRST_ID, FIRST_NAME, FIRST_DESCRIPTION,
                PRICE, LOCAL_DATE_TIME, LOCAL_DATE_TIME, DURATION_DAYS);

        tags = new HashSet<>(Collections.singletonList(tag));
        tagDTOSet = new HashSet<>(Collections.singletonList(tagDTO));
    }

    @Test
    public void getById_FirstIdSupplied_ShouldReturnFirstCertificate() {
        certificateDTO.setTags(tagDTOSet);

        when(certificateRepository.query(certificatePredicateSpecificationFactory
                .getCertificatePredicateSpecificationById(FIRST_ID), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.singletonList(certificate));
        when(certificateModelMapper.toDto(certificate)).thenReturn(certificateDTO);

        CertificateDTO actual = service.getById(FIRST_ID);

        assertNotNull(actual);
        verify(certificatePredicateSpecificationFactory, times(1)).getCertificatePredicateSpecificationById(FIRST_ID);
        verify(certificateModelMapper, times(1)).toDto(certificate);
        assertEquals(certificateDTO, actual);
    }

    @Test
    public void getCertificates_ParamsNull_ReturnAllTags() {
        List<Certificate> listCertificates = Collections.singletonList(certificate);
        List<CertificateDTO> expected = Collections.singletonList(certificateDTO);
        SearchParamsDTO searchParamsDTO = new SearchParamsDTO();

        doNothing().when(pageValidator).verifyPageNumbers(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
        when(certificateRepository.query(certificatePredicateSpecificationFactory
                .getCertificatePredicateSpecificationGetAll(), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(listCertificates);
        when(certificateModelMapper.toDto(certificate)).thenReturn(certificateDTO);

        List<CertificateDTO> actual = service.getCertificates(searchParamsDTO, DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);

        assertThat(actual, is(expected));
        assertThat(actual, hasSize(1));
    }

    @Test
    public void addCertificate_WellEntitySupplied_CertificateDto() {
        certificateDTO.setTags(tagDTOSet);
        certificate.setTags(tags);

        doNothing().when(certificateDtoValidator).isValid(certificateDTO);
        when(certificateValidator.checkName(anyString())).thenReturn(true);
        when(certificateValidator.checkDescription(anyString())).thenReturn(true);
        when(certificateValidator.checkPrice(anyObject())).thenReturn(true);
        when(certificateValidator.checkDurationDays(anyInt())).thenReturn(true);
        when(tagValidator.checkTitle(anyString())).thenReturn(true);

        when(certificateModelMapper.toEntity(certificateDTO)).thenReturn(certificate);
        when(tagRepository.query(tagSpecificationFactory
                .getTagSpecificationGetExistTags(certificate.getTags()), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.singletonList(tag));
        when(certificateRepository.add(certificate)).thenReturn(certificate);
        when(certificateModelMapper.toDto(certificate)).thenReturn(certificateDTO);

        CertificateDTO actual = service.addCertificate(certificateDTO);

        assertEquals(actual, certificateDTO);
    }

    @Test(expected = FailedAddObjectException.class)
    public void addCertificate_RepositoryReturnException_Exception() {
        certificateDTO.setTags(tagDTOSet);

        doNothing().when(certificateDtoValidator).isValid(certificateDTO);

        when(certificateModelMapper.toEntity(certificateDTO)).thenReturn(certificate);
        when(tagRepository.query(tagSpecificationFactory
                .getTagSpecificationGetExistTags(certificate.getTags()), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.singletonList(tag));
        doThrow(new RuntimeException()).when(certificateRepository).add(certificate);

        service.addCertificate(certificateDTO);
    }

    @Test
    public void updateCertificate_WellParametersSupplied_ReturnNothing() {
        int number = 1;

        certificateDTO.setTags(tagDTOSet);
        certificate.setTags(tags);

        doNothing().when(certificateDtoValidator).isValid(certificateDTO);
        when(certificateModelMapper.toEntity(certificateDTO)).thenReturn(certificate);
        when(certificateRepository.query(certificatePredicateSpecificationFactory
                .getCertificatePredicateSpecificationById(certificate.getId()), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.singletonList(certificate));
        when(tagRepository.query(tagSpecificationFactory
                .getTagSpecificationGetExistTags(certificate.getTags()), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.singletonList(tag));

        service.updateCertificate(FIRST_ID, certificateDTO);

        verify(certificateModelMapper, times(number)).toEntity(certificateDTO);
        verify(certificateRepository, times(number)).update(FIRST_ID, certificate);
    }

    @Test(expected = NotFoundObjectException.class)
    public void updateCertificate_RepositoryNotFoundCertificate_Exception() {
        certificateDTO.setTags(tagDTOSet);
        certificate.setTags(tags);

        doNothing().when(certificateDtoValidator).isValid(certificateDTO);
        when(certificateModelMapper.toEntity(certificateDTO)).thenReturn(certificate);
        doThrow(new RuntimeException()).when(certificateRepository).update(FIRST_ID, certificate);

        service.updateCertificate(FIRST_ID, certificateDTO);
    }

    @Test(expected = IncorrectParameterException.class)
    public void updateCertificate_WrongParametersSupplied_Exception() {
        doThrow(IncorrectParameterException.class).when(certificateDtoValidator).isValid(certificateDTO);

        service.updateCertificate(FIRST_ID, certificateDTO);
    }

    @Test
    public void deleteCertificate_WellParametersSupplied_ReturnNothing() {
        when(certificateRepository.query(certificatePredicateSpecificationFactory
                .getCertificatePredicateSpecificationById(FIRST_ID), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.singletonList(certificate));

        service.deleteCertificate(FIRST_ID);

        verify(certificateRepository, times(1)).delete(FIRST_ID);
    }

    @Test(expected = NotFoundObjectException.class)
    public void deleteCertificate_ReturnNothing_Exception() {
        when(certificateRepository.query(certificatePredicateSpecificationFactory
                .getCertificatePredicateSpecificationById(FIRST_ID), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.emptyList());

        service.deleteCertificate(FIRST_ID);
    }

    private Certificate getCertificate(Long firstId, String firstName, String firstDescription,
                                       BigDecimal price, LocalDateTime creationDate, LocalDateTime modificationDate,
                                       int durationDays) {
        Certificate certificate = new Certificate();
        certificate.setId(firstId);
        certificate.setName(firstName);
        certificate.setDescription(firstDescription);
        certificate.setPrice(price);
        certificate.setCreationDate(creationDate);
        certificate.setModificationDate(modificationDate);
        certificate.setDurationDays(durationDays);
        return certificate;
    }


    private CertificateDTO getCertificateDTO(Long firstId, String firstName, String firstDescription,
                                             BigDecimal price, LocalDateTime creationDate, LocalDateTime modificationDate,
                                             int durationDays) {
        CertificateDTO certificateDTO = new CertificateDTO();
        certificateDTO.setId(firstId);
        certificateDTO.setName(firstName);
        certificateDTO.setDescription(firstDescription);
        certificateDTO.setPrice(price);
        certificateDTO.setCreationDate(creationDate);
        certificateDTO.setModificationDate(modificationDate);
        certificateDTO.setDurationDays(durationDays);
        return certificateDTO;
    }
}
