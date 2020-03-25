package com.epam.esm.validation;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CertificateValidator {

    private static final String NAME_REGEX = "\\w{1,25}";
    private static final String DESCRIPTION_REGEX = "^[A-Za-z0-9\\s\\.\\,]{10,240}$";
    private static final String PRICE_REGEX = "[0-9]+([,.][0-9]{1,2})?";
    private static final int MIN_DAYS = 1;
    private static final int MAX_DAYS = 999999;

    public boolean checkName(String name) {
        return name.trim().matches(NAME_REGEX);
    }

    public boolean checkDescription(String description) {
        return description.trim().matches(DESCRIPTION_REGEX);
    }

    public boolean checkPrice(BigDecimal price) {
        return price.toString().matches(PRICE_REGEX) && price.doubleValue() > 0;
    }

    public boolean checkDurationDays(int durationDays) {
        return durationDays >= MIN_DAYS && durationDays <= MAX_DAYS;
    }
}
