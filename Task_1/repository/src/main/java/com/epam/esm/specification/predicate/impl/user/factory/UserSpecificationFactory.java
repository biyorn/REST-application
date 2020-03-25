package com.epam.esm.specification.predicate.impl.user.factory;

import com.epam.esm.specification.predicate.impl.user.UserSpecificationById;
import com.epam.esm.specification.predicate.impl.user.UserSpecificationByLogin;
import com.epam.esm.specification.predicate.impl.user.UserSpecificationGetAll;
import org.springframework.stereotype.Component;

@Component
public class UserSpecificationFactory {

    public UserSpecificationGetAll getUserSpecificationGetAll() {
        return new UserSpecificationGetAll();
    }

    public UserSpecificationById getUserSpecificationById(Long id) {
        return new UserSpecificationById(id);
    }

    public UserSpecificationByLogin getUserSpecificationByLogin(String login) {
        return new UserSpecificationByLogin(login);
    }
}
