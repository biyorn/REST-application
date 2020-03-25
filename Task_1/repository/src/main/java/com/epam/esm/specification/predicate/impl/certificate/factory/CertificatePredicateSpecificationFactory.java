package com.epam.esm.specification.predicate.impl.certificate.factory;

import com.epam.esm.entity.impl.SearchParams;
import com.epam.esm.specification.predicate.impl.certificate.CertificatePredicateSpecificationById;
import com.epam.esm.specification.predicate.impl.certificate.CertificatePredicateSpecificationGetAll;
import com.epam.esm.specification.predicate.impl.certificate.CertificatePredicateSpecificationSearchByParams;
import org.springframework.stereotype.Component;

@Component
public class CertificatePredicateSpecificationFactory {

    public CertificatePredicateSpecificationById getCertificatePredicateSpecificationById(Long id) {
        return new CertificatePredicateSpecificationById(id);
    }

    public CertificatePredicateSpecificationGetAll getCertificatePredicateSpecificationGetAll() {
        return new CertificatePredicateSpecificationGetAll();
    }

    public CertificatePredicateSpecificationSearchByParams getCertificatePredicateSpecificationSearchByParams(SearchParams searchParams) {
        return new CertificatePredicateSpecificationSearchByParams(searchParams);
    }
}
