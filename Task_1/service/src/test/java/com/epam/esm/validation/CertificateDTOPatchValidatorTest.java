package com.epam.esm.validation;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.exception.IncorrectParameterException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CertificateDTOPatchValidatorTest {

    private static final String VALID_NAME = "Valid";
    private static final String VALID_DESC = "Valid description is well";
    private static final BigDecimal VALID_PRICE = new BigDecimal("22.22");
    private static final int VALID_DURATION = 10;
    private static final String INVALID_NAME = "-Invalid-";
    private static final String INVALID_DESC = "Invalid";
    private static final BigDecimal INVALID_PRICE = new BigDecimal("-22.22");
    private static final int INVALID_DURATION = -10;

    @InjectMocks
    private CertificateDTOPatchValidator mainValidator;
    @Mock
    private CertificateValidator certificateValidator;
    @Mock
    private TagValidator tagValidator;
    private CertificateDTO validCertificate;
    private CertificateDTO invalidCertificate;

    @Before
    public void init() {
        validCertificate = getCertificate(VALID_NAME, VALID_DESC, VALID_PRICE, VALID_DURATION);
        invalidCertificate = getCertificate(INVALID_NAME, INVALID_DESC, INVALID_PRICE, INVALID_DURATION);
    }

    @Test
    public void isValid_ValidObjectSupplied_Nothing() {
        String name = validCertificate.getName();
        when(certificateValidator.checkName(name)).thenReturn(true);
        String description = validCertificate.getDescription();
        when(certificateValidator.checkDescription(description)).thenReturn(true);
        BigDecimal price = validCertificate.getPrice();
        when(certificateValidator.checkPrice(price)).thenReturn(true);
        int duration = validCertificate.getDurationDays();
        when(certificateValidator.checkDurationDays(duration)).thenReturn(true);
        when(tagValidator.checkTitle(anyString())).thenReturn(true);

        mainValidator.isValid(validCertificate);

        verify(certificateValidator, times(1)).checkName(name);
        verify(certificateValidator, times(1)).checkDescription(description);
        verify(certificateValidator, times(1)).checkPrice(price);
        verify(certificateValidator, times(1)).checkDurationDays(duration);
        verify(tagValidator, times(0)).checkTitle(anyString());
    }

    @Test(expected = IncorrectParameterException.class)
    public void isValid_InvalidNameSupplied_Exception() {
        when(certificateValidator.checkName(invalidCertificate.getName())).thenReturn(false);

        mainValidator.isValid(invalidCertificate);
    }

    @Test(expected = IncorrectParameterException.class)
    public void isValid_InvalidDescriptionSupplied_Exception() {
        when(certificateValidator.checkName(invalidCertificate.getName())).thenReturn(true);
        when(certificateValidator.checkDescription(invalidCertificate.getDescription())).thenReturn(false);

        mainValidator.isValid(invalidCertificate);
    }

    @Test(expected = IncorrectParameterException.class)
    public void isValid_InvalidPriceSupplied_Exception() {
        when(certificateValidator.checkName(invalidCertificate.getName())).thenReturn(true);
        when(certificateValidator.checkDescription(invalidCertificate.getDescription())).thenReturn(true);
        when(certificateValidator.checkPrice(invalidCertificate.getPrice())).thenReturn(false);

        mainValidator.isValid(invalidCertificate);
    }

    @Test(expected = IncorrectParameterException.class)
    public void isValid_InvalidDurationSupplied_Exception() {
        when(certificateValidator.checkName(invalidCertificate.getName())).thenReturn(true);
        when(certificateValidator.checkDescription(invalidCertificate.getDescription())).thenReturn(true);
        when(certificateValidator.checkPrice(invalidCertificate.getPrice())).thenReturn(true);
        when(certificateValidator.checkDurationDays(invalidCertificate.getDurationDays())).thenReturn(false);

        mainValidator.isValid(invalidCertificate);
    }

    private CertificateDTO getCertificate(String name, String description, BigDecimal price, int duration) {
        CertificateDTO certificateDTO = new CertificateDTO();
        certificateDTO.setName(name);
        certificateDTO.setDescription(description);
        certificateDTO.setPrice(price);
        certificateDTO.setDurationDays(duration);
        return certificateDTO;
    }
}
