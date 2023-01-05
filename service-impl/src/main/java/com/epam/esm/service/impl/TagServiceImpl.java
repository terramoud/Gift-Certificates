package com.epam.esm.service.impl;


import com.epam.esm.domain.entity.Tag;
import com.epam.esm.exceptions.ErrorCodes;
import com.epam.esm.exceptions.InvalidResourceNameException;
import com.epam.esm.exceptions.ResourceNotFoundException;
import com.epam.esm.exceptions.InvalidResourcePropertyException;
import com.epam.esm.repository.api.TagRepository;
import com.epam.esm.service.api.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    public static final String TAG_NOT_FOUND = "tag.not.found";
    public static final String TAG_ERROR_INVALID_ID = "tag.error.invalid.id";

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAllTags(List<String> sortParameters) {
        return tagRepository.getAllTags(sortParameters);
    }

    @Override
    public List<Tag> getAllTags(List<String> sortParameters, String query) {
        return tagRepository.getAllTags(sortParameters, query);
    }

    @Override
    public List<Tag> getTagsByCertificateId(Long certificateId) {
        if (certificateId == null || certificateId < 1 ) throw new InvalidResourcePropertyException(
                "certificate.error.invalid.id", certificateId, ErrorCodes.INVALID_CERTIFICATE_ID_PROPERTY);
        return tagRepository.getTagsByCertificateId(certificateId);
    }

    @Override
    public Tag getTagById(Long tagId) {
        if (tagId == null || tagId < 1 ) throw new InvalidResourcePropertyException(
                TAG_ERROR_INVALID_ID, tagId, ErrorCodes.INVALID_TAG_ID_PROPERTY);
        return tagRepository.getTagById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        TAG_NOT_FOUND, tagId, ErrorCodes.NOT_FOUND_TAG_RESOURCE));
    }

    @Override
    public Tag addTag(Tag tag) {
        if (tag.getName() == null || tag.getName().isEmpty() || tag.getName().equals("null"))
            throw new InvalidResourceNameException(
                    "tag.error.invalid.name", tag.getId(), ErrorCodes.INVALID_TAG_NAME_PROPERTY);
        return tagRepository.addTag(tag);
    }

    @Override
    public Tag updateTagById(Long tagId, Map<String, Object> body) {
        if (tagId == null || tagId < 1) throw new InvalidResourcePropertyException(
                TAG_ERROR_INVALID_ID, tagId, ErrorCodes.INVALID_TAG_ID_PROPERTY);
        Tag tagToUpdate = tagRepository.getTagById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        TAG_NOT_FOUND, tagId, ErrorCodes.NOT_FOUND_TAG_RESOURCE));
        if (body.containsKey("name")) tagToUpdate.setName((String) body.get("name"));
        String tagName = tagToUpdate.getName();
        if (tagName == null || tagName.isEmpty() || tagName.equals("null")) {
            throw new InvalidResourcePropertyException(
                    "tag.error.invalid.name", tagId, ErrorCodes.INVALID_TAG_NAME_PROPERTY);
        }
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
