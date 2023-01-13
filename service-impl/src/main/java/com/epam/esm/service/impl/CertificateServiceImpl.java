package com.epam.esm.service.impl;

import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.mapper.CertificateMapper;
import com.epam.esm.domain.entity.mapper.RequestParametersMapper;
import com.epam.esm.domain.payload.CertificateRequest;
import com.epam.esm.exceptions.*;
import com.epam.esm.repository.api.CertificateRepository;
import com.epam.esm.repository.api.TagRepository;
import com.epam.esm.service.api.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CertificateServiceImpl implements CertificateService {

    private static final String CERTIFICATE_NOT_FOUND = "certificate.not.found";
    private static final String CERTIFICATE_ERROR_INVALID_ID = "certificate.error.invalid.id";
    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final RequestParametersMapper parametersMapper;
    private final CertificateMapper certificateMapper;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository,
                                  TagRepository tagRepository,
                                  RequestParametersMapper parametersMapper,
                                  CertificateMapper certificateMapper) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.parametersMapper = parametersMapper;
        this.certificateMapper = certificateMapper;
    }

    @Override
    public List<Certificate> getAllCertificates(List<String> sortParams, int limit, int offset) {
        if (limit < 0) throw new InvalidPaginationParameterException(
                "limit", limit + "", ErrorCodes.INVALID_PAGINATION_PARAMETER);
        if (offset < 0) throw new InvalidPaginationParameterException(
                "offset", offset + "", ErrorCodes.INVALID_PAGINATION_PARAMETER);
        String sortParameters = parametersMapper.mapSortParameters(sortParams);
        return certificateRepository.getAllCertificates(sortParameters, limit, offset);
    }

    @Override
    public List<Certificate> searchAllCertificates(List<String> sortParams,
                                                   int limit,
                                                   int offset,
                                                   String searchQuery) {
        if (limit < 0) throw new InvalidPaginationParameterException(
                "limit", limit + "", ErrorCodes.INVALID_PAGINATION_PARAMETER);
        if (offset < 0) throw new InvalidPaginationParameterException(
                "offset", offset + "", ErrorCodes.INVALID_PAGINATION_PARAMETER);
        String sortParameters = parametersMapper.mapSortParameters(sortParams);
        return certificateRepository.searchAllCertificates(sortParameters, limit, offset, searchQuery);
    }

    @Override
    public List<Certificate> getAllCertificatesByTagName(List<String> sortParams,
                                                         int limit,
                                                         int offset,
                                                         String tagName) {
        if (tagName == null || tagName.isEmpty() || tagName.equals("null"))
            throw new InvalidResourceNameException(
                    "tag.error.invalid.name", tagName, ErrorCodes.INVALID_TAG_NAME_PROPERTY);
        if (limit < 0) throw new InvalidPaginationParameterException(
                "limit", limit + "", ErrorCodes.INVALID_PAGINATION_PARAMETER);
        if (offset < 0) throw new InvalidPaginationParameterException(
                "offset", offset + "", ErrorCodes.INVALID_PAGINATION_PARAMETER);
        String sortParameters = parametersMapper.mapSortParameters(sortParams);
        return certificateRepository.getAllCertificatesByTagName(sortParameters, limit, offset, tagName);
    }

    @Override
    public List<Certificate> getAllCertificatesByTagId(List<String> sortParams,
                                                       int limit,
                                                       int offset,
                                                       Long tagId) {
        if (tagId == null || tagId < 1) throw new InvalidResourcePropertyException(
                "tag.error.invalid.id", tagId, ErrorCodes.INVALID_TAG_ID_PROPERTY);
        if (limit < 0) throw new InvalidPaginationParameterException(
                "limit", limit + "", ErrorCodes.INVALID_PAGINATION_PARAMETER);
        if (offset < 0) throw new InvalidPaginationParameterException(
                "offset", offset + "", ErrorCodes.INVALID_PAGINATION_PARAMETER);
        String sortParameters = parametersMapper.mapSortParameters(sortParams);
        return certificateRepository.getAllCertificatesByTagId(sortParameters, limit, offset, tagId);
    }

    @Override
    public Certificate getCertificateById(Long certificateId) {
        if (certificateId == null || certificateId < 1) throw new InvalidResourcePropertyException(
                CERTIFICATE_ERROR_INVALID_ID, certificateId, ErrorCodes.INVALID_CERTIFICATE_ID_PROPERTY);
        return certificateRepository.getCertificateById(certificateId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CERTIFICATE_NOT_FOUND, certificateId, ErrorCodes.NOT_FOUND_CERTIFICATE_RESOURCE));
    }

    @Override
    @Transactional
    public Certificate addCertificate(CertificateRequest certificateRequest) {
        Certificate certificate = certificateMapper.mapToCertificate(certificateRequest);
        String name = certificate.getName();
        String description = certificate.getDescription();
        BigDecimal price = certificate.getPrice();
        Integer duration = certificate.getDuration();
        Long tagId = certificateRequest.getTagId();
        if (tagId == null || tagId < 1) throw new InvalidResourcePropertyException(
                "tag.error.invalid.id", tagId, ErrorCodes.INVALID_TAG_ID_PROPERTY);
        tagRepository.getTagById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "tag.not.found", tagId, ErrorCodes.NOT_FOUND_TAG_RESOURCE));
        if (name == null || name.isEmpty() || name.equals("null"))
            throw new InvalidResourceNameException(
                    "certificate.error.invalid.name", name, ErrorCodes.INVALID_CERTIFICATE_PROPERTY);
        if (description == null || description.isEmpty() || description.equals("null"))
            throw new InvalidResourcePropertyException(
                    "certificate.error.invalid.description", description, ErrorCodes.INVALID_CERTIFICATE_PROPERTY);
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidResourcePropertyException(
                    "certificate.error.invalid.price", price + "", ErrorCodes.INVALID_CERTIFICATE_PROPERTY);
        if (duration == null || duration < 1)
            throw new InvalidResourcePropertyException(
                    "certificate.error.invalid.duration", duration + "", ErrorCodes.INVALID_CERTIFICATE_PROPERTY);
        certificate.setCreateDate(LocalDateTime.now());
        certificate.setLastUpdateDate(certificate.getCreateDate());
        return certificateRepository.addCertificate(certificate, tagId);
    }

    @Override
    @Transactional
    public Certificate updateCertificateById(Long certificateId, Map<String, String> body) {
        if (certificateId == null || certificateId < 1)
            throw new InvalidResourcePropertyException(
            CERTIFICATE_ERROR_INVALID_ID, certificateId, ErrorCodes.INVALID_CERTIFICATE_ID_PROPERTY);
        Long bodyCertificateId = (body.containsKey("id")) ? Long.parseLong((String) body.get("id")) : null;
        if (bodyCertificateId == null || bodyCertificateId < 1 || !bodyCertificateId.equals(certificateId))
            throw new InvalidResourcePropertyException(
                    "certificate.error.paramId.no.equals.bodyId",
                    bodyCertificateId,
                    ErrorCodes.INVALID_CERTIFICATE_ID_PROPERTY);
        Certificate certificateToUpdate = certificateRepository.getCertificateById(certificateId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CERTIFICATE_NOT_FOUND, certificateId, ErrorCodes.NOT_FOUND_CERTIFICATE_RESOURCE));
        if (body.containsKey("name")) certificateToUpdate.setName((String) body.get("name"));
        if (body.containsKey("description")) certificateToUpdate.setDescription((String) body.get("description"));
        if (body.containsKey("price")) certificateToUpdate.setPrice( new BigDecimal(body.get("price")));
        if (body.containsKey("duration"))
            certificateToUpdate.setDuration(Integer.parseInt((String) body.get("duration")));
        certificateToUpdate.setLastUpdateDate(LocalDateTime.now());
        String name = certificateToUpdate.getName();
        String description = certificateToUpdate.getDescription();
        BigDecimal price = certificateToUpdate.getPrice();
        Integer duration = certificateToUpdate.getDuration();
        if (name == null || name.isEmpty() || name.equals("null"))
            throw new InvalidResourceNameException(
                    "certificate.error.invalid.name", name, ErrorCodes.INVALID_CERTIFICATE_PROPERTY);
        if (description == null || description.isEmpty() || description.equals("null"))
            throw new InvalidResourcePropertyException(
                    "certificate.error.invalid.description", description, ErrorCodes.INVALID_CERTIFICATE_PROPERTY);
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidResourcePropertyException(
                    "certificate.error.invalid.price", price + "", ErrorCodes.INVALID_CERTIFICATE_PROPERTY);
        if (duration == null || duration < 1)
            throw new InvalidResourcePropertyException(
                    "certificate.error.invalid.duration", duration + "", ErrorCodes.INVALID_CERTIFICATE_PROPERTY);
        Long tagId = (body.containsKey("tagId")) ? Long.parseLong((String) body.get("tagId")) : null;
        if (tagId == null || tagId < 1) throw new InvalidResourcePropertyException(
                "tag.error.invalid.id", tagId, ErrorCodes.INVALID_TAG_ID_PROPERTY);
        CertificateRequest certificateRequest = certificateMapper.mapToRequest(certificateToUpdate, tagId);
        return certificateRepository.updateCertificateById(certificateId, certificateRequest);
    }

    @Override
    public Certificate deleteCertificateById(Long certificateId) {
        if (certificateId == null || certificateId < 1) throw new InvalidResourcePropertyException(
                CERTIFICATE_ERROR_INVALID_ID, certificateId, ErrorCodes.INVALID_CERTIFICATE_ID_PROPERTY);
        ResourceNotFoundException resourceNotFoundException = new ResourceNotFoundException(
                CERTIFICATE_NOT_FOUND, certificateId, ErrorCodes.NOT_FOUND_CERTIFICATE_RESOURCE);
        Certificate deletedCertificate = certificateRepository.getCertificateById(certificateId)
                .orElseThrow(() -> resourceNotFoundException);
        if (!certificateRepository.deleteCertificateById(certificateId)) throw resourceNotFoundException;
        return deletedCertificate;
    }
}
