package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;

import java.util.List;

public interface TagService {

    TagDTO getById(Long id);

    List<TagDTO> getAll(int pageNum, int pageSize);

    TagDTO getFrequentlyUsedOrders();

    TagDTO addTag(TagDTO tagDTO);

    void deleteTag(Long id);
}
