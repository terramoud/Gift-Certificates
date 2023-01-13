package com.epam.esm.service.api;

import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.payload.CertificateRequest;

import java.util.List;
import java.util.Map;

public interface CertificateService {

    List<Certificate> getAllCertificatesByTagId(List<String> sortParams, int limit, int offset, Long tagId);

    Certificate getCertificateById(Long certificateId);

    Certificate addCertificate(CertificateRequest certificateRequest);

    Certificate updateCertificateById(Long certificateId, Map<String, String> body);

    Certificate deleteCertificateById(Long certificateId);

    List<Certificate> getAllCertificates(List<String> sortParams, int limit, int offset);

    List<Certificate> searchAllCertificates(List<String> sortParams, int limit, int offset, String searchQuery);

    List<Certificate> getAllCertificatesByTagName(List<String> sortParams, int limit, int offset, String tagName);
}
