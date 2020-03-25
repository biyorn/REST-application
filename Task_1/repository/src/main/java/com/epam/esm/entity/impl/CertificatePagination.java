package com.epam.esm.entity.impl;

import java.util.List;

public class CertificatePagination {

    private List<Certificate> list;
    private int numberElements;

    public CertificatePagination() {
    }

    public CertificatePagination(List<Certificate> list, int numberElements) {
        this.list = list;
        this.numberElements = numberElements;
    }

    public List<Certificate> getList() {
        return list;
    }

    public void setList(List<Certificate> list) {
        this.list = list;
    }

    public int getNumberElements() {
        return numberElements;
    }

    public void setNumberElements(int numberElements) {
        this.numberElements = numberElements;
    }
}
