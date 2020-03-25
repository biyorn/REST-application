package com.epam.esm.service.impl;

import com.epam.esm.dto.UserEntityDTO;
import com.epam.esm.entity.impl.Role;
import com.epam.esm.entity.impl.UserEntity;
import com.epam.esm.mapper.CommonModelMapper;
import com.epam.esm.repository.impl.UserRepositoryImpl;
import com.epam.esm.specification.predicate.impl.user.factory.UserSpecificationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 1;
    private static final String ROLE_PREFIX = "ROLE_";

    private UserRepositoryImpl userRepository;
    private UserSpecificationFactory userSpecificationFactory;
    private CommonModelMapper<UserEntity, UserEntityDTO> userModelMapper;

    @Autowired
    public UserDetailsServiceImpl(UserRepositoryImpl userRepository, UserSpecificationFactory userSpecificationFactory,
                                  CommonModelMapper<UserEntity, UserEntityDTO> userModelMapper) {
        this.userRepository = userRepository;
        this.userSpecificationFactory = userSpecificationFactory;
        this.userModelMapper = userModelMapper;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        UserEntity userEntity = userRepository
                .query(userSpecificationFactory.getUserSpecificationByLogin(username),
                        DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Not found user - [" + username + "]"));
        return new User(userEntity.getLogin(), userEntity.getPassword(), getRoles(userEntity.getRoles()));
    }

    @Transactional
    public UserDetails findOrRegister(UserEntityDTO userEntityDTO) {
        UserEntity userEntity = userRepository
                .query(userSpecificationFactory.getUserSpecificationByLogin(userEntityDTO.getLogin()),
                        DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE)
                .stream()
                .findFirst()
                .orElseGet(() -> userRepository.save(userModelMapper.toEntity(userEntityDTO)));
        return new User(userEntity.getLogin(), userEntity.getPassword(), getRoles(userEntity.getRoles()));
    }

    private List<GrantedAuthority> getRoles(Set<Role> roles) {
        return roles.stream()
                .map(value -> new SimpleGrantedAuthority(ROLE_PREFIX + value.getName().toUpperCase()))
                .collect(Collectors.toList());
    }
}
