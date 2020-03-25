package com.epam.esm.entity;

public enum CertificateSortEnum {
    CREATION_DATE("creationDate", true),
    CREATION_DATE_DESC("creationDate", false),
    NAME("name", true),
    NAME_DESC("name", false),
    PRICE("price", true),
    PRICE_DESC("price", false),
    DESCRIPTION("description", true),
    DESCRIPTION_DESC("description", false);

    private String title;
    private boolean isAsc;

    CertificateSortEnum(String title, boolean isAsc) {
        this.title = title;
        this.isAsc = isAsc;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAsc() {
        return isAsc;
    }
}
