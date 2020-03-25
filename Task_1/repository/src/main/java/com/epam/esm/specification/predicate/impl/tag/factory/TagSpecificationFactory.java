package com.epam.esm.specification.predicate.impl.tag.factory;

import com.epam.esm.entity.impl.Tag;
import com.epam.esm.specification.predicate.impl.tag.TagSpecificationById;
import com.epam.esm.specification.predicate.impl.tag.TagSpecificationByTitle;
import com.epam.esm.specification.predicate.impl.tag.TagSpecificationGetAll;
import com.epam.esm.specification.predicate.impl.tag.TagSpecificationGetExistTags;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TagSpecificationFactory {

    public TagSpecificationById getTagSpecificationById(Long id) {
        return new TagSpecificationById(id);
    }

    public TagSpecificationByTitle getTagSpecificationByTitle(String title) {
        return new TagSpecificationByTitle(title);
    }

    public TagSpecificationGetAll getTagSpecificationGetAll() {
        return new TagSpecificationGetAll();
    }

    public TagSpecificationGetExistTags getTagSpecificationGetExistTags(Set<Tag> tags) {
        return new TagSpecificationGetExistTags(tags);
    }
}
