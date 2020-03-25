package com.epam.esm.validation;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TagValidator {

    private static final String TITLE_REGEX = "\\w{1,20}";

    public boolean checkTitle(String name) {
        return Objects.nonNull(name) && name.trim().matches(TITLE_REGEX);
    }
}
