package com.epam.esm.service.impl;

import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.exceptions.ErrorCodes;
import com.epam.esm.exceptions.InvalidResourceNameException;
import com.epam.esm.exceptions.InvalidResourcePropertyException;
import com.epam.esm.exceptions.ResourceNotFoundException;
import com.epam.esm.repository.api.CertificateRepository;
import com.epam.esm.service.api.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CertificateServiceImpl implements CertificateService {

    private static final String CERTIFICATE_NOT_FOUND = "certificate.not.found";
    private static final String CERTIFICATE_ERROR_INVALID_ID = "certificate.error.invalid.id";
    private final CertificateRepository certificateRepository;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @Override
    public List<Certificate> getAllCertificates(List<String> sortParameters) {
        return certificateRepository.getAllCertificates(sortParameters);
    }

    @Override
    public List<Certificate> getAllCertificates(List<String> sortParameters, String query) {
        return certificateRepository.getAllCertificates(sortParameters, query);
    }

    @Override
    public List<Certificate> getAllCertificatesByTagId(Long tagId) {
        if (tagId == null || tagId < 1 ) throw new InvalidResourcePropertyException(
                "tag.error.invalid.id", tagId, ErrorCodes.INVALID_TAG_ID_PROPERTY);
        return certificateRepository.getAllCertificatesByTagId(tagId);
    }

    @Override
    public List<Certificate> getAllCertificatesByTagName(String tagName) {
        if (tagName == null || tagName.isEmpty() || tagName.equals("null"))
            throw new InvalidResourceNameException(
                "tag.error.invalid.name", tagName, ErrorCodes.INVALID_TAG_NAME_PROPERTY);
        return certificateRepository.getAllCertificatesByTagName(tagName);
    }

    @Override
    public Certificate getCertificateById(Long certificateId) {
        if (certificateId == null || certificateId < 1 ) throw new InvalidResourcePropertyException(
                CERTIFICATE_ERROR_INVALID_ID, certificateId, ErrorCodes.INVALID_CERTIFICATE_ID_PROPERTY);
        return certificateRepository.getCertificateById(certificateId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CERTIFICATE_NOT_FOUND, certificateId, ErrorCodes.NOT_FOUND_CERTIFICATE_RESOURCE));
    }

    @Override
    public Certificate addCertificate(Certificate certificate) {
        Long id = certificate.getId();
        String name = certificate.getName();
        String description = certificate.getDescription();
        BigDecimal price = certificate.getPrice();
        Integer duration = certificate.getDuration();
        if (name == null || name.isEmpty() || name.equals("null"))
            throw new InvalidResourcePropertyException(
                    "certificate.error.invalid.name", id, ErrorCodes.INVALID_CERTIFICATE_PROPERTY);
        if (description == null || description.isEmpty() || description.equals("null"))
            throw new InvalidResourcePropertyException(
                    "certificate.error.invalid.description", id, ErrorCodes.INVALID_CERTIFICATE_PROPERTY);
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidResourcePropertyException(
                    "certificate.error.invalid.price", id, ErrorCodes.INVALID_CERTIFICATE_PROPERTY);
        if (duration == null || duration < 1)
            throw new InvalidResourcePropertyException(
                    "certificate.error.invalid.duration", id, ErrorCodes.INVALID_CERTIFICATE_PROPERTY);
        certificate.setCreateDate(LocalDateTime.now());
        certificate.setLastUpdateDate(certificate.getCreateDate());
        return certificateRepository.addCertificate(certificate);
    }

    @Override
    public Certificate updateCertificateById(Long certificateId, Map<String, Object> body) {
        if (certificateId == null || certificateId < 1 ) throw new InvalidResourcePropertyException(
                CERTIFICATE_ERROR_INVALID_ID, certificateId, ErrorCodes.INVALID_CERTIFICATE_ID_PROPERTY);
        Certificate certificateToUpdate = certificateRepository.getCertificateById(certificateId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CERTIFICATE_NOT_FOUND, certificateId, ErrorCodes.NOT_FOUND_CERTIFICATE_RESOURCE));
        if (body.containsKey("name")) certificateToUpdate.setName((String) body.get("name"));
        if (body.containsKey("description")) certificateToUpdate.setDescription((String) body.get("description"));
        if (body.containsKey("price")) certificateToUpdate.setPrice((BigDecimal) body.get("price"));
        if (body.containsKey("duration")) certificateToUpdate.setDuration((Integer) body.get("duration"));
        String name = certificateToUpdate.getName();
        String description = certificateToUpdate.getDescription();
        BigDecimal price = certificateToUpdate.getPrice();
        Integer duration = certificateToUpdate.getDuration();
        if (name == null || name.isEmpty() || name.equals("null"))
            throw new InvalidResourcePropertyException(
                    "certificate.error.invalid.name", certificateId, ErrorCodes.INVALID_CERTIFICATE_PROPERTY);
        if (description == null || description.isEmpty() || description.equals("null"))
            throw new InvalidResourcePropertyException(
                    "certificate.error.invalid.description", certificateId, ErrorCodes.INVALID_CERTIFICATE_PROPERTY);
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidResourcePropertyException(
                    "certificate.error.invalid.price", certificateId, ErrorCodes.INVALID_CERTIFICATE_PROPERTY);
        if (duration == null || duration < 1)
            throw new InvalidResourcePropertyException(
                    "certificate.error.invalid.duration", certificateId, ErrorCodes.INVALID_CERTIFICATE_PROPERTY);
        certificateToUpdate.setLastUpdateDate(LocalDateTime.now());
        return certificateRepository.updateCertificateById(certificateId, certificateToUpdate);
    }

    @Override
    public Certificate deleteCertificateById(Long certificateId) {
        if (certificateId == null || certificateId < 1 ) throw new InvalidResourcePropertyException(
                CERTIFICATE_ERROR_INVALID_ID, certificateId, ErrorCodes.INVALID_CERTIFICATE_ID_PROPERTY);
        ResourceNotFoundException resourceNotFoundException = new ResourceNotFoundException(
            CERTIFICATE_NOT_FOUND, certificateId, ErrorCodes.NOT_FOUND_CERTIFICATE_RESOURCE);
        Certificate deletedCertificate = certificateRepository.getCertificateById(certificateId)
                .orElseThrow(() -> resourceNotFoundException);
        if (!certificateRepository.deleteCertificateById(certificateId)) throw resourceNotFoundException;
        return deletedCertificate;
    }
}
