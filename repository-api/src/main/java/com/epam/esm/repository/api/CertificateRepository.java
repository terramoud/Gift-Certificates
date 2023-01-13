package com.epam.esm.repository.api;

import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.payload.CertificateRequest;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository {
    List<Certificate> getAllCertificates(String sortParams, int limit, int offset);

    List<Certificate> getAllCertificatesByTagId(String sortParams, int limit, int offset, Long tagId);

    List<Certificate> getAllCertificatesByTagName(String sortParams, int limit, int offset, String tagName);

    List<Certificate> searchAllCertificates(String sortParams, int limit, int offset, String searchQuery);

    Optional<Certificate> getCertificateById(Long certificateId);

    Certificate addCertificate(Certificate certificate, Long tagId);

    Certificate updateCertificateById(Long certificateId, CertificateRequest certificateRequest);

    boolean deleteCertificateById(Long certificateId);
}
