package com.epam.esm.specification.predicate.impl.bought.factory;

import com.epam.esm.specification.predicate.impl.bought.BoughtCertificatePredicateSpecificationByUserLogin;
import org.springframework.stereotype.Component;

@Component
public class BoughtCertificatePredicateSpecificationFactory {

    public BoughtCertificatePredicateSpecificationByUserLogin getBoughtCertificatePredicateSpecificationByUser(String login) {
        return new BoughtCertificatePredicateSpecificationByUserLogin(login);
    }
}
