package com.epam.esm.specification.orderby.impl.boughtCertificate.factory;

import com.epam.esm.specification.orderby.impl.boughtCertificate.BoughtCertificateOrderBySpecificationDefault;
import org.springframework.stereotype.Component;

@Component
public class BoughtCertificateOrderBySpecificationFactory {

    public BoughtCertificateOrderBySpecificationDefault getBoughtCertificateOrderBySpecificationDefault() {
        return new BoughtCertificateOrderBySpecificationDefault();
    }
}
