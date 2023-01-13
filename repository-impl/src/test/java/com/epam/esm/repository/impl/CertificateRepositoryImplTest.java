package com.epam.esm.repository.impl;

import com.epam.esm.config.ConfigApplicationContextRepository;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.mapper.CertificateMapper;
import com.epam.esm.domain.entity.mapper.TagRowMapper;
import com.epam.esm.repository.api.CertificateRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

//@TransactionConfiguration(transactionManager = "txMgr", defaultRollback = false)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ConfigApplicationContextRepository.class})
@Sql(value = "file:../sql-to-create-db/schema_H2.sql", executionPhase = BEFORE_TEST_METHOD)
class CertificateRepositoryImplTest {

    @Autowired
    private CertificateRepository certificateRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
//    @Rollback(false)

    /**
     * @see CertificateRepository#getAllCertificates(String, int, int)
     */
    @Test
    void testGetAllCertificatesShouldReturnSortedListCertificatesByCreateDateAndName() {
        List<Certificate> expectedCertificates = List.of(
                getCertificate10(),
                getCertificate8(),
                getCertificate7(),
                getCertificate6(),
                getCertificate5()
        );
        List<Certificate> allCertificates =
                certificateRepository.getAllCertificates("create_date desc,name asc", 5, 0);
        assertEquals(expectedCertificates, allCertificates);
    }

    /**
     * @see CertificateRepository#searchAllCertificates(String, int, int, String)
     */
    @Test
    void testSearchAllCertificatesShouldReturnSortedListCertificatesByCreateDateAndName() {
        List<Certificate> expectedCertificates = List.of(
                getCertificate3(),
                getCertificate2(),
                getCertificate1()
        );
        List<Certificate> allCertificates = certificateRepository.searchAllCertificates(
                "create_date desc,name asc",
                5,
                0,
                "standard");
        assertEquals(expectedCertificates, allCertificates);
    }

    @Test
    void getAllCertificatesByTagName() {
    }

    @Test
    void getAllCertificatesByTagId() {
    }

    /**
     * @see CertificateRepository#getCertificateById(Long)
     */
    @Test
    void testGetCertificateByIdShouldReturnCertificateWithObtainedId() {
        Certificate expectedCertificate = getCertificate1();
        Certificate certificateById = certificateRepository.getCertificateById(1L).get();
        assertEquals(expectedCertificate, certificateById);
    }

    /**
     * @see CertificateRepository#addCertificate(Certificate, Long)
     */
    @Test
    void addCertificate() {
    }

    @Test
    void updateCertificateById() {
    }

    @Test
    void deleteCertificateById() {
    }

    private Certificate getCertificate1() {
        Certificate certificate1 = new Certificate();
        certificate1.setId(1L);
        certificate1.setName("standard");
        certificate1.setDescription("standard level gift certificate");
        certificate1.setPrice(new BigDecimal("999.99"));
        certificate1.setDuration(180);
        certificate1.setCreateDate("2023-01-02T07:37:14.974");
        certificate1.setLastUpdateDate("2023-01-02T07:37:14.974");
        return certificate1;
    }

    private Certificate getCertificate2() {
        Certificate certificate2 = new Certificate();
        certificate2.setId(2L);
        certificate2.setName("standard plus");
        certificate2.setDescription("standard plus level gift certificate");
        certificate2.setPrice(new BigDecimal("699.99"));
        certificate2.setDuration(120);
        certificate2.setCreateDate("2023-01-03T07:37:14.974");
        certificate2.setLastUpdateDate("2023-01-03T07:37:14.974");
        return certificate2;
    }

    private Certificate getCertificate3() {
        Certificate certificate3 = new Certificate();
        certificate3.setId(3L);
        certificate3.setName("standard extra");
        certificate3.setDescription("standard extra plus level gift certificate");
        certificate3.setPrice(new BigDecimal("1099.99"));
        certificate3.setDuration(120);
        certificate3.setCreateDate("2023-01-04T07:37:14.974");
        certificate3.setLastUpdateDate("2023-01-04T07:37:14.974");
        return certificate3;
    }

    private Certificate getCertificate4() {
        Certificate certificate4 = new Certificate();
        certificate4.setId(4L);
        certificate4.setName("VIP");
        certificate4.setDescription("VIP level gift certificate");
        certificate4.setPrice(new BigDecimal("1099.99"));
        certificate4.setDuration(120);
        certificate4.setCreateDate("2023-01-05T07:37:14.974");
        certificate4.setLastUpdateDate("2023-01-05T07:37:14.974");
        return certificate4;
    }

    private Certificate getCertificate5() {
        Certificate certificate5 = new Certificate();
        certificate5.setId(5L);
        certificate5.setName("base");
        certificate5.setDescription("base level gift certificate");
        certificate5.setPrice(new BigDecimal("1099.99"));
        certificate5.setDuration(120);
        certificate5.setCreateDate("2023-01-06T07:37:14.974");
        certificate5.setLastUpdateDate("2023-01-06T07:37:14.974");
        return certificate5;
    }

    private Certificate getCertificate6() {
        Certificate certificate6 = new Certificate();
        certificate6.setId(6L);
        certificate6.setName("premium");
        certificate6.setDescription("premium level gift certificate");
        certificate6.setPrice(new BigDecimal("1099.99"));
        certificate6.setDuration(120);
        certificate6.setCreateDate("2023-01-07T07:37:14.974");
        certificate6.setLastUpdateDate("2023-01-07T07:37:14.974");
        return certificate6;
    }

    private Certificate getCertificate7() {
        Certificate certificate7 = new Certificate();
        certificate7.setId(7L);
        certificate7.setName("gold");
        certificate7.setDescription("gold level gift certificate");
        certificate7.setPrice(new BigDecimal("1099.99"));
        certificate7.setDuration(120);
        certificate7.setCreateDate("2023-01-08T07:37:14.974");
        certificate7.setLastUpdateDate("2023-01-08T07:37:14.974");
        return certificate7;
    }

    private Certificate getCertificate8() {
        Certificate certificate8 = new Certificate();
        certificate8.setId(8L);
        certificate8.setName("platinum");
        certificate8.setDescription("platinum level gift certificate");
        certificate8.setPrice(new BigDecimal("1099.99"));
        certificate8.setDuration(120);
        certificate8.setCreateDate("2023-01-09T07:37:14.974");
        certificate8.setLastUpdateDate("2023-01-09T07:37:14.974");
        return certificate8;
    }

    private Certificate getCertificate9() {
        Certificate certificate9 = new Certificate();
        certificate9.setId(9L);
        certificate9.setName("New Year edition");
        certificate9.setDescription("New Year edition level gift certificate");
        certificate9.setPrice(new BigDecimal("1099.99"));
        certificate9.setDuration(120);
        certificate9.setCreateDate("2023-01-01T07:37:14.974");
        certificate9.setLastUpdateDate("2023-01-01T07:37:14.974");
        return certificate9;
    }

    private Certificate getCertificate10() {
        Certificate certificate10 = new Certificate();
        certificate10.setId(10L);
        certificate10.setName("some certificate");
        certificate10.setDescription("some certificate for some days");
        certificate10.setPrice(new BigDecimal("1099.99"));
        certificate10.setDuration(120);
        certificate10.setCreateDate("2023-01-10T07:37:14.974");
        certificate10.setLastUpdateDate("2023-01-10T07:37:14.974");
        return certificate10;
    }
}