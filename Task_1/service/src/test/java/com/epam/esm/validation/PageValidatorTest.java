package com.epam.esm.validation;

import com.epam.esm.exception.IncorrectParameterException;
import org.junit.Test;

public class PageValidatorTest {

    private static final int PAGE_NUM = 1;
    private static final int PAGE_SIZE = 1;

    private static final int INVALID_PAGE_NUM = -1;
    private static final int INVALID_PAGE_SIZE = -1;

    private PageValidator pageValidator = new PageValidator();


    @Test
    public void verifyPageNumbers_ValidParametersSupplied_Nothing() {
        pageValidator.verifyPageNumbers(PAGE_NUM, PAGE_SIZE);
    }

    @Test(expected = IncorrectParameterException.class)
    public void verifyPageNumbers_InvalidPageNum_Exception() {
        pageValidator.verifyPageNumbers(INVALID_PAGE_NUM, PAGE_SIZE);
    }

    @Test(expected = IncorrectParameterException.class)
    public void verifyPageNumbers_InvalidPageSize_Exception() {
        pageValidator.verifyPageNumbers(PAGE_NUM, INVALID_PAGE_SIZE);
    }

    @Test(expected = IncorrectParameterException.class)
    public void verifyPageNumbers_InvalidParametersSupplied_Exception() {
        pageValidator.verifyPageNumbers(INVALID_PAGE_NUM, INVALID_PAGE_SIZE);
    }
}
