package com.epam.esm.repository.api;

import com.epam.esm.domain.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Optional<Tag> getTagById(Long tagId);

    List<Tag> getAllTags(List<String> sortParameters);

    List<Tag> getAllTags(List<String> sortParameters, String query);

    List<Tag> getTagsByCertificateId(Long certificateId);

    Tag addTag(Tag tag);

    Tag updateTagById(Long tagId, Tag tag);

    boolean deleteTagById(Long tagId);
}
