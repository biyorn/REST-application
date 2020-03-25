package com.epam.esm.mapper;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.impl.Tag;
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
public class TagModelMapperTest {

    private static final Long ID = 1L;
    private static final String TITLE = "test";

    @InjectMocks
    private TagModelMapper tagModelMapper;
    @Mock
    private ModelMapper modelMapper;
    private Tag tag;
    private TagDTO tagDTO;
    private Class<Tag> entity = Tag.class;
    private Class<TagDTO> entityDTO = TagDTO.class;

    @Before
    public void init() {
        tag = getTag(ID, TITLE);
        tagDTO = getTagDTO(ID, TITLE);
    }

    @Test
    public void toEntityFromDto_DtoObjectSupplied_Entity() {
        when(modelMapper.map(tagDTO, (Type) entity)).thenReturn(tag);

        Tag actual = tagModelMapper.toEntity(tagDTO);

        assertEquals(tag, actual);
    }

    @Test
    public void toDtoFromEntity_EntityObjectSupplied_Dto() {
        when(modelMapper.map(tag, (Type) entityDTO)).thenReturn(tagDTO);

        TagDTO actual = tagModelMapper.toDto(tag);

        assertEquals(tagDTO, actual);
    }

    private Tag getTag(Long id, String title) {
        Tag local = new Tag();
        local.setId(id);
        local.setTitle(title);
        return local;
    }

    private TagDTO getTagDTO(Long id, String title) {
        TagDTO local = new TagDTO();
        local.setId(id);
        local.setTitle(title);
        return local;
    }
}
