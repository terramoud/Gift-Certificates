package com.epam.esm.repository.impl;

import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.entity.mapper.RequestParametersMapper;
import com.epam.esm.domain.entity.mapper.TagRowMapper;
import com.epam.esm.exceptions.*;
import com.epam.esm.repository.api.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository {

    public static final String SQL_FIND_TAG_BY_ID = "SELECT * FROM tag WHERE id = ? LIMIT 1";
    public static final String SQL_FIND_ALL_TAGS = "SELECT * FROM tag";
    public static final String SQL_FIND_ALL_TAGS_BY_CERTIFICATE_ID =
            "SELECT * FROM tag INNER JOIN certificates_tags ct on tag.id = ct.tag_id WHERE certificate_id = ?";
    private static final String SQL_CREATE_TAG = "INSERT INTO tag VALUES (DEFAULT, ?)";
    private static final String SQL_UPDATE_TAG = "UPDATE tag SET name = ? WHERE id = ?";
    private static final String SQL_DELETE_TAG = "DELETE FROM tag WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Tag> getAllTags(List<String> sortParameters) {
        RequestParametersMapper parametersMapper = new RequestParametersMapper();
        String parameters = parametersMapper.mapSortParameters(sortParameters);
        String sqlQuery = SQL_FIND_ALL_TAGS + " ORDER BY " + parameters;
        return jdbcTemplate.query(sqlQuery, new TagRowMapper());
    }

    @Override
    public List<Tag> getAllTags(List<String> sortParameters, String query) {
        return Collections.emptyList();
    }

    @Override
    public Optional<Tag> getTagById(Long tagId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    SQL_FIND_TAG_BY_ID, new Object[]{tagId}, new TagRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> getTagsByCertificateId(Long certificateId) {
        return jdbcTemplate.query(
                SQL_FIND_ALL_TAGS_BY_CERTIFICATE_ID, new Object[] {certificateId}, new TagRowMapper());
    }

    @Override
    public Tag addTag(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowCount = jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            SQL_CREATE_TAG,
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, tag.getName());
                    return ps;
                },
                keyHolder);
        if (rowCount == 0) throw new ResourceNotSavedException(
                "tag.no.rows.inserted", tag.getId(), ErrorCodes.NOT_CREATE_ROW);
        tag.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return tag;
    }

    @Override
    public Tag updateTagById(Long tagId, Tag tag) {
        int rowCount = jdbcTemplate.update(SQL_UPDATE_TAG, tag.getName(), tagId);
        if (rowCount == 0) throw new ResourceNotUpdatedException(
                "tag.no.rows.changed", tag.getId(), ErrorCodes.NOT_CHANGE_ROW);
        return tag;
    }

    @Override
    public boolean deleteTagById(Long tagId) {
        return jdbcTemplate.update(SQL_DELETE_TAG, tagId) > 0;
    }
}
