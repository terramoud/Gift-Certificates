package com.epam.esm.repository.impl;

import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.mapper.CertificateMapper;
import com.epam.esm.domain.payload.CertificateRequest;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {

    private static final String SQL_GET_ALL_CERTIFICATES = "SELECT * FROM certificate";
    private static final String SQL_DELETE_CERTIFICATE = "DELETE FROM certificate WHERE id = ?";
    private static final String SQL_UPDATE_CERTIFICATE_ASSOCIATION_TO_TAG = "UPDATE certificates_tags SET tag_id = ?";
    private static final String SQL_UPDATE_CERTIFICATE = "UPDATE certificate " +
            "SET name = ?, description = ?, price = ?, duration = ?, last_update_date = ? " +
            "WHERE id = ?";
    private static final String SQL_CREATE_CERTIFICATE = "INSERT INTO certificate VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_JOIN_CERTIFICATE_AND_TAG = "INSERT INTO certificates_tags VALUES (?, ?)";
    private static final String SQL_FIND_CERTIFICATE_BY_ID = "SELECT * FROM certificate WHERE id = ?";
    private static final String SQL_FIND_ALL_CERTIFICATES_BY_TAG_ID =
            "SELECT * FROM certificate " +
                    "INNER JOIN certificates_tags ct on certificate.id = ct.certificate_id  " +
                    "WHERE tag_id = ?";
    private static final String SQL_FIND_ALL_CERTIFICATES_BY_TAG_NAME =
            "SELECT * FROM certificate" +
                    " INNER JOIN certificates_tags ct on certificate.id = ct.certificate_id  " +
                    "INNER JOIN tag on ct.tag_id = tag.id " +
                    "WHERE tag.name = ?";
    private static final String SQL_FIND_ALL_SEARCHED_CERTIFICATES =
            "SELECT * FROM certificate WHERE name or description LIKE ?";

    private CertificateMapper certificateRequestMapper;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CertificateRepositoryImpl(CertificateMapper certificateRequestMapper, DataSource dataSource) {
        this.certificateRequestMapper = certificateRequestMapper;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Certificate> getAllCertificates(String sortParams, int limit, int offset) {
        String sqlQuery = SQL_GET_ALL_CERTIFICATES
                .concat(" ORDER BY " + sortParams)
                .concat(" LIMIT " + limit)
                .concat(" OFFSET " + offset);
        return jdbcTemplate.query(sqlQuery, new CertificateMapper());
    }

    @Override
    public List<Certificate> searchAllCertificates(String sortParams, int limit, int offset, String searchQuery) {
        String sqlQuery = SQL_FIND_ALL_SEARCHED_CERTIFICATES
                .concat(" ORDER BY " + sortParams)
                .concat(" LIMIT " + limit)
                .concat(" OFFSET " + offset);
        return jdbcTemplate.query(sqlQuery, new Object[]{"%" + searchQuery + "%"}, new CertificateMapper());
    }

    @Override
    public List<Certificate> getAllCertificatesByTagName(String sortParams, int limit, int offset, String tagName) {
        String sqlQuery = SQL_FIND_ALL_CERTIFICATES_BY_TAG_NAME
                .concat(" ORDER BY " + sortParams)
                .concat(" LIMIT " + limit)
                .concat(" OFFSET " + offset);
        return jdbcTemplate.query(sqlQuery, new String[]{tagName}, new CertificateMapper());
    }

    @Override
    public List<Certificate> getAllCertificatesByTagId(String sortParams, int limit, int offset, Long tagId) {
        String sqlQuery = SQL_FIND_ALL_CERTIFICATES_BY_TAG_ID
                .concat(" ORDER BY " + sortParams)
                .concat(" LIMIT " + limit)
                .concat(" OFFSET " + offset);
        return jdbcTemplate.query(
                sqlQuery, new Object[]{tagId}, new CertificateMapper());
    }

    @Override
    public Optional<Certificate> getCertificateById(Long certificateId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(SQL_FIND_CERTIFICATE_BY_ID,
                            new Object[]{certificateId},
                            new CertificateMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Certificate addCertificate(Certificate certificate, Long tagId) {
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
        rowCount = jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            SQL_JOIN_CERTIFICATE_AND_TAG,
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setLong(1, certificate.getId());
                    ps.setLong(2, tagId);
                    return ps;
                },
                keyHolder);
        if (rowCount == 0) throw new ResourceNotSavedException(
                "certificate.no.join.to.tag", certificate.getId(), ErrorCodes.NOT_CREATE_ROW);
        return certificate;
    }

    @Override
    public Certificate updateCertificateById(Long certificateId, CertificateRequest certificateRequest) {
        int rowCount = jdbcTemplate.update(SQL_UPDATE_CERTIFICATE,
                certificateRequest.getName(),
                certificateRequest.getDescription(),
                certificateRequest.getPrice(),
                certificateRequest.getDuration(),
                certificateRequest.getLastUpdateDate(),
                certificateId);
        if (rowCount == 0) throw new ResourceNotUpdatedException(
                "certificate.no.rows.changed", certificateId, ErrorCodes.NOT_CHANGE_ROW);
        rowCount = jdbcTemplate.update(SQL_UPDATE_CERTIFICATE_ASSOCIATION_TO_TAG,
                certificateRequest.getTagId());
        if (rowCount == 0) throw new ResourceNotUpdatedException(
                "certificate.cannot.update.tag.id", certificateRequest.getTagId(), ErrorCodes.NOT_CHANGE_ROW);
        return certificateRequestMapper.mapToCertificate(certificateRequest);
    }

    @Override
    public boolean deleteCertificateById(Long certificateId) {
        return jdbcTemplate.update(SQL_DELETE_CERTIFICATE, certificateId) > 0;
    }
}
