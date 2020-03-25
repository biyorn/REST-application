package com.epam.esm.repository;

import javax.persistence.TypedQuery;

public interface Pagination<T> {

    default void setPagination(TypedQuery<T> query, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        query.setFirstResult(offset);
        query.setMaxResults(pageSize);
    }
}
