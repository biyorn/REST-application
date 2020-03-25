package com.epam.esm.validation;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.IncorrectParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
public class CertificateDtoValidator {

    private CertificateValidator certificateValidator;
    private TagValidator tagValidator;

    @Autowired
    public CertificateDtoValidator(CertificateValidator certificateValidator, TagValidator tagValidator) {
        this.certificateValidator = certificateValidator;
        this.tagValidator = tagValidator;
    }

    public void isValid(CertificateDTO certificateDTO) {
        String name = certificateDTO.getName();
        if (Objects.isNull(name) || !certificateValidator.checkName(name)) {
            throw new IncorrectParameterException("You entered an invalid name - [" + name + "]");
        }
        String description = certificateDTO.getDescription();
        if (Objects.isNull(description) || !certificateValidator.checkDescription(description)) {
            throw new IncorrectParameterException("Your description is wrong: [" + description + "], should write min 10 symbols");
        }
        BigDecimal price = certificateDTO.getPrice();
        if (Objects.isNull(price) || !certificateValidator.checkPrice(price)) {
            throw new IncorrectParameterException("You entered invalid price - [" + price + "], example - 1.99");
        }
        int durationDays = certificateDTO.getDurationDays();
        if (!certificateValidator.checkDurationDays(durationDays)) {
            throw new IncorrectParameterException("You enter wrong duration of days - [" + durationDays + "]");
        }
        Set<TagDTO> tags = Optional.ofNullable(certificateDTO.getTags())
                .orElseGet(Collections::emptySet);
        tags.forEach(tag -> {
            String title = tag.getTitle();
            if (Objects.isNull(title) || !tagValidator.checkTitle(title)) {
                throw new IncorrectParameterException("You enter wrong tag title - [" + title + "]");
            }
        });
        certificateDTO.setTags(tags);
    }
}
