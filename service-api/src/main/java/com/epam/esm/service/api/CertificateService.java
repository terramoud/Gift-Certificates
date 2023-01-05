package com.epam.esm.service.api;

import com.epam.esm.domain.entity.Certificate;

import java.util.List;
import java.util.Map;

public interface CertificateService {

    List<Certificate> getAllCertificates(List<String> sortParameters);

    List<Certificate> getAllCertificates(List<String> sortParameters, String query);

    List<Certificate> getAllCertificatesByTagId(Long tagId);

    List<Certificate> getAllCertificatesByTagName(String tagName);

    Certificate getCertificateById(Long certificateId);

    Certificate addCertificate(Certificate certificate);

    Certificate updateCertificateById(Long certificateId, Map<String, Object> body);

    Certificate deleteCertificateById(Long certificateId);
}
