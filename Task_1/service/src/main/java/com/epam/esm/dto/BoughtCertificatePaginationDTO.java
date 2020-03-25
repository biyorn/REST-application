package com.epam.esm.dto;

import java.util.List;

public class BoughtCertificatePaginationDTO {

    private List<BoughtCertificateDTO> boughtCertificates;
    private int numberElements;

    public BoughtCertificatePaginationDTO() {
    }

    public BoughtCertificatePaginationDTO(List<BoughtCertificateDTO> boughtCertificates, int numberElements) {
        this.boughtCertificates = boughtCertificates;
        this.numberElements = numberElements;
    }

    public List<BoughtCertificateDTO> getBoughtCertificates() {
        return boughtCertificates;
    }

    public void setBoughtCertificates(List<BoughtCertificateDTO> boughtCertificates) {
        this.boughtCertificates = boughtCertificates;
    }

    public int getNumberElements() {
        return numberElements;
    }

    public void setNumberElements(int numberElements) {
        this.numberElements = numberElements;
    }
}
