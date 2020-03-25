package com.epam.esm.service;

import com.epam.esm.dto.RoleDTO;
import com.epam.esm.dto.UserEntityDTO;
import com.epam.esm.entity.impl.Role;
import com.epam.esm.entity.impl.UserEntity;
import com.epam.esm.mapper.UserEntityModelMapper;
import com.epam.esm.repository.impl.UserRepositoryImpl;
import com.epam.esm.service.impl.UserDetailsServiceImpl;
import com.epam.esm.specification.predicate.impl.user.factory.UserSpecificationFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    private static final int PAGE = 1;
    private static final int SIZE = 1;

    private static final String LOGIN = "Test";
    private static final String PASSWORD = "Test";
    private static final String USER = "user";

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Mock
    private UserRepositoryImpl userRepository;
    @Mock
    private UserSpecificationFactory userSpecificationFactory;
    @Mock
    private UserEntityModelMapper userModelMapper;
    private UserEntityDTO userEntityDTO;
    private UserEntity userEntity;
    private Role role;
    private RoleDTO roleDTO;
    private User user;

    @Before
    public void init() {
        userEntityDTO = new UserEntityDTO();
        userEntityDTO.setLogin(LOGIN);
        userEntityDTO.setPassword(PASSWORD);
        roleDTO = new RoleDTO();
        roleDTO.setName(USER);
        userEntityDTO.setRoles(Set.of(roleDTO));

        userEntity = new UserEntity();
        userEntity.setLogin(LOGIN);
        userEntity.setPassword(PASSWORD);
        role = new Role();
        role.setName(USER);
        userEntity.setRoles(Set.of(role));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + LOGIN.toUpperCase());
        user = new User(LOGIN, PASSWORD, Collections.singletonList(authority));
    }

    @Test
    public void findOrRegister_ValidUserEntityDtoSupplied_UserDetails() {
        when(userRepository
                .query(userSpecificationFactory.getUserSpecificationByLogin(userEntityDTO.getLogin()), PAGE, SIZE))
                .thenReturn(Collections.emptyList());
        when(userModelMapper.toEntity(userEntityDTO)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserDetails actual = userDetailsServiceImpl.findOrRegister(userEntityDTO);

        assertEquals(user, actual);
    }
}
