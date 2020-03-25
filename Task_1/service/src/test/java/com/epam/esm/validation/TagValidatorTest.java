package com.epam.esm.validation;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagValidatorTest {

    private TagValidator validator = new TagValidator();

    @Test
    public void checkTitle_TitleLessThanTwenty_True() {
        String title = "testTitle";

        boolean actual = validator.checkTitle(title);

        assertTrue(actual);
    }

    @Test
    public void checkTitle_TitleEmpty_False() {
        String title = "";

        boolean actual = validator.checkTitle(title);

        assertFalse(actual);
    }
}
