package com.epam.esm.repository.impl;

import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.mapper.CertificateRowMapper;
import com.epam.esm.domain.entity.mapper.RequestParametersMapper;
import com.epam.esm.exceptions.ErrorCodes;
import com.epam.esm.exceptions.ResourceNotSavedException;
import com.epam.esm.exceptions.ResourceNotUpdatedException;
import com.epam.esm.repository.api.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {

    private static final String SQL_GET_ALL_CERTIFICATES =
            "SELECT id, name, description, price, duration, " +
                    "DATE_FORMAT(create_date, '%Y-%m-%dT%H:%i:%s.%m3') as create_date, " +
                    "DATE_FORMAT(last_update_date, '%Y-%m-%dT%H:%i:%s.%m3') as last_update_date " +
                    "FROM certificate";
    private static final String SQL_DELETE_CERTIFICATE = "DELETE FROM certificate WHERE id = ?";
    private static final String SQL_UPDATE_CERTIFICATE = "UPDATE certificate " +
            "SET name = ?, description = ?, price = ?, duration = ?, last_update_date = ? " +
            "WHERE id = ?";
    private static final String SQL_CREATE_CERTIFICATE = "INSERT INTO certificate VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_FIND_CERTIFICATE_BY_ID =
            "SELECT id, name, description, price, duration, " +
                    "DATE_FORMAT(create_date, '%Y-%m-%dT%H:%i:%s.%m3') as create_date, " +
                    "DATE_FORMAT(last_update_date, '%Y-%m-%dT%H:%i:%s.%m3') as last_update_date " +
                    "FROM certificate WHERE id = ?";
    private static final String SQL_FIND_ALL_CERTIFICATES_BY_TAG_ID =
            "SELECT id, name, description, price, duration, " +
                    "DATE_FORMAT(create_date, '%Y-%m-%dT%H:%i:%s.%m3') as create_date, " +
                    "DATE_FORMAT(last_update_date, '%Y-%m-%dT%H:%i:%s.%m3') as last_update_date " +
                    "FROM certificate" +
            " INNER JOIN certificates_tags ct on certificate.id = ct.certificate_id  " +
            "WHERE tag_id = ?";
    private static final String SQL_FIND_ALL_CERTIFICATES_BY_TAG_NAME =
            "SELECT certificate.id, certificate.name, description, price, duration, " +
                    "DATE_FORMAT(create_date, '%Y-%m-%dT%H:%i:%s.%m3') as create_date, " +
                    "DATE_FORMAT(last_update_date, '%Y-%m-%dT%H:%i:%s.%m3') as last_update_date " +
                    "FROM certificate" +
            " INNER JOIN certificates_tags ct on certificate.id = ct.certificate_id  " +
            "INNER JOIN tag on ct.tag_id = tag.id " +
            "WHERE tag.name = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CertificateRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Certificate> getAllCertificates(List<String> sortParameters) {
        RequestParametersMapper parametersMapper = new RequestParametersMapper();
        String parameters = parametersMapper.mapSortParameters(sortParameters);
        String sqlQuery = SQL_GET_ALL_CERTIFICATES + " ORDER BY " + parameters;
        return jdbcTemplate.query(sqlQuery, new CertificateRowMapper());
    }

    @Override
    public List<Certificate> getAllCertificates(List<String> sortParameters, String query) {
        return Collections.emptyList();
    }

    @Override
    public List<Certificate> getAllCertificatesByTagId(Long tagId) {
        return jdbcTemplate.query(
                SQL_FIND_ALL_CERTIFICATES_BY_TAG_ID, new Object[]{tagId}, new CertificateRowMapper());
    }

    @Override
    public List<Certificate> getAllCertificatesByTagName(String tagName) {
        return jdbcTemplate.query(
                SQL_FIND_ALL_CERTIFICATES_BY_TAG_NAME, new String[]{tagName}, new CertificateRowMapper());
    }

    @Override
    public Optional<Certificate> getCertificateById(Long certificateId) {
        try {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(SQL_FIND_CERTIFICATE_BY_ID,
                new Object[]{certificateId},
                new CertificateRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Certificate addCertificate(Certificate certificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowCount = jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            SQL_CREATE_CERTIFICATE,
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, certificate.getName());
                    ps.setString(2, certificate.getDescription());
                    ps.setBigDecimal(3, certificate.getPrice());
                    ps.setInt(4, certificate.getDuration());
                    ps.setTimestamp(5, Timestamp.valueOf(certificate.getCreateDate()));
                    ps.setTimestamp(6, Timestamp.valueOf(certificate.getLastUpdateDate()));
                    return ps;
                },
                keyHolder);
        if (rowCount == 0) throw new ResourceNotSavedException(
                "certificate.no.rows.inserted", certificate.getId(), ErrorCodes.NOT_CREATE_ROW);
        certificate.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return certificate;
    }

    @Override
    public Certificate updateCertificateById(Long certificateId, Certificate certificate) {
        int rowCount = jdbcTemplate.update(SQL_UPDATE_CERTIFICATE,
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                certificate.getLastUpdateDate(),
                certificateId);
        if (rowCount == 0) throw new ResourceNotUpdatedException(
                "certificate.no.rows.changed", certificateId, ErrorCodes.NOT_CHANGE_ROW);
        return certificate;
    }

    @Override
    public boolean deleteCertificateById(Long certificateId) {
        return jdbcTemplate.update(SQL_DELETE_CERTIFICATE, certificateId) > 0;
    }
}
