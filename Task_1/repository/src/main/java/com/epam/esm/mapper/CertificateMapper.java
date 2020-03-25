package com.epam.esm.mapper;

import com.epam.esm.entity.impl.Certificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class CertificateMapper implements RowMapper<Certificate> {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String CREATION_DATE = "creation_date";
    private static final String MODIFICATION_DATE = "modification_date";
    private static final String DURATION_DAYS = "duration_days";

    @Override
    public Certificate mapRow(ResultSet resultSet, int i) throws SQLException {
        Certificate certificate = new Certificate();

        Long id = resultSet.getLong(ID);
        certificate.setId(id);

        String name = resultSet.getString(NAME);
        certificate.setName(name);

        String description = resultSet.getString(DESCRIPTION);
        certificate.setDescription(description);

        BigDecimal price = resultSet.getBigDecimal(PRICE);
        certificate.setPrice(price);

        Timestamp timestamp = resultSet.getTimestamp(CREATION_DATE);
        LocalDateTime creationDate = timestamp.toLocalDateTime();
        certificate.setCreationDate(creationDate);

        timestamp = resultSet.getTimestamp(MODIFICATION_DATE);
        LocalDateTime modificationDate = timestamp.toLocalDateTime();
        certificate.setModificationDate(modificationDate);

        int durationDays = resultSet.getInt(DURATION_DAYS);
        certificate.setDurationDays(durationDays);

        return certificate;
    }
}
