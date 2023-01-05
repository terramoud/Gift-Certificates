package com.epam.esm.repository.api;

import com.epam.esm.domain.entity.Certificate;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository {
    List<Certificate> getAllCertificates(List<String> sortParameters);

    List<Certificate> getAllCertificates(List<String> sortParameters, String query);

    List<Certificate> getAllCertificatesByTagId(Long tagId);

    List<Certificate> getAllCertificatesByTagName(String tagName);

    Optional<Certificate> getCertificateById(Long certificateId);

    Certificate addCertificate(Certificate certificate);

    Certificate updateCertificateById(Long certificateId, Certificate certificate);

    boolean deleteCertificateById(Long certificateId);
}
