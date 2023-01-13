package com.epam.esm.service.api;

import com.epam.esm.domain.entity.Tag;

import java.util.List;
import java.util.Map;


public interface TagService {

    List<Tag> getAllTags(List<String> sortParameters, int limit, int offset);

    List<Tag> searchAllTags(List<String> sortParams, int limit, int offset, String searchQuery);

    List<Tag> getTagsByCertificateId(List<String> sortParams, int limit, int offset, Long certificateId);

    Tag getTagById(Long tagId);

    Tag addTag(Tag tag);

    Tag updateTagById(Long tagId, Map<String, String> body);

    Tag deleteTagById(Long tagId);
}
