package com.epam.esm.dto;

import java.util.List;

public class CertificatePaginationDTO {

    private List<CertificateDTO> list;
    private int numberElements;

    public CertificatePaginationDTO() {
    }

    public CertificatePaginationDTO(List<CertificateDTO> list, int numberElements) {
        this.list = list;
        this.numberElements = numberElements;
    }

    public List<CertificateDTO> getList() {
        return list;
    }

    public void setList(List<CertificateDTO> list) {
        this.list = list;
    }

    public int getNumberElements() {
        return numberElements;
    }

    public void setNumberElements(int numberElements) {
        this.numberElements = numberElements;
    }
}
