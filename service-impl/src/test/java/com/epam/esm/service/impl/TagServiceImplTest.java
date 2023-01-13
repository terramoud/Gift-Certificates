package com.epam.esm.service.impl;

import com.epam.esm.config.ConfigApplicationContextService;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.entity.mapper.RequestParametersMapper;
import com.epam.esm.exceptions.InvalidPaginationParameterException;
import com.epam.esm.exceptions.InvalidResourceNameException;
import com.epam.esm.exceptions.InvalidResourcePropertyException;
import com.epam.esm.exceptions.ResourceNotFoundException;
import com.epam.esm.repository.api.TagRepository;
import com.epam.esm.service.api.TagService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ConfigApplicationContextService.class})
class TagServiceImplTest {

    /**
     * Create a mock implementation of the TagRepository
     */
    @Autowired
    @Qualifier("mockTagRepository")
    public TagRepository tagRepository;

    @Autowired
    @Qualifier("mockParametersMapper")
    public RequestParametersMapper parametersMapper;

    @Autowired
    public TagService tagService;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    /**
     * @see TagServiceImpl#getAllTags(List, int, int)
     */
    @Test
    void testGetAllTagsShouldReturnSortedListTagsByNameAndId() {
        List<Tag> expected = LongStream.range(0, 9L)
                .mapToObj(i -> new Tag(i, "tagName" + i))
                .sorted(Comparator.comparing(Tag::getName)
                        .thenComparing(Tag::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        List<String> sortParams = List.of("name:asc", "id:desc");
        String mappedSortParams = "name asc,id desc";
        when(parametersMapper.mapSortParameters(anyList())).thenReturn(mappedSortParams);
        when(tagRepository.getAllTags(anyString(), anyInt(), anyInt())).thenReturn(expected);
        assertEquals(expected, tagService.getAllTags(sortParams, 5, 3));
    }

    /**
     * @see TagServiceImpl#getAllTags(List, int, int)
     */
    @Test
    void testGetAllTagsShouldThrowExceptionWhenInvalidLimitOrOffset() {
        List<Tag> expected = LongStream.range(0, 9L)
                .mapToObj(i -> new Tag(i, "tagName" + i))
                .sorted(Comparator.comparing(Tag::getName)
                        .thenComparing(Tag::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        List<String> sortParams = List.of("name:asc", "id:desc");
        String mappedSortParams = "name asc,id desc";
        when(parametersMapper.mapSortParameters(anyList())).thenReturn(mappedSortParams);
        when(tagRepository.getAllTags(anyString(), anyInt(), anyInt())).thenReturn(expected);
        assertThrows(InvalidPaginationParameterException.class,
                () -> tagService.getAllTags(sortParams, -1, 3));
        assertThrows(InvalidPaginationParameterException.class,
                () -> tagService.getAllTags(sortParams, -1, -3));
        assertThrows(InvalidPaginationParameterException.class,
                () -> tagService.getAllTags(sortParams, 5, -3));
    }

    /**
     * @see TagServiceImpl#searchAllTags(List, int, int, String)
     */
    @Test
    void testSearchAllTagsShouldReturnSortedListTagsByNameAndId() {
        List<Tag> expected = LongStream.range(0, 9L)
                .mapToObj(i -> new Tag(i, "tagName" + i))
                .sorted(Comparator.comparing(Tag::getName)
                        .thenComparing(Tag::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        List<String> sortParams = List.of("name:asc", "id:desc");
        String mappedSortParams = "name asc,id desc";
        when(parametersMapper.mapSortParameters(anyList())).thenReturn(mappedSortParams);
        when(tagRepository.searchAllTags(anyString(), anyInt(), anyInt(), anyString())).thenReturn(expected);
        assertEquals(expected, tagService.searchAllTags(sortParams, 5, 3, "some tag name"));
    }

    /**
     * @see TagServiceImpl#searchAllTags(List, int, int, String)
     */
    @Test
    void testSearchAllTagsShouldThrowExceptionWhenInvalidLimitOrOffset() {
        List<Tag> expected = LongStream.range(0, 9L)
                .mapToObj(i -> new Tag(i, "tagName" + i))
                .sorted(Comparator.comparing(Tag::getName)
                        .thenComparing(Tag::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        List<String> sortParams = List.of("name:asc", "id:desc");
        String mappedSortParams = "name asc,id desc";
        when(parametersMapper.mapSortParameters(anyList())).thenReturn(mappedSortParams);
        when(tagRepository.searchAllTags(anyString(), anyInt(), anyInt(), anyString())).thenReturn(expected);
        assertThrows(InvalidPaginationParameterException.class,
                () -> tagService.searchAllTags(sortParams, -1, 3, "some tag name"));
        assertThrows(InvalidPaginationParameterException.class,
                () -> tagService.searchAllTags(sortParams, -1, -3, "some tag name"));
        assertThrows(InvalidPaginationParameterException.class,
                () -> tagService.searchAllTags(sortParams, 5, -3, "some tag name"));
    }

    /**
     * @see TagService#getTagsByCertificateId(List, int, int, Long)
     */
    @Test
    void testGetTagsByCertificateIdShouldReturnSortedListTagsByNameAndId() {
        List<Tag> expected = LongStream.range(0, 9L)
                .mapToObj(i -> new Tag(i, "tagName" + i))
                .sorted(Comparator.comparing(Tag::getName)
                        .thenComparing(Tag::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        List<String> sortParams = List.of("name:asc", "id:desc");
        String mappedSortParams = "name asc,id desc";
        when(parametersMapper.mapSortParameters(anyList())).thenReturn(mappedSortParams);
        when(tagRepository.getTagsByCertificateId(anyString(), anyInt(), anyInt(), anyLong())).thenReturn(expected);
        assertEquals(expected, tagService.getTagsByCertificateId(sortParams, 5, 3, 1L));
    }

    /**
     * @see TagService#getTagsByCertificateId(List, int, int, Long)
     */
    @Test
    void testGetTagsByCertificateIdShouldThrowExceptionWhenInvalidLimitOrOffsetOrCertificateId() {
        List<Tag> expected = LongStream.range(0, 9L)
                .mapToObj(i -> new Tag(i, "tagName" + i))
                .sorted(Comparator.comparing(Tag::getName)
                        .thenComparing(Tag::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        List<String> sortParams = List.of("name:asc", "id:desc");
        String mappedSortParams = "name asc,id desc";
        when(parametersMapper.mapSortParameters(anyList())).thenReturn(mappedSortParams);
        when(tagRepository.getTagsByCertificateId(anyString(), anyInt(), anyInt(), anyLong())).thenReturn(expected);
        assertThrows(InvalidPaginationParameterException.class,
                () -> tagService.getTagsByCertificateId(sortParams, -1, 3, 1L));
        assertThrows(InvalidPaginationParameterException.class,
                () -> tagService.getTagsByCertificateId(sortParams, -1, -3, 1L));
        assertThrows(InvalidPaginationParameterException.class,
                () -> tagService.getTagsByCertificateId(sortParams, 5, -3, 1L));
        assertThrows(InvalidResourcePropertyException.class,
                () -> tagService.getTagsByCertificateId(sortParams, 5, 3, 0L));
        assertThrows(InvalidResourcePropertyException.class,
                () -> tagService.getTagsByCertificateId(sortParams, 5, 3, -1L));
        assertThrows(InvalidResourcePropertyException.class,
                () -> tagService.getTagsByCertificateId(sortParams, 5, 3, null));
    }

    /**
     * @see TagServiceImpl#getTagById(Long)
     */
    @Test
    void testGetTagByIdShouldReturnTag() {
        Tag tag = new Tag(1L, "language courses");
        when(tagRepository.getTagById(anyLong())).thenReturn(Optional.of(tag));
        assertEquals(tag, tagService.getTagById(tag.getId()));
    }

    /**
     * @see TagServiceImpl#getTagById(Long)
     */
    @Test
    void testGetTagByIdShouldThrowExceptionWhenInvalidTagId() {
        Tag tag = new Tag(1L, "language courses");
        when(tagRepository.getTagById(anyLong())).thenReturn(Optional.of(tag));
        assertThrows(InvalidResourcePropertyException.class, () -> tagService.getTagById(0L));
        assertThrows(InvalidResourcePropertyException.class, () -> tagService.getTagById(-1L));
        assertThrows(InvalidResourcePropertyException.class, () -> tagService.getTagById(null));
    }

    /**
     * @see TagServiceImpl#getTagById(Long)
     */
    @Test
    void testGetTagByIdShouldThrowExceptionWhenTagIsNotFound() {
        when(tagRepository.getTagById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tagService.getTagById(1L));
    }

    /**
     * @see TagServiceImpl#addTag(Tag)
     */
    @Test
    void testAddTagShouldReturnTagWhenTagSuccessfullyCreated() {
        Tag tag = new Tag(1L, "language courses");
        when(tagRepository.addTag(any())).thenReturn(tag);
        assertEquals(tag, tagService.addTag(tag));
    }

    /**
     * @see TagServiceImpl#addTag(Tag)
     */
    @Test
    void testAddTagShouldThrowExceptionWhenInvalidName() {
        Tag tag = new Tag();
        when(tagRepository.addTag(any())).thenReturn(tag);
        tag.setName("");
        assertThrows(InvalidResourceNameException.class, () -> tagService.addTag(tag));
        tag.setName("null");
        assertThrows(InvalidResourceNameException.class, () -> tagService.addTag(tag));
        tag.setName(null);
        assertThrows(InvalidResourceNameException.class, () -> tagService.addTag(tag));
        assertThrows(NullPointerException.class, () -> tagService.addTag(null));
    }

    /**
     * @see TagService#updateTagById(Long, Map)
     */
    @Test
    void testUpdateTagByIdShouldReturnTagWhenTagSuccessfullyUpdated() {
        Map<String, String> requestBody = Map.of(
                "id", "1L",
                "name", "updated language courses"
        );
        Tag tag = new Tag(1L, "language courses");
        Tag expectedUpdatedTag = new Tag(1L, "updated language courses");
        when(tagRepository.getTagById(anyLong())).thenReturn(Optional.of(tag));
        when(tagRepository.updateTagById(anyLong(), any())).thenReturn(expectedUpdatedTag);
        assertEquals(expectedUpdatedTag, tagService.updateTagById(tag.getId(), requestBody));
    }

    /**
     * @see TagService#updateTagById(Long, Map)
     */
    @Test
    void testUpdateTagByIdShouldThrowExceptionWhenInvalidTagId() {
        Map<String, String> requestBody = Map.of(
                "id", "1L",
                "name", "updated language courses"
        );
        assertThrows(InvalidResourcePropertyException.class, () -> tagService.updateTagById(0L, requestBody));
        assertThrows(InvalidResourcePropertyException.class, () -> tagService.updateTagById(-1L, requestBody));
        assertThrows(InvalidResourcePropertyException.class, () -> tagService.updateTagById(null, requestBody));
        assertThrows(InvalidResourcePropertyException.class, () -> tagService.updateTagById(null, requestBody));
    }

    /**
     * @see TagService#updateTagById(Long, Map)
     */
    @Test
    void testUpdateTagByIdShouldThrowExceptionWhenInvalidTagIdFromRequestBodyOrNotEqualsToTadIdFromParameter() {
        Map<String, String> requestBody = new HashMap<>(Map.of(
                "id", "0L",
                "name", "updated language courses"
        ));
        assertThrows(InvalidResourcePropertyException.class, () -> tagService.updateTagById(1L, requestBody));
        requestBody.put("id", "-1L");
        assertThrows(InvalidResourcePropertyException.class, () -> tagService.updateTagById(1L, requestBody));
        requestBody.put("id", null);
        assertThrows(InvalidResourcePropertyException.class, () -> tagService.updateTagById(1L, requestBody));
        requestBody.put("id", "9999L");
        assertThrows(InvalidResourcePropertyException.class, () -> tagService.updateTagById(1L, requestBody));
    }

    /**
     * @see TagService#updateTagById(Long, Map)
     */
    @Test
    void testUpdateTagByIdShouldThrowExceptionWhenInvalidRequestBodyTagName() {
        Map<String, String> requestBody = new HashMap<>(Map.of(
                "id", "1L",
                "name", "null"
        ));
        Tag tagToUpdate = new Tag(1L, "language courses");
        when(tagRepository.getTagById(anyLong())).thenReturn(Optional.of(tagToUpdate));
        assertThrows(InvalidResourceNameException.class, () -> tagService.updateTagById(1L, requestBody));
        requestBody.put("name", "");
        assertThrows(InvalidResourceNameException.class, () -> tagService.updateTagById(1L, requestBody));
        requestBody.put("name", null);
        assertThrows(InvalidResourceNameException.class, () -> tagService.updateTagById(1L, requestBody));
        requestBody.remove("name");
        assertThrows(InvalidResourceNameException.class, () -> tagService.updateTagById(1L, requestBody));
    }

    /**
     * @see TagServiceImpl#deleteTagById(Long)
     */
    @Test
    void testDeleteTagByIdShouldReturnTagWhenTagSuccessfullyDeleted() {
        Tag tagToDelete = new Tag(1L, "language courses");
        when(tagRepository.getTagById(anyLong())).thenReturn(Optional.of(tagToDelete));
        when(tagRepository.deleteTagById(anyLong())).thenReturn(true);
        assertEquals(tagToDelete, tagService.deleteTagById(1L));
    }

    /**
     * @see TagServiceImpl#deleteTagById(Long)
     */
    @Test
    void testDeleteTagByIdShouldThrowExceptionWhenInvalidTagId() {
        assertThrows(InvalidResourcePropertyException.class, () -> tagService.deleteTagById(0L));
        assertThrows(InvalidResourcePropertyException.class, () -> tagService.deleteTagById(-1L));
    }

    /**
     * @see TagServiceImpl#deleteTagById(Long)
     */
    @Test
    void testDeleteTagByIdShouldThrowExceptionWhenTagIsNotFound() {
        when(tagRepository.getTagById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tagService.deleteTagById(1L));
    }

    /**
     * @see TagServiceImpl#deleteTagById(Long)
     */
    @Test
    void testDeleteTagByIdShouldThrowExceptionWhenTagIsNotDeleted() {
        Tag tagToDelete = new Tag(1L, "language courses");
        when(tagRepository.getTagById(anyLong())).thenReturn(Optional.of(tagToDelete));
        when(tagRepository.deleteTagById(anyLong())).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> tagService.deleteTagById(1L));
    }
}