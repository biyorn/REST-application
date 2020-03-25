package com.epam.esm.service.impl;

import com.epam.esm.dto.UserEntityDTO;
import com.epam.esm.entity.impl.Role;
import com.epam.esm.entity.impl.UserEntity;
import com.epam.esm.exception.FailedAddObjectException;
import com.epam.esm.exception.NotFoundObjectException;
import com.epam.esm.mapper.CommonModelMapper;
import com.epam.esm.repository.impl.UserRepositoryImpl;
import com.epam.esm.service.UserService;
import com.epam.esm.specification.predicate.impl.user.factory.UserSpecificationFactory;
import com.epam.esm.validation.PageValidator;
import com.epam.esm.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 1;
    private static final long USER_ROLE_ID = 2L;
    private static final String USER_ROLE_NAME = "user";

    private UserSpecificationFactory userSpecificationFactory;
    private UserRepositoryImpl userRepository;
    private CommonModelMapper<UserEntity, UserEntityDTO> userModelMapper;
    private PageValidator pageValidator;
    private UserValidator userValidator;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserSpecificationFactory userSpecificationFactory,
                           UserRepositoryImpl userRepository,
                           CommonModelMapper<UserEntity, UserEntityDTO> userModelMapper,
                           PageValidator pageValidator,
                           UserValidator userValidator,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userSpecificationFactory = userSpecificationFactory;
        this.userRepository = userRepository;
        this.userModelMapper = userModelMapper;
        this.pageValidator = pageValidator;
        this.userValidator = userValidator;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public UserEntityDTO getUserById(Long id) {
        return userRepository
                .query(userSpecificationFactory.getUserSpecificationById(id), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE)
                .stream()
                .findFirst()
                .map(userModelMapper::toDto)
                .orElseThrow(() -> new NotFoundObjectException("Not found a user by id - [" + id + "]"));
    }

    @Override
    @Transactional
    public List<UserEntityDTO> getUsers(int pageNum, int pageSize) {
        pageValidator.verifyPageNumbers(pageNum, pageSize);
        return userRepository
                .query(userSpecificationFactory.getUserSpecificationGetAll(), pageNum, pageSize)
                .stream()
                .map(userModelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserEntityDTO save(UserEntityDTO userEntityDTO) {
        userValidator.isValid(userEntityDTO);
        UserEntity userEntity = userModelMapper.toEntity(userEntityDTO);
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        setUserRole(userEntity);
        userRepository
                .query(userSpecificationFactory.getUserSpecificationByLogin(userEntity.getLogin()), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE)
                .stream()
                .findFirst()
                .ifPresent(value -> {
                    throw new FailedAddObjectException("Login - [" + value.getLogin() + "] already exists");
                });
        return userModelMapper.toDto(userRepository.save(userEntity));
    }

    private void setUserRole(UserEntity userEntity) {
        Role role = new Role();
        role.setId(USER_ROLE_ID);
        role.setName(USER_ROLE_NAME);
        Set<Role> roles = Set.of(role);
        userEntity.setRoles(roles);
    }
}
