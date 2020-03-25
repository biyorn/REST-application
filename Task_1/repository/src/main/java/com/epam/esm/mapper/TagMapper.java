package com.epam.esm.mapper;

import com.epam.esm.entity.impl.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagMapper implements RowMapper<Tag> {

    @Override
    public Tag mapRow(ResultSet resultSet, int i) throws SQLException {
        Tag tag = new Tag();
        Long id = resultSet.getLong("id");
        tag.setId(id);
        String title = resultSet.getString("title");
        tag.setTitle(title);
        return tag;
    }
}
