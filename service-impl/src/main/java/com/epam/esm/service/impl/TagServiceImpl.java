package com.epam.esm.service.impl;


import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.entity.mapper.RequestParametersMapper;
import com.epam.esm.exceptions.*;
import com.epam.esm.repository.api.TagRepository;
import com.epam.esm.service.api.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@EnableTransactionManagement
@Transactional
public class TagServiceImpl implements TagService {

    public static final String TAG_NOT_FOUND = "tag.not.found";
    public static final String TAG_ERROR_INVALID_ID = "tag.error.invalid.id";

    private final RequestParametersMapper parametersMapper;
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(RequestParametersMapper parametersMapper,
                          TagRepository tagRepository) {
        this.parametersMapper = parametersMapper;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAllTags(List<String> sortParams, int limit, int offset) {
        if (limit < 0) throw new InvalidPaginationParameterException(
                "limit", limit + "", ErrorCodes.INVALID_PAGINATION_PARAMETER);
        if (offset < 0) throw new InvalidPaginationParameterException(
                "offset", offset + "", ErrorCodes.INVALID_PAGINATION_PARAMETER);
        String sortParameters = parametersMapper.mapSortParameters(sortParams);
        return tagRepository.getAllTags(sortParameters, limit, offset);
    }

    @Override
    public List<Tag> searchAllTags(List<String> sortParams, int limit, int offset, String searchQuery) {
        if (limit < 0) throw new InvalidPaginationParameterException(
                "limit", limit + "", ErrorCodes.INVALID_PAGINATION_PARAMETER);
        if (offset < 0) throw new InvalidPaginationParameterException(
                "offset", offset + "", ErrorCodes.INVALID_PAGINATION_PARAMETER);
        String sortParameters = parametersMapper.mapSortParameters(sortParams);
        return tagRepository.searchAllTags(sortParameters, limit, offset, searchQuery);
    }

    @Override
    public List<Tag> getTagsByCertificateId(List<String> sortParams, int limit, int offset, Long certificateId) {
        if (certificateId == null || certificateId < 1) throw new InvalidResourcePropertyException(
                "certificate.error.invalid.id", certificateId, ErrorCodes.INVALID_CERTIFICATE_ID_PROPERTY);
        if (limit < 0) throw new InvalidPaginationParameterException(
                "limit", limit + "", ErrorCodes.INVALID_PAGINATION_PARAMETER);
        if (offset < 0) throw new InvalidPaginationParameterException(
                "offset", offset + "", ErrorCodes.INVALID_PAGINATION_PARAMETER);
        String sortParameters = parametersMapper.mapSortParameters(sortParams);
        return tagRepository.getTagsByCertificateId(sortParameters, limit, offset, certificateId);
    }

    @Override
    public Tag getTagById(Long tagId) {
        if (tagId == null || tagId < 1) throw new InvalidResourcePropertyException(
                TAG_ERROR_INVALID_ID, tagId, ErrorCodes.INVALID_TAG_ID_PROPERTY);
        return tagRepository.getTagById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        TAG_NOT_FOUND, tagId, ErrorCodes.NOT_FOUND_TAG_RESOURCE));
    }

    @Override
    public Tag addTag(Tag tag) {
        if (tag.getName() == null || tag.getName().isEmpty() || tag.getName().equals("null"))
            throw new InvalidResourceNameException(
                    "tag.error.invalid.name", tag.getName(), ErrorCodes.INVALID_TAG_NAME_PROPERTY);
        return tagRepository.addTag(tag);
    }

    @Override
    public Tag updateTagById(Long tagId, Map<String, String> body) {
        if (tagId == null || tagId < 1)
            throw new InvalidResourcePropertyException(
                    TAG_ERROR_INVALID_ID, tagId, ErrorCodes.INVALID_TAG_ID_PROPERTY);
        Long bodyTagId = (body.containsKey("id")) ? Long.parseLong((String) body.get("id")) : null;
        if (bodyTagId == null || bodyTagId < 1 || !bodyTagId.equals(tagId)) throw new InvalidResourcePropertyException(
                    "tag.error.paramId.no.equals.bodyId", bodyTagId, ErrorCodes.INVALID_TAG_ID_PROPERTY);
        Tag tagToUpdate = tagRepository.getTagById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        TAG_NOT_FOUND, tagId, ErrorCodes.NOT_FOUND_TAG_RESOURCE));
        if (body.containsKey("name")) tagToUpdate.setName((String) body.get("name"));
        if (tagToUpdate.getName() == null ||
                tagToUpdate.getName().isEmpty() ||
                tagToUpdate.getName().equals("null")) throw new InvalidResourceNameException(
                    "tag.error.invalid.name", tagToUpdate.getName(), ErrorCodes.INVALID_TAG_NAME_PROPERTY);
        return tagRepository.updateTagById(tagId, tagToUpdate);
    }

    @Override
    public Tag deleteTagById(Long tagId) {
        if (tagId == null || tagId < 1) throw new InvalidResourcePropertyException(
                TAG_ERROR_INVALID_ID, tagId, ErrorCodes.INVALID_TAG_ID_PROPERTY);
        ResourceNotFoundException resourceNotFoundException = new ResourceNotFoundException(
                TAG_NOT_FOUND, tagId, ErrorCodes.NOT_FOUND_TAG_RESOURCE);
        Tag deletedTag = tagRepository.getTagById(tagId)
                .orElseThrow(() -> resourceNotFoundException);
        if (!tagRepository.deleteTagById(tagId)) throw resourceNotFoundException;
        return deletedTag;
    }
}
