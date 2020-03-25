package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.impl.Tag;
import com.epam.esm.exception.FailedAddObjectException;
import com.epam.esm.exception.IncorrectParameterException;
import com.epam.esm.exception.NotFoundObjectException;
import com.epam.esm.mapper.CommonModelMapper;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import com.epam.esm.service.TagService;
import com.epam.esm.specification.predicate.impl.tag.factory.TagSpecificationFactory;
import com.epam.esm.validation.PageValidator;
import com.epam.esm.validation.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 1;

    private TagRepositoryImpl repository;
    private TagValidator tagValidator;
    private PageValidator pageValidator;
    private CommonModelMapper<Tag, TagDTO> tagModelMapper;
    private TagSpecificationFactory specificationFactory;

    @Autowired
    public TagServiceImpl(TagRepositoryImpl repository, TagValidator tagValidator,
                          PageValidator pageValidator, CommonModelMapper<Tag, TagDTO> tagModelMapper,
                          TagSpecificationFactory specificationFactory) {
        this.repository = repository;
        this.tagValidator = tagValidator;
        this.pageValidator = pageValidator;
        this.tagModelMapper = tagModelMapper;
        this.specificationFactory = specificationFactory;
    }

    @Override
    @Transactional
    public TagDTO getById(Long id) {
        return repository.query(specificationFactory.getTagSpecificationById(id), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE)
                .stream()
                .findFirst()
                .map(tagModelMapper::toDto)
                .orElseThrow(() -> new NotFoundObjectException("Not found a tag by id - [" + id + "]"));
    }

    @Override
    @Transactional
    public List<TagDTO> getAll(int pageNum, int pageSize) {
        pageValidator.verifyPageNumbers(pageNum, pageSize);
        return repository.query(specificationFactory.getTagSpecificationGetAll(), pageNum, pageSize)
                .stream()
                .map(tagModelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TagDTO getFrequentlyUsedOrders() {
        return tagModelMapper.toDto(repository.frequentlyUsedOrders());
    }

    @Override
    @Transactional
    public TagDTO addTag(TagDTO tagDTO) {
        String title = tagDTO.getTitle();
        if (Objects.isNull(title) || !tagValidator.checkTitle(title)) {
            throw new IncorrectParameterException("Wrong title - [" + title + "]");
        }
        tagDTO.setTitle(title.trim());
        Tag tag = tagModelMapper.toEntity(tagDTO);
        repository.query(specificationFactory.getTagSpecificationByTitle(title), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE)
                .stream()
                .findFirst()
                .ifPresentOrElse(value -> {
                    throw new FailedAddObjectException("Failed to add tag with title - [" + title + "] already exists");
                }, () -> repository.add(tag));
        return tagModelMapper.toDto(tag);
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        repository.query(specificationFactory.getTagSpecificationById(id), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE)
                .stream()
                .findFirst()
                .ifPresentOrElse(value -> repository.delete(id), () -> {
                    throw new NotFoundObjectException("Not found a tag by id - [" + id + "]");
                });
    }
}
