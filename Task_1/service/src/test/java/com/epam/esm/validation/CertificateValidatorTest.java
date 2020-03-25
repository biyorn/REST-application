package com.epam.esm.validation;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CertificateValidatorTest {

    private CertificateValidator validator = new CertificateValidator();

    @Test
    public void checkName_NameLessThanTwenty_True() {
        String name = "firstTest";

        boolean actual = validator.checkName(name);

        assertTrue(actual);
    }

    @Test
    public void checkName_NameMoreThanTwentyFive_False() {
        String name = "nameMoreThanTwentyFiveSymbols";

        boolean actual = validator.checkName(name);

        assertFalse(actual);
    }

    @Test
    public void checkDescription_DescriptionSupplied_True() {
        String description = "sed elementum tempus egestas sed sed risus pretium quam vulputate";

        boolean actual = validator.checkDescription(description);

        assertTrue(actual);
    }

    @Test
    public void checkDescription_DescriptionEmpty_False() {
        String description = "";

        boolean actual = validator.checkDescription(description);

        assertFalse(actual);
    }

    @Test
    public void checkPrice_PriceTwenty_True() {
        BigDecimal price = new BigDecimal(20);

        boolean actual = validator.checkPrice(price);

        assertTrue(actual);
    }

    @Test
    public void checkPrice_PriceNegative_False() {
        BigDecimal price = new BigDecimal(-20);

        boolean actual = validator.checkPrice(price);

        assertFalse(actual);
    }

    @Test
    public void checkDurationDays_DurationDaysEqualOneYear_True() {
        int durationDays = 366;

        boolean actual = validator.checkDurationDays(durationDays);

        assertTrue(actual);
    }

    @Test
    public void checkDurationDays_DurationDaysNegative_False() {
        int durationDays = -1;

        boolean actual = validator.checkDurationDays(durationDays);

        assertFalse(actual);
    }

}
