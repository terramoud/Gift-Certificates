package com.epam.esm.controller;

import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.payload.CertificateRequest;
import com.epam.esm.service.api.CertificateService;
import com.epam.esm.service.api.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/gift-certificates")
public class CertificateController {

    private final TagService tagService;
    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService, TagService tagService) {
        this.certificateService = certificateService;
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<Certificate>> getAllCertificates(
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            @RequestParam(value = "start", defaultValue = "0") int offset,
            @RequestParam(value = "sort-by", defaultValue = "create_date:desc,name:asc") List<String> sortParams,
            @RequestParam(value = "tag-name", required = false) String tagName,
            @RequestParam(value = "search-by", required = false) String searchQuery) {
        if (limit < 1) limit = 1;
        if (offset < 0) offset = 0;
        List<Certificate> certificates;
        if (searchQuery != null) {
            certificates = certificateService.searchAllCertificates(sortParams, limit, offset, searchQuery);
            return new ResponseEntity<>(certificates, HttpStatus.OK);
        }
        if (tagName != null) {
            certificates = certificateService.getAllCertificatesByTagName(sortParams, limit, offset, tagName);
            return new ResponseEntity<>(certificates, HttpStatus.OK);
        }
        certificates = certificateService.getAllCertificates(sortParams, limit, offset);
        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }

    @GetMapping("/{certificate-id}/tags")
    public ResponseEntity<List<Tag>> getTagsByCertificateId(
            @PathVariable("certificate-id") Long certificateId,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            @RequestParam(value = "start", defaultValue = "0") int offset,
            @RequestParam(value = "sort-by", defaultValue = "create_date:desc,name:asc") List<String> sortParams) {
        List<Tag> tags = tagService.getTagsByCertificateId(sortParams, limit, offset, certificateId);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/{certificate-id}")
    public ResponseEntity<Certificate> getCertificateById(@PathVariable("certificate-id") Long certificateId) {
        Certificate certificate = certificateService.getCertificateById(certificateId);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Certificate> addCertificate(@RequestBody CertificateRequest certificateRequest) {
        Certificate addedCertificate = certificateService.addCertificate(certificateRequest);

        return new ResponseEntity<>(addedCertificate, HttpStatus.CREATED);
    }

    @PutMapping("/{certificate-id}")
    public ResponseEntity<Certificate> updateCertificateById(
            @PathVariable("certificate-id") Long certificateId,
            @RequestBody Map<String, String> body) {
        Certificate updatedCertificate = certificateService.updateCertificateById(certificateId, body);
        return new ResponseEntity<>(updatedCertificate, HttpStatus.OK);
    }

    @DeleteMapping("/{certificate-id}")
    public ResponseEntity<Certificate> deleteCertificateById(@PathVariable("certificate-id") Long certificateId) {
        Certificate certificate = certificateService.deleteCertificateById(certificateId);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }
}
