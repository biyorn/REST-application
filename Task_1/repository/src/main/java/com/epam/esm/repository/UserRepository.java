package com.epam.esm.repository;

import com.epam.esm.entity.impl.UserEntity;
import com.epam.esm.specification.Specification;

import java.util.List;

public interface UserRepository {

    UserEntity save(UserEntity userEntity);

    List<UserEntity> query(Specification<UserEntity> specification, int pageNum, int pageSize);
}
