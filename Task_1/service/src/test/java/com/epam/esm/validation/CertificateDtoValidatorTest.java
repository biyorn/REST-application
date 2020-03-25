package com.epam.esm.validation;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.IncorrectParameterException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CertificateDtoValidatorTest {

    private static final Long FIRST_ID = 1L;
    private static final String FIRST_NAME = "first";
    private static final String FIRST_DESCRIPTION = "First description";
    private static final BigDecimal PRICE = new BigDecimal(22);
    private static final int DURATION_DAYS = 30;
    private TagDTO tag;
    private CertificateDTO certificateDTO;
    @InjectMocks
    private CertificateDtoValidator certificateDtoValidator;
    @Mock
    private CertificateValidator certificateValidator;
    @Mock
    private TagValidator tagValidator;

    @Before
    public void init() {
        certificateDTO = new CertificateDTO();
        certificateDTO.setId(FIRST_ID);
        certificateDTO.setName(FIRST_NAME);
        certificateDTO.setDescription(FIRST_DESCRIPTION);
        certificateDTO.setPrice(PRICE);
        certificateDTO.setDurationDays(DURATION_DAYS);

        tag = new TagDTO();
        tag.setId(1L);
        tag.setTitle("firstTag");

        certificateDTO.setTags(new HashSet<>(Collections.singletonList(tag)));
    }

    @Test
    public void isValid_AllParamsWell_WithoutExceptions() {
        when(certificateValidator.checkName(anyString())).thenReturn(true);
        when(certificateValidator.checkDescription(anyString())).thenReturn(true);
        when(certificateValidator.checkPrice(anyObject())).thenReturn(true);
        when(certificateValidator.checkDurationDays(anyInt())).thenReturn(true);
        when(tagValidator.checkTitle(anyString())).thenReturn(true);

        certificateDtoValidator.isValid(certificateDTO);

        verify(certificateValidator, times(1)).checkName(anyString());
        verify(certificateValidator, times(1)).checkDescription(anyString());
        verify(certificateValidator, times(1)).checkPrice(anyObject());
        verify(certificateValidator, times(1)).checkDurationDays(anyInt());
        verify(tagValidator, times(1)).checkTitle(anyString());
    }

    @Test(expected = IncorrectParameterException.class)
    public void isValid_CheckNameReturnFalse_Exception() {
        when(certificateValidator.checkName(anyString())).thenReturn(false);
        certificateDtoValidator.isValid(certificateDTO);
    }

    @Test(expected = IncorrectParameterException.class)
    public void isValid_CheckDescriptionReturnFalse_Exception() {
        when(certificateValidator.checkName(anyString())).thenReturn(true);
        when(certificateValidator.checkDescription(anyString())).thenReturn(false);
        certificateDtoValidator.isValid(certificateDTO);
    }

    @Test(expected = IncorrectParameterException.class)
    public void isValid_CheckPriceReturnFalse_Exception() {
        when(certificateValidator.checkName(anyString())).thenReturn(true);
        when(certificateValidator.checkDescription(anyString())).thenReturn(true);
        when(certificateValidator.checkPrice(anyObject())).thenReturn(false);
        certificateDtoValidator.isValid(certificateDTO);
    }

    @Test(expected = IncorrectParameterException.class)
    public void isValid_CheckDurationDaysReturnFalse_Exception() {
        when(certificateValidator.checkName(anyString())).thenReturn(true);
        when(certificateValidator.checkDescription(anyString())).thenReturn(true);
        when(certificateValidator.checkPrice(anyObject())).thenReturn(true);
        when(certificateValidator.checkDurationDays(anyInt())).thenReturn(false);
        certificateDtoValidator.isValid(certificateDTO);
    }

    @Test(expected = IncorrectParameterException.class)
    public void isValid_CheckTitleReturnFalse_Exception() {
        when(certificateValidator.checkName(anyString())).thenReturn(true);
        when(certificateValidator.checkDescription(anyString())).thenReturn(true);
        when(certificateValidator.checkPrice(anyObject())).thenReturn(true);
        when(certificateValidator.checkDurationDays(anyInt())).thenReturn(true);
        when(tagValidator.checkTitle(anyString())).thenReturn(false);

        certificateDtoValidator.isValid(certificateDTO);
    }

}
