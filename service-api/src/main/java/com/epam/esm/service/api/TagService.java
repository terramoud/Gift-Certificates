package com.epam.esm.service.api;

import com.epam.esm.domain.entity.Tag;

import java.util.List;
import java.util.Map;


public interface TagService {

    List<Tag> getAllTags(List<String> sortParameters);

    List<Tag> getAllTags(List<String> sortParameters, String query);

    List<Tag> getTagsByCertificateId(Long certificateId);

    Tag getTagById(Long tagId);

    Tag addTag(Tag tag);

    Tag updateTagById(Long tagId, Map<String, Object> body);

    Tag deleteTagById(Long tagId);
}
