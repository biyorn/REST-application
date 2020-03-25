package com.epam.esm.mapper;

import com.epam.esm.dto.UserEntityDTO;
import com.epam.esm.entity.impl.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserEntityModelMapperTest {

    private static final Long ID = 1L;
    private static final String LOGIN = "test";

    @InjectMocks
    private UserEntityModelMapper userEntityModelMapper;
    @Mock
    private ModelMapper modelMapper;
    private UserEntity userEntity;
    private UserEntityDTO userEntityDTO;
    private Class<UserEntity> entity = UserEntity.class;
    private Class<UserEntityDTO> entityDTO = UserEntityDTO.class;

    @Before
    public void init() {
        userEntity = getUser(ID, LOGIN);
        userEntityDTO = getUserDTO(ID, LOGIN);
    }

    @Test
    public void toEntityFromDto_DtoObjectSupplied_Entity() {
        when(modelMapper.map(userEntityDTO, (Type) entity)).thenReturn(userEntity);

        UserEntity actual = userEntityModelMapper.toEntity(userEntityDTO);

        assertEquals(userEntity, actual);
    }

    @Test
    public void toDtoFromEntity_EntityObjectSupplied_Dto() {
        when(modelMapper.map(userEntity, (Type) entityDTO)).thenReturn(userEntityDTO);

        UserEntityDTO actual = userEntityModelMapper.toDto(userEntity);

        assertEquals(userEntityDTO, actual);
    }

    private UserEntityDTO getUserDTO(Long id, String login) {
        UserEntityDTO local = new UserEntityDTO();
        local.setId(id);
        local.setLogin(login);
        return local;
    }

    private UserEntity getUser(Long id, String login) {
        UserEntity local = new UserEntity();
        local.setId(id);
        local.setLogin(login);
        return local;
    }

}
