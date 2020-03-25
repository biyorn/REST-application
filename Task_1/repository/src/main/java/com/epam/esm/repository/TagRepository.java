package com.epam.esm.repository;

import com.epam.esm.entity.impl.Tag;
import com.epam.esm.specification.Specification;

import java.util.List;

public interface TagRepository {

    Tag add(Tag tag);

    void delete(Long id);

    Tag frequentlyUsedOrders();

    List<Tag> query(Specification<Tag> specification, int pageNum, int pageSize);
}
