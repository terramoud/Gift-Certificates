package com.epam.esm.service.impl;

import com.epam.esm.config.ConfigApplicationContextService;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.entity.mapper.CertificateMapper;
import com.epam.esm.domain.entity.mapper.RequestParametersMapper;
import com.epam.esm.domain.payload.CertificateRequest;
import com.epam.esm.exceptions.InvalidPaginationParameterException;
import com.epam.esm.exceptions.InvalidResourceNameException;
import com.epam.esm.exceptions.InvalidResourcePropertyException;
import com.epam.esm.exceptions.ResourceNotFoundException;
import com.epam.esm.repository.api.CertificateRepository;
import com.epam.esm.repository.api.TagRepository;
import com.epam.esm.service.api.CertificateService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ConfigApplicationContextService.class})
class CertificateServiceImplTest {

    /**
     * Create a mock implementation of the TagRepository
     */
    @Autowired
    @Qualifier("mockCertificateRepository")
    private CertificateRepository certificateRepository;

    @Autowired
    @Qualifier("mockTagRepository")
    public TagRepository tagRepository;

    @Autowired
    @Qualifier("mockParametersMapper")
    public RequestParametersMapper parametersMapper;

    @Autowired
    public CertificateService certificateService;

    @Autowired
    @Qualifier("mockCertificateMapper")
    public CertificateMapper certificateMapper;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * @see CertificateServiceImpl#getAllCertificates(List, int, int)
     */
    @Test
    void testGetAllCertificatesShouldReturnSortedListCertificatesByCreateDateAndName() {
        List<Certificate> expected = LongStream.range(0, 9L)
                .mapToObj(i -> {
                    Certificate certificate = getCertificateExample();
                    certificate.setName("name" + i);
                    certificate.setCreateDate("2018-02-25T12:49:3" + i + ".018");
                    return certificate;
                })
                .sorted(Comparator.comparing(Certificate::getCreateDate, Comparator.reverseOrder())
                        .thenComparing(Certificate::getName))
                .collect(Collectors.toList());
        List<String> sortParams = List.of("create_data:desc", "name:asc");
        String mappedSortParams = "create_data desc,name asc";
        when(parametersMapper.mapSortParameters(anyList())).thenReturn(mappedSortParams);
        when(certificateRepository.getAllCertificates(anyString(), anyInt(), anyInt())).thenReturn(expected);
        assertEquals(expected, certificateService.getAllCertificates(sortParams, 5, 3));
    }

    /**
     * @see CertificateServiceImpl#getAllCertificates(List, int, int)
     */
    @Test
    void testGetAllCertificatesShouldThrowExceptionWhenInvalidLimitOrOffset() {
        List<Certificate> expected = LongStream.range(0, 9L)
                .mapToObj(i -> {
                    Certificate certificate = getCertificateExample();
                    certificate.setName("name" + i);
                    certificate.setCreateDate("2018-02-25T12:49:3" + i + ".018");
                    return certificate;
                })
                .sorted(Comparator.comparing(Certificate::getCreateDate, Comparator.reverseOrder())
                        .thenComparing(Certificate::getName))
                .collect(Collectors.toList());
        List<String> sortParams = List.of("create_data:desc", "name:asc");
        String mappedSortParams = "create_data desc,name asc";
        when(parametersMapper.mapSortParameters(anyList())).thenReturn(mappedSortParams);
        when(certificateRepository.getAllCertificates(anyString(), anyInt(), anyInt())).thenReturn(expected);
        assertThrows(InvalidPaginationParameterException.class,
                () -> certificateService.getAllCertificates(sortParams, -1, 3));
        assertThrows(InvalidPaginationParameterException.class,
                () -> certificateService.getAllCertificates(sortParams, -1, -3));
        assertThrows(InvalidPaginationParameterException.class,
                () -> certificateService.getAllCertificates(sortParams, 5, -3));
    }

