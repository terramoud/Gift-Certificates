package com.epam.esm.repository.api;

import com.epam.esm.domain.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    List<Tag> getAllTags(String sortParameters, int limit, int offset);

    List<Tag> searchAllTags(String sortParams, int limit, int offset, String searchQuery);

    List<Tag> getTagsByCertificateId(String sortParams, int limit, int offset, Long certificateId);

    Optional<Tag> getTagById(Long tagId);

    Tag addTag(Tag tag);

    Tag updateTagById(Long tagId, Tag tag);

    boolean deleteTagById(Long tagId);
}
