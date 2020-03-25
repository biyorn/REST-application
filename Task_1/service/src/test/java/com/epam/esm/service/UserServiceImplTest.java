package com.epam.esm.service;

import com.epam.esm.dto.UserEntityDTO;
import com.epam.esm.entity.impl.UserEntity;
import com.epam.esm.exception.FailedAddObjectException;
import com.epam.esm.exception.NotFoundObjectException;
import com.epam.esm.mapper.UserEntityModelMapper;
import com.epam.esm.repository.impl.UserRepositoryImpl;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.specification.predicate.impl.user.factory.UserSpecificationFactory;
import com.epam.esm.validation.PageValidator;
import com.epam.esm.validation.UserValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 1;

    private static final Long FIRST_ID = 1L;
    private static final String FIRST_LOGIN = "first";
    private static final Long SECOND_ID = 2L;
    private static final String SECOND_LOGIN = "second";
    private static final Long THIRD_ID = 3L;
    private static final String THIRD_LOGIN = "third";

    @InjectMocks
    private UserServiceImpl userServiceImpl;
    @Mock
    private UserSpecificationFactory userSpecificationFactory;
    @Mock
    private UserRepositoryImpl userRepository;
    @Mock
    private UserEntityModelMapper modelMapper;
    @Mock
    private PageValidator pageValidator;
    @Mock
    private UserValidator userValidator;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserEntity firstUser;
    private UserEntityDTO firstUserDto;
    private UserEntity secondUser;
    private UserEntityDTO secondUserDto;
    private UserEntity thirdUser;
    private UserEntityDTO thirdUserDto;

    @Before
    public void init() {
        firstUser = getUserEntity(FIRST_ID, FIRST_LOGIN);
        firstUserDto = getUserEntityDTO(FIRST_ID, FIRST_LOGIN);
        secondUser = getUserEntity(SECOND_ID, SECOND_LOGIN);
        secondUserDto = getUserEntityDTO(SECOND_ID, SECOND_LOGIN);
        thirdUser = getUserEntity(THIRD_ID, THIRD_LOGIN);
        thirdUserDto = getUserEntityDTO(THIRD_ID, THIRD_LOGIN);
    }

    @Test
    public void getUserById_FirstIdSupplied_FirstUser() {
        when(userRepository.query(userSpecificationFactory
                .getUserSpecificationById(FIRST_ID), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.singletonList(firstUser));
        when(modelMapper.toDto(firstUser)).thenReturn(firstUserDto);

        UserEntityDTO actual = userServiceImpl.getUserById(FIRST_ID);

        assertEquals(firstUserDto, actual);
    }

    @Test(expected = NotFoundObjectException.class)
    public void getUserById_ThirdIdSupplied_Exception() {
        when(userRepository.query(userSpecificationFactory
                .getUserSpecificationById(THIRD_ID), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.emptyList());

        userServiceImpl.getUserById(THIRD_ID);
    }

    @Test
    public void getUsers_Nothing_FirstAndSecondUsers() {
        when(userRepository.query(userSpecificationFactory
                .getUserSpecificationGetAll(), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Arrays.asList(firstUser, secondUser));
        when(modelMapper.toDto(firstUser)).thenReturn(firstUserDto);
        when(modelMapper.toDto(secondUser)).thenReturn(secondUserDto);

        List<UserEntityDTO> actual = userServiceImpl.getUsers(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);

        assertThat(actual, hasSize(2));
        assertEquals(Arrays.asList(firstUserDto, secondUserDto), actual);
    }

    @Test
    public void save_ValidUserSupplied_NewUser() {
        doNothing().when(userValidator).isValid(thirdUserDto);
        when(modelMapper.toEntity(thirdUserDto)).thenReturn(thirdUser);
        when(bCryptPasswordEncoder.encode(thirdUser.getPassword())).thenReturn("somePass");
        when(userRepository.query(userSpecificationFactory
                .getUserSpecificationByLogin(thirdUser.getLogin()), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.emptyList());
        when(userRepository.save(thirdUser)).thenReturn(thirdUser);
        when(modelMapper.toDto(thirdUser)).thenReturn(thirdUserDto);

        UserEntityDTO actual = userServiceImpl.save(thirdUserDto);

        assertEquals(thirdUserDto, actual);
    }

    @Test(expected = FailedAddObjectException.class)
    public void save_ExistUserSupplied_Exception() {
        doNothing().when(userValidator).isValid(firstUserDto);
        when(modelMapper.toEntity(firstUserDto)).thenReturn(firstUser);
        when(bCryptPasswordEncoder.encode(thirdUser.getPassword())).thenReturn("somePass");
        when(userRepository.query(userSpecificationFactory
                .getUserSpecificationByLogin(thirdUser.getLogin()), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.singletonList(firstUser));

        userServiceImpl.save(firstUserDto);
    }

    private UserEntity getUserEntity(Long id, String login) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setLogin(login);
        return userEntity;
    }

    private UserEntityDTO getUserEntityDTO(Long id, String login) {
        UserEntityDTO userEntityDTO = new UserEntityDTO();
        userEntityDTO.setId(id);
        userEntityDTO.setLogin(login);
        return userEntityDTO;
    }
}
