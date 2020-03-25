package com.epam.esm.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class BoughtCertificateDTO {

    private Long id;
    private CertificateDTO certificate;
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CertificateDTO getCertificate() {
        return certificate;
    }

    public void setCertificate(CertificateDTO certificate) {
        this.certificate = certificate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoughtCertificateDTO that = (BoughtCertificateDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(certificate, that.certificate) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, certificate, price);
    }
}
