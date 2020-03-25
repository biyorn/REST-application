package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.impl.Tag;
import com.epam.esm.exception.FailedAddObjectException;
import com.epam.esm.exception.IncorrectParameterException;
import com.epam.esm.exception.NotFoundObjectException;
import com.epam.esm.mapper.TagModelMapper;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.specification.predicate.impl.tag.factory.TagSpecificationFactory;
import com.epam.esm.validation.PageValidator;
import com.epam.esm.validation.TagValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceImplTest {

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 1;

    private static final Long FIRST_ID = 1L;
    private static final Long SECOND_ID = 2L;
    private static final Long THIRD_ID = 3L;
    private static final String FIRST_TITLE = "first";
    private static final String SECOND_TITLE = "second";
    private static final String THIRD_TITLE = "third";

    private Tag firstTag;
    private Tag secondTag;
    private Tag thirdTag;
    private List<Tag> tagList;

    private TagDTO firstTagDto;
    private TagDTO secondTagDto;
    private TagDTO thirdTagDto;
    private List<TagDTO> tagDTOList;

    @InjectMocks
    private TagServiceImpl service;
    @Mock
    private TagRepositoryImpl repository;
    @Mock
    private TagValidator validator;
    @Mock
    private PageValidator pageValidator;
    @Mock
    private TagModelMapper modelMapper;
    @Mock
    private TagSpecificationFactory specificationFactory;

    @Before
    public void init() {
        firstTag = getTag(FIRST_ID, FIRST_TITLE);
        secondTag = getTag(SECOND_ID, SECOND_TITLE);
        thirdTag = getTag(THIRD_ID, THIRD_TITLE);

        tagList = Arrays.asList(firstTag, secondTag, thirdTag);

        firstTagDto = getTagDto(FIRST_ID, FIRST_TITLE);
        secondTagDto = getTagDto(SECOND_ID, SECOND_TITLE);
        thirdTagDto = getTagDto(THIRD_ID, THIRD_TITLE);

        tagDTOList = Arrays.asList(firstTagDto, secondTagDto, thirdTagDto);
    }

    @Test
    public void getById_FirstIdSupplied_FirstTagDto() {
        Long id = 1L;

        when(repository.query(specificationFactory
                .getTagSpecificationById(firstTag.getId()), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.singletonList(firstTag));
        when(modelMapper.toDto(firstTag)).thenReturn(firstTagDto);

        TagDTO actual = service.getById(id);

        assertNotNull(actual);
        assertEquals(firstTagDto, actual);
    }

    @Test(expected = NotFoundObjectException.class)
    public void getById_TagNotFound_Exception() {
        when(repository.query(specificationFactory
                .getTagSpecificationById(10L), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.emptyList());

        service.getById(anyLong());
    }

    @Test
    public void getAll_ParametersSupplied_ListTagDto() {
        when(repository.query(specificationFactory
                .getTagSpecificationGetAll(), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(tagList);
        when(modelMapper.toDto(firstTag)).thenReturn(firstTagDto);
        when(modelMapper.toDto(secondTag)).thenReturn(secondTagDto);
        when(modelMapper.toDto(thirdTag)).thenReturn(thirdTagDto);

        List<TagDTO> actual = service.getAll(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);

        assertThat(actual, hasSize(tagList.size()));
        assertThat(actual, is(tagDTOList));
    }

    @Test
    public void getFrequentlyUsedOrders_None_FirstTagDto() {
        when(repository.frequentlyUsedOrders()).thenReturn(firstTag);
        when(modelMapper.toDto(firstTag)).thenReturn(firstTagDto);

        TagDTO actual = service.getFrequentlyUsedOrders();

        assertEquals(firstTagDto, actual);
    }

    @Test
    public void addTag_WellTagSupplied_TagDto() {
        when(validator.checkTitle(firstTagDto.getTitle())).thenReturn(true);
        when(modelMapper.toEntity(firstTagDto)).thenReturn(firstTag);
        when(repository.add(firstTag)).thenReturn(firstTag);
        when(modelMapper.toDto(firstTag)).thenReturn(firstTagDto);

        TagDTO actual = service.addTag(firstTagDto);

        assertEquals(firstTagDto, actual);
    }

    @Test(expected = IncorrectParameterException.class)
    public void addTag_TagValidatorReturnFalse_Exception() {
        when(validator.checkTitle(firstTagDto.getTitle())).thenReturn(false);

        service.addTag(firstTagDto);
    }

    @Test(expected = FailedAddObjectException.class)
    public void addTag_RepositoryCouldNotAddTag_Exception() {
        when(validator.checkTitle(firstTagDto.getTitle())).thenReturn(true);
        when(modelMapper.toEntity(firstTagDto)).thenReturn(firstTag);
        when(repository.query(specificationFactory
                .getTagSpecificationByTitle(FIRST_TITLE), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.singletonList(firstTag));
        service.addTag(firstTagDto);
    }

    @Test
    public void delete_WellTagDtoSupplied_CallTwoMethods() {
        Long id = firstTag.getId();

        when(repository.query(specificationFactory
                .getTagSpecificationById(id), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.singletonList(firstTag));
        doNothing().when(repository).delete(id);

        service.deleteTag(id);

        verify(repository, times(1))
                .query(specificationFactory.getTagSpecificationById(id), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
        verify(repository, times(1)).delete(id);
    }

    private Tag getTag(Long id, String title) {
        Tag tag = new Tag();
        tag.setId(id);
        tag.setTitle(title);
        return tag;
    }

    private TagDTO getTagDto(Long id, String title) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(id);
        tagDTO.setTitle(title);
        return tagDTO;
    }
}
