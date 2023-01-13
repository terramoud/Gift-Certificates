package com.epam.esm.domain.entity.mapper;

import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.payload.CertificateRequest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class CertificateMapper implements RowMapper<Certificate> {

    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN)
            .withZone(ZoneId.of("UTC"));

    /**
     * Implementations must implement this method to map each row of data
     * in the ResultSet. This method should not call {@code next()} on
     * the ResultSet; it is only supposed to map values of the current row.
     *
     * @param rs     the ResultSet to map (pre-initialized for the current row)
     * @param rowNum the number of the current row
     * @return the result object for the current row (may be {@code null})
     * @throws SQLException if an SQLException is encountered getting
     *                      column values (that is, there's no need to catch SQLException)
     */
    @Override
    public Certificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        Certificate certificate = new Certificate();
        certificate.setId(rs.getLong("id"));
        certificate.setName(rs.getString("name"));
        certificate.setDescription(rs.getString("description"));
        certificate.setDuration(rs.getInt("duration"));
        certificate.setPrice(rs.getBigDecimal("price"));
        certificate.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime());
        certificate.setLastUpdateDate(rs.getTimestamp("last_update_date").toLocalDateTime());
        return certificate;
    }

    public Certificate mapToCertificate(CertificateRequest certificateRequest) {
        return new Certificate(
                certificateRequest.getId(),
                certificateRequest.getName(),
                certificateRequest.getDescription(),
                certificateRequest.getPrice(),
                certificateRequest.getDuration(),
                certificateRequest.getCreateDate(),
                certificateRequest.getLastUpdateDate()
        );
    }

    public CertificateRequest mapToRequest(Certificate certificate, Long tagId) {
        return new CertificateRequest(
                tagId,
                certificate.getId(),
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                certificate.getCreateDate(),
                certificate.getLastUpdateDate()
        );
    }
}
