package com.epam.esm.service.impl;

import com.epam.esm.config.SpringWebConfig;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringWebConfig.class}) // TODO: Cannot import this class, @see build.gradle
class TagServiceImplTest {
    /**
     * Autowire in the service we want to test
     */
    @Autowired
    public TagServiceImpl tagService; // TODO: thus cannot initialize this bean

    /**
     * Create a mock implementation of the WidgetRepository
     */
    @Mock
    TagRepositoryImpl mockTagRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllTags() {
        assertEquals("tag", "tag");
    } // Simple check whether test is working

    @Test
    void testGetAllTags() {
    }


    @Test
    void getTagsByCertificateId() {
    }

    /**
     * @see TagServiceImpl#getTagById(Long)
     */
    @Test
    void getTagById() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("testTag");
        when(mockTagRepository.getTagById(anyLong())).thenReturn((Optional.of(tag)));
        assertEquals(tag, tagService.getTagById(tag.getId()));
    }

    @Test
    void addTag() {
    }

    @Test
    void updateTagById() {
    }

    @Test
    void deleteTagById() {
    }


}