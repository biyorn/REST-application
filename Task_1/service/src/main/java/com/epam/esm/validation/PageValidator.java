package com.epam.esm.validation;

import com.epam.esm.exception.IncorrectParameterException;
import org.springframework.stereotype.Component;

@Component
public class PageValidator {

    public void verifyPageNumbers(int pageNum, int pageSize) {
        if(pageNum <= 0 || (pageSize <= 0)) {
            throw new IncorrectParameterException(
                    "Page num - [" + pageNum + "] and page size - [" + pageSize + "] both should be more than 0");
        }
    }
}
