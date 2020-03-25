package com.epam.esm.mapper;

import com.epam.esm.dto.RoleDTO;
import com.epam.esm.entity.impl.Role;
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
public class RoleModelMapperTest {

    private static final Long ID = 1L;
    private static final String NAME = "Test";

    @InjectMocks
    private RoleModelMapper roleModelMapper;
    @Mock
    private ModelMapper modelMapper;
    private Role role;
    private RoleDTO roleDTO;
    private Class<Role> entity = Role.class;
    private Class<RoleDTO> entityDTO = RoleDTO.class;

    @Before
    public void init() {
        role = getRole(ID, NAME);
        roleDTO = getRoleDTO(ID, NAME);
    }

    @Test
    public void toEntityFromDto_DtoObjectSupplied_Entity() {
        when(modelMapper.map(roleDTO, (Type) entity)).thenReturn(role);

        Role actual = roleModelMapper.toEntity(roleDTO);

        assertEquals(role, actual);
    }

    @Test
    public void toDtoFromEntity_EntityObjectSupplied_Dto() {
        when(modelMapper.map(role, (Type) entityDTO)).thenReturn(roleDTO);

        RoleDTO actual = roleModelMapper.toDto(role);

        assertEquals(roleDTO, actual);
    }

    private Role getRole(Long id, String name) {
        Role local = new Role();
        local.setId(id);
        local.setName(name);
        return local;
    }

    private RoleDTO getRoleDTO(Long id, String name) {
        RoleDTO local = new RoleDTO();
        local.setId(id);
        local.setName(name);
        return local;
    }
}
