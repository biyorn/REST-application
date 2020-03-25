package com.epam.esm.repository.impl;

import com.epam.esm.entity.impl.UserEntity;
import com.epam.esm.repository.Pagination;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.specification.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository, Pagination<UserEntity> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserEntity save(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    @Override
    public List<UserEntity> query(Specification<UserEntity> specification, int pageNum, int pageSize) {
        TypedQuery<UserEntity> query = entityManager
                .createQuery(specification.getQuery(entityManager.getCriteriaBuilder()));
        setPagination(query, pageNum, pageSize);
        return query.getResultList();
    }
}
