package com.epam.esm.specification.orderby.impl.certificate.factory;

import com.epam.esm.entity.CertificateSortEnum;
import com.epam.esm.specification.orderby.impl.certificate.CertificateOrderBySpecificationDefault;
import com.epam.esm.specification.orderby.impl.certificate.CertificateOrderBySpecificationSort;
import org.springframework.stereotype.Component;

@Component
public class CertificateOrderByFactory {

    public CertificateOrderBySpecificationSort getCertificateOrderBySpecificationSort(CertificateSortEnum sort) {
        return new CertificateOrderBySpecificationSort(sort);
    }

    public CertificateOrderBySpecificationDefault getCertificateOrderBySpecificationDefault() {
        return new CertificateOrderBySpecificationDefault();
    }
}
