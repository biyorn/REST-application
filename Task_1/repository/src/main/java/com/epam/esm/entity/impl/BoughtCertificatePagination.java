package com.epam.esm.entity.impl;

import java.util.List;

public class BoughtCertificatePagination {

    private List<BoughtCertificate> boughtCertificates;
    private int numberElements;

    public BoughtCertificatePagination() {
    }

    public BoughtCertificatePagination(List<BoughtCertificate> boughtCertificates, int numberElements) {
        this.boughtCertificates = boughtCertificates;
        this.numberElements = numberElements;
    }

    public List<BoughtCertificate> getBoughtCertificates() {
        return boughtCertificates;
    }

    public void setBoughtCertificates(List<BoughtCertificate> boughtCertificates) {
        this.boughtCertificates = boughtCertificates;
    }

    public int getNumberElements() {
        return numberElements;
    }

    public void setNumberElements(int numberElements) {
        this.numberElements = numberElements;
    }
}
