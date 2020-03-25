package com.epam.esm.validation;

import com.epam.esm.dto.UserEntityDTO;
import com.epam.esm.exception.IncorrectParameterException;
import org.junit.Before;
import org.junit.Test;

public class UserValidatorTest {

    private UserValidator validator;
    private UserEntityDTO validUser;
    private UserEntityDTO invalidUser;

    @Before
    public void init() {
        validator = new UserValidator();
        validUser = getUser("Test12", "Test1234", "Test", "Test");
        invalidUser = getUser("-in", "12", "222", "333");
    }

    @Test
    public void isValid_ValidObjectSupplied_Nothing() {
        validator.isValid(validUser);
    }

    @Test(expected = IncorrectParameterException.class)
    public void isValid_InvalidObjectSupplied_Exception() {
        validator.isValid(invalidUser);
    }

    private UserEntityDTO getUser(String login, String password, String firstName, String lastName) {
        UserEntityDTO userEntityDTO = new UserEntityDTO();
        userEntityDTO.setLogin(login);
        userEntityDTO.setPassword(password);
        userEntityDTO.setFirstName(firstName);
        userEntityDTO.setLastName(lastName);
        return userEntityDTO;
    }
}
