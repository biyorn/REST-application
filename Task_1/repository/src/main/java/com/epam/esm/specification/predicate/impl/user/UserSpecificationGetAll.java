package com.epam.esm.specification.predicate.impl.user;

import com.epam.esm.entity.impl.UserEntity;
import com.epam.esm.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class UserSpecificationGetAll implements Specification<UserEntity> {

    @Override
    public CriteriaQuery<UserEntity> getQuery(CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<UserEntity> criteriaQuery = criteriaBuilder.createQuery(UserEntity.class);
        Root<UserEntity> root = criteriaQuery.from(UserEntity.class);
        return criteriaQuery
                .select(root)
                .orderBy(criteriaBuilder.asc(root.get("id")));
    }
}
