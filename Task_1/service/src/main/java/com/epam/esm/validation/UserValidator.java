package com.epam.esm.validation;

import com.epam.esm.dto.UserEntityDTO;
import com.epam.esm.exception.IncorrectParameterException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserValidator {

    private static final String LOGIN_REGEX = "^[A-Za-z0-9]{5,30}$";
    private static final String PASSWORD_REGEX = "^[A-Za-z0-9]{5,}$";
    private static final String FIRST_NAME_REGEX = "^[A-Z][a-z]{1,20}$";
    private static final String LAST_NAME_REGEX = "^[A-Z][a-z]{1,20}$";

    public void isValid(UserEntityDTO userEntityDTO) {
        String login = userEntityDTO.getLogin();
        if(Objects.isNull(login) || !login.matches(LOGIN_REGEX)) {
            throw new IncorrectParameterException("You entered incorrect login - " + login);
        }
        String password = userEntityDTO.getPassword();
        if(Objects.isNull(password) || !password.matches(PASSWORD_REGEX)) {
            throw new IncorrectParameterException("You entered incorrect password - " + password);
        }
        String firstName = userEntityDTO.getFirstName();
        if(Objects.isNull(firstName) || !firstName.matches(FIRST_NAME_REGEX)) {
            throw new IncorrectParameterException("You entered incorrect first name - " + firstName);
        }
        String lastName = userEntityDTO.getLastName();
        if(Objects.isNull(lastName) || !lastName.matches(LAST_NAME_REGEX)) {
            throw new IncorrectParameterException("You entered incorrect last name - " + lastName);
        }
    }
}
