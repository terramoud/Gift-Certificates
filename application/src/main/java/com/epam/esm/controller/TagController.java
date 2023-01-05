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
import java.util.function.BiFunction;

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

    private final Map<Boolean, BiFunction<List<String>, String, List<Tag>>> getAllTags = Map.of(
            true, (sortParameters, query) -> tagService.getAllTags(sortParameters),
            false, (sortParameters, query) -> tagService.getAllTags(sortParameters, query)
    );

    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags(
            @RequestParam(value = "sort_by", defaultValue = "name:asc") List<String> sortParams,
            @RequestParam(value = "search_by", defaultValue = "") String searchQuery) {
        List<Tag> tags = getAllTags.get(searchQuery.isEmpty()).apply(sortParams, searchQuery);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/{tag_id}")
    public ResponseEntity<Tag> getTagById(@PathVariable("tag_id") Long tagId) {
        Tag tag = tagService.getTagById(tagId);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @GetMapping("/{tag_id}/gift_certificates")
    public ResponseEntity<List<Certificate>> getGiftCertificatesByTagId(@PathVariable("tag_id") Long tagId) {
        List<Certificate> giftCertificates = certificateService.getAllCertificatesByTagId(tagId);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Tag> addTag(@RequestBody Tag tag) {
        Tag addedTag = tagService.addTag(tag);
        return new ResponseEntity<>(addedTag, HttpStatus.CREATED);
    }

    @PutMapping("/{tag_id}")
    public ResponseEntity<Tag> updateTagById(
            @PathVariable("tag_id") Long tagId,
            @RequestBody Map<String, Object> body) {
        Tag updatedTag = tagService.updateTagById(tagId, body);
        return new ResponseEntity<>(updatedTag, HttpStatus.OK);
    }

    @DeleteMapping("/{tag_id}")
    public ResponseEntity<Tag> deleteTagById(@PathVariable("tag_id") Long tagId) {
        Tag tag = tagService.deleteTagById(tagId);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }
}