    /**
     * @see CertificateServiceImpl#getAllCertificatesByTagName(List, int, int, String)
     */
    @Test
    void testGetAllCertificatesByTagNameShouldReturnSortedListCertificatesByCreateDateAndName() {
        List<Certificate> expected = LongStream.range(0, 9L)
                .mapToObj(i -> {
                    Certificate certificate = getCertificateExample();
                    certificate.setName("name" + i);
                    certificate.setCreateDate("2018-02-25T12:49:3" + i + ".018");
                    return certificate;
                })
                .sorted(Comparator.comparing(Certificate::getCreateDate, Comparator.reverseOrder())
                        .thenComparing(Certificate::getName))
                .collect(Collectors.toList());
        List<String> sortParams = List.of("create_data:desc", "name:asc");
        String mappedSortParams = "create_data desc,name asc";
        when(parametersMapper.mapSortParameters(anyList())).thenReturn(mappedSortParams);
        when(certificateRepository.getAllCertificatesByTagName(anyString(), anyInt(), anyInt(), anyString()))
                .thenReturn(expected);
        assertEquals(expected,
                certificateService.getAllCertificatesByTagName(sortParams, 5, 3, "tag name"));
    }

    /**
     * @see CertificateServiceImpl#getAllCertificatesByTagName(List, int, int, String)
     */
    @Test
    void testGetAllCertificatesByTagNameShouldThrowExceptionWhenInvalidLimitOrOffsetOrTagName() {
        List<Certificate> expected = LongStream.range(0, 9L)
                .mapToObj(i -> {
                    Certificate certificate = getCertificateExample();
                    certificate.setName("name" + i);
                    certificate.setCreateDate("2018-02-25T12:49:3" + i + ".018");
                    return certificate;
                })
                .sorted(Comparator.comparing(Certificate::getCreateDate, Comparator.reverseOrder())
                        .thenComparing(Certificate::getName))
                .collect(Collectors.toList());
        List<String> sortParams = List.of("create_data:desc", "name:asc");
        String mappedSortParams = "create_data desc,name asc";
        when(parametersMapper.mapSortParameters(anyList())).thenReturn(mappedSortParams);
        when(certificateRepository.getAllCertificatesByTagName(anyString(), anyInt(), anyInt(), anyString()))
                .thenReturn(expected);
        assertThrows(InvalidResourceNameException.class,
                () -> certificateService.getAllCertificatesByTagName(sortParams, 1, 3, ""));
        assertThrows(InvalidResourceNameException.class,
                () -> certificateService.getAllCertificatesByTagName(sortParams, 1, 3, "null"));
        assertThrows(InvalidResourceNameException.class,
                () -> certificateService.getAllCertificatesByTagName(sortParams, 1, 3, null));
    }

    /**
     * @see CertificateServiceImpl#getAllCertificatesByTagId(List, int, int, Long)
     */
    @Test
    void testGetAllCertificatesByTagIdShouldReturnSortedListCertificatesByCreateDateAndName() {
        List<Certificate> expected = LongStream.range(0, 9L)
                .mapToObj(i -> {
                    Certificate certificate = getCertificateExample();
                    certificate.setName("name" + i);
                    certificate.setCreateDate("2018-02-25T12:49:3" + i + ".018");
                    return certificate;
                })
                .sorted(Comparator.comparing(Certificate::getCreateDate, Comparator.reverseOrder())
                        .thenComparing(Certificate::getName))
                .collect(Collectors.toList());
        List<String> sortParams = List.of("create_data:desc", "name:asc");
        String mappedSortParams = "create_data desc,name asc";
        when(parametersMapper.mapSortParameters(anyList())).thenReturn(mappedSortParams);
        when(certificateRepository.getAllCertificatesByTagId(anyString(), anyInt(), anyInt(), anyLong()))
                .thenReturn(expected);
        assertEquals(expected, certificateService.getAllCertificatesByTagId(sortParams, 5, 3, 1L));
    }

