package com.epam.esm.controller;

import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.service.api.CertificateService;
import com.epam.esm.service.api.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/tags")
public class TagController {

    private final CertificateService certificateService;
    private TagService tagService;

    @Autowired
    public TagController(TagService tagService, CertificateService certificateService) {
        this.tagService = tagService;
        this.certificateService = certificateService;
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags(
            @RequestParam(value = "sort-by", defaultValue = "name:asc") List<String> sortParams,
            @RequestParam(value = "search-by", defaultValue = "") String searchQuery,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            @RequestParam(value = "start", defaultValue = "0") int offset) {
        if (limit < 1) limit = 1;
        if (offset < 0) offset = 0;
        List<Tag> tags;
        if (searchQuery != null) {
            tags = tagService.searchAllTags(sortParams, limit, offset, searchQuery);
            return new ResponseEntity<>(tags, HttpStatus.OK);
        }
        tags = tagService.getAllTags(sortParams, limit, offset);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/{tag-id}/gift-certificates")
    public ResponseEntity<List<Certificate>> getGiftCertificatesByTagId(
            @PathVariable("tag-id") Long tagId,
            @RequestParam(value = "sort-by", defaultValue = "name:asc") List<String> sortParams,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            @RequestParam(value = "start", defaultValue = "0") int offset) {
        List<Certificate> giftCertificates =
                certificateService.getAllCertificatesByTagId(sortParams, limit, offset, tagId);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    @GetMapping("/{tag-id}")
    public ResponseEntity<Tag> getTagById(@PathVariable("tag-id") Long tagId) {
        Tag tag = tagService.getTagById(tagId);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Tag> addTag(@RequestBody Tag tag) {
        Tag addedTag = tagService.addTag(tag);
        return new ResponseEntity<>(addedTag, HttpStatus.CREATED);
    }

    @PutMapping("/{tag-id}")
    public ResponseEntity<Tag> updateTagById(
            @PathVariable("tag-id") Long tagId,
            @RequestBody Map<String, String> body) {
        Tag updatedTag = tagService.updateTagById(tagId, body);
        return new ResponseEntity<>(updatedTag, HttpStatus.OK);
    }

    @DeleteMapping("/{tag-id}")
    public ResponseEntity<Tag> deleteTagById(@PathVariable("tag-id") Long tagId) {
        Tag tag = tagService.deleteTagById(tagId);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }
}
