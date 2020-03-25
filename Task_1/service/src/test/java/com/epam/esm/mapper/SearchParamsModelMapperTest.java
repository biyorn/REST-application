package com.epam.esm.mapper;

import com.epam.esm.dto.SearchParamsDTO;
import com.epam.esm.entity.impl.SearchParams;
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
public class SearchParamsModelMapperTest {

    private static final String PART_NAME = "test";

    @InjectMocks
    private SearchParamsModelMapper searchParamsModelMapper;
    @Mock
    private ModelMapper modelMapper;
    private SearchParams searchParams;
    private SearchParamsDTO searchParamsDTO;
    private Class<SearchParams> entity = SearchParams.class;
    private Class<SearchParamsDTO> entityDTO = SearchParamsDTO.class;


    @Before
    public void init() {
        searchParams = getSearchParams(PART_NAME);
        searchParamsDTO = getSearchParamsDTO(PART_NAME);
    }

    @Test
    public void toEntityFromDto_DtoObjectSupplied_Entity() {
        when(modelMapper.map(searchParamsDTO, (Type) entity)).thenReturn(searchParams);

        SearchParams actual = searchParamsModelMapper.toEntity(searchParamsDTO);

        assertEquals(searchParams, actual);
    }

    @Test
    public void toDtoFromEntity_EntityObjectSupplied_Dto() {
        when(modelMapper.map(searchParams, (Type) entityDTO)).thenReturn(searchParamsDTO);

        SearchParamsDTO actual = searchParamsModelMapper.toDto(searchParams);

        assertEquals(searchParamsDTO, actual);
    }

    private SearchParams getSearchParams(String partName) {
        SearchParams local = new SearchParams();
        local.setPartName(partName);
        return local;
    }

    private SearchParamsDTO getSearchParamsDTO(String partName) {
        SearchParamsDTO local = new SearchParamsDTO();
        local.setPartName(partName);
        return local;
    }
}