    /**
     * @see CertificateServiceImpl#getAllCertificatesByTagId(List, int, int, Long)
     */
    @Test
    void testGetAllCertificatesByTagIdShouldThrowExceptionWhenInvalidLimitOrOffsetOrTagId() {
        List<Certificate> expected = LongStream.range(0, 9L)
                .mapToObj(i -> {
                    Certificate certificate = getCertificateExample();
                    certificate.setName("name" + i);
                    certificate.setCreateDate("2018-02-25T12:49:3" + i + ".018");
                    return certificate;
                })
                .sorted(Comparator.comparing(Certificate::getCreateDate, Comparator.reverseOrder())
                        .thenComparing(Certificate::getName))
                .collect(Collectors.toList());
        List<String> sortParams = List.of("create_data:desc", "name:asc");
        String mappedSortParams = "create_data desc,name asc";
        when(parametersMapper.mapSortParameters(anyList())).thenReturn(mappedSortParams);
        when(certificateRepository.getAllCertificatesByTagId(anyString(), anyInt(), anyInt(), anyLong()))
                .thenReturn(expected);
        assertThrows(InvalidPaginationParameterException.class,
                () -> certificateService.getAllCertificatesByTagId(sortParams, -1, 3, 1L));
        assertThrows(InvalidPaginationParameterException.class,
                () -> certificateService.getAllCertificatesByTagId(sortParams, -1, -3, 1L));
        assertThrows(InvalidPaginationParameterException.class,
                () -> certificateService.getAllCertificatesByTagId(sortParams, 1, -3, 1L));
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.getAllCertificatesByTagId(sortParams, 1, 3, 0L));
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.getAllCertificatesByTagId(sortParams, 1, 3, -1L));
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.getAllCertificatesByTagId(sortParams, 1, 3, null));
    }

    /**
     * @see CertificateServiceImpl#getCertificateById(Long)
     */
    @Test
    void testGetCertificateByIdReturnCertificate() {
        Certificate expectedCertificate = getCertificateExample();
        when(certificateRepository.getCertificateById(anyLong())).thenReturn(Optional.of(expectedCertificate));
        assertEquals(expectedCertificate, certificateService.getCertificateById(1L));
    }

    /**
     * @see CertificateServiceImpl#getCertificateById(Long)
     */
    @Test
    void testGetCertificateByIdShouldThrowExceptionWhenInvalidCertificateId() {
        Certificate expectedCertificate = getCertificateExample();
        when(certificateRepository.getCertificateById(anyLong())).thenReturn(Optional.of(expectedCertificate));
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.getCertificateById(0L));
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.getCertificateById(-1L));
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.getCertificateById(null));
    }

    /**
     * @see CertificateServiceImpl#getCertificateById(Long)
     */
    @Test
    void testGetCertificateByIdShouldThrowExceptionWhenCertificateIsNotFound() {
        Certificate expectedCertificate = getCertificateExample();
        when(certificateRepository.getCertificateById(anyLong())).thenReturn(Optional.of(expectedCertificate));
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.getCertificateById(0L));
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.getCertificateById(-1L));
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.getCertificateById(null));
    }

    /**
     * @see CertificateServiceImpl#addCertificate(CertificateRequest)
     */
    @Test
    void testAddCertificateShouldReturnCertificateWhenCertificateSuccessfullyCreated() {
        CertificateRequest certificateRequest = getCertificateRequestExample();
        Certificate expectedCertificate = getCertificateExample();
        when(certificateMapper.mapToCertificate(any())).thenReturn(expectedCertificate);
        when(tagRepository.getTagById(any())).thenReturn(Optional.of(new Tag()));
        when(certificateRepository.addCertificate(any(), anyLong())).thenReturn(expectedCertificate);
        assertEquals(expectedCertificate, certificateService.addCertificate(certificateRequest));
    }

    /**
     * @see CertificateServiceImpl#addCertificate(CertificateRequest)
     */
    @Test
    void testAddCertificateShouldThrowExceptionWhenInvalidCertificatePropertyOrTagIdOrTagIsNotExist() {
        Certificate certificate = getCertificateExample();
        when(certificateMapper.mapToCertificate(any())).thenReturn(certificate);
        when(certificateRepository.addCertificate(any(), anyLong())).thenReturn(certificate);
        CertificateRequest certificateRequest = getCertificateRequestExample();
        certificateRequest.setTagId(0L);
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.addCertificate(certificateRequest));
        certificateRequest.setTagId(-1L);
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.addCertificate(certificateRequest));
        certificateRequest.setTagId(10L);
        when(tagRepository.getTagById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> certificateService.addCertificate(certificateRequest));
        when(tagRepository.getTagById(any())).thenReturn(Optional.of(new Tag()));
        certificate.setName("");
        assertThrows(InvalidResourceNameException.class,
                () -> certificateService.addCertificate(certificateRequest));
        certificate.setName("null");
        assertThrows(InvalidResourceNameException.class,
                () -> certificateService.addCertificate(certificateRequest));
        certificate.setName(null);
        assertThrows(InvalidResourceNameException.class,
                () -> certificateService.addCertificate(certificateRequest));
        certificate.setName("some name");
        certificate.setDescription("");
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.addCertificate(certificateRequest));
        certificate.setDescription("null");
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.addCertificate(certificateRequest));
        certificate.setDescription(null);
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.addCertificate(certificateRequest));
        certificate.setDescription("some description");
        certificate.setPrice(null);
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.addCertificate(certificateRequest));
        certificate.setPrice(BigDecimal.valueOf(-1L));
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.addCertificate(certificateRequest));
        certificate.setPrice(new BigDecimal("499.99"));
        certificate.setDuration(null);
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.addCertificate(certificateRequest));
        certificate.setDuration(0);
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.addCertificate(certificateRequest));
        certificate.setDuration(-1);
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.addCertificate(certificateRequest));
        certificate.setDuration(30);
        assertThrows(NullPointerException.class,
                () -> certificateService.addCertificate(null));
    }

    /**
     * @see CertificateServiceImpl#deleteCertificateById(Long)
     */
    @Test
    void testDeleteCertificateByIdShouldReturnCertificateWhenTagSuccessfullyDeleted() {
        Certificate certificateToDelete = getCertificateExample();
        when(certificateRepository.getCertificateById(anyLong())).thenReturn(Optional.of(certificateToDelete));
        when(certificateRepository.deleteCertificateById(anyLong())).thenReturn(true);
        assertEquals(certificateToDelete, certificateService.deleteCertificateById(1L));
    }

    /**
     * @see CertificateServiceImpl#deleteCertificateById(Long)
     */
    @Test
    void testDeleteCertificateByIdShouldThrowExceptionWhenInvalidCertificateId() {
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.deleteCertificateById(0L));
        assertThrows(InvalidResourcePropertyException.class,
                () -> certificateService.deleteCertificateById(-1L));
    }

    /**
     * @see CertificateServiceImpl#deleteCertificateById(Long)
     */
    @Test
    void testDeleteCertificateByIdShouldThrowExceptionWhenCertificateIsNotFound() {
        when(certificateRepository.getCertificateById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> certificateService.deleteCertificateById(1L));
    }

    /**
     * @see CertificateServiceImpl#deleteCertificateById(Long)
     */
    @Test
    void testDeleteCertificateByIdShouldThrowExceptionWhenCertificateIsNotDeleted() {
        Certificate certificateToDelete = getCertificateExample();
        when(certificateRepository.getCertificateById(anyLong())).thenReturn(Optional.of(certificateToDelete));
        when(certificateRepository.deleteCertificateById(anyLong())).thenReturn(false);
        assertThrows(ResourceNotFoundException.class,
                () -> certificateService.deleteCertificateById(1L));
    }

    private Certificate getCertificateExample() {
        Certificate certificate = new Certificate();
        certificate.setId(1L);
        certificate.setName("name");
        certificate.setDescription("description");
        certificate.setPrice(new BigDecimal("499.99"));
        certificate.setDuration(30);
        certificate.setCreateDate("2018-02-25T12:49:35.018");
        certificate.setLastUpdateDate(LocalDateTime.now());
        return certificate;
    }

    private CertificateRequest getCertificateRequestExample() {
        CertificateRequest certificateRequest = new CertificateRequest();
        certificateRequest.setTagId(10L);
        certificateRequest.setId(1L);
        certificateRequest.setName("name");
        certificateRequest.setDescription("description");
        certificateRequest.setPrice(new BigDecimal("499.99"));
        certificateRequest.setDuration(30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .withZone(ZoneId.of("UTC"));
        LocalDateTime createTime = LocalDateTime.parse("2018-02-25T12:49:35.018", formatter);
        certificateRequest.setCreateDate(createTime);
        certificateRequest.setLastUpdateDate(LocalDateTime.now());
        return certificateRequest;
    }
}