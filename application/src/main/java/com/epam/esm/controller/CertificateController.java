package com.epam.esm.controller;

import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.service.api.CertificateService;
import com.epam.esm.service.api.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@RestController
@RequestMapping("api/v1/gift_certificates")
public class CertificateController {

    private final TagService tagService;
    private CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService, TagService tagService) {
        this.certificateService = certificateService;
        this.tagService = tagService;
    }

    private final Map<Boolean, BiFunction<List<String>, String, List<Certificate>>> getAllCertificates = Map.of(
            true, (sortParameters, query) -> certificateService.getAllCertificates(sortParameters),
            false, (sortParameters, query) -> certificateService.getAllCertificates(sortParameters, query)
    );

    @GetMapping
    public ResponseEntity<List<Certificate>> getAllCertificates(
            @RequestParam(value = "sort_by", defaultValue = "create_date:desc,name:asc") List<String> sortParams,
            @RequestParam(value = "tag_name", required = false) String tagName,
            @RequestParam(value = "search_by", defaultValue = "") String searchQuery) {
        if (tagName != null) {
            List<Certificate> giftCertificates = certificateService.getAllCertificatesByTagName(tagName);
            return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
        }
        List<Certificate> certificates = getAllCertificates.get(searchQuery.isEmpty()).apply(sortParams, searchQuery);
        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }

    @GetMapping("/{certificate_id}")
    public ResponseEntity<Certificate> getCertificateById(@PathVariable("certificate_id") Long certificateId) {
        Certificate certificate = certificateService.getCertificateById(certificateId);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }

    @GetMapping("/{certificate_id}/tags")
    public ResponseEntity<List<Tag>> getTagsByCertificateId(@PathVariable("certificate_id") Long certificateId) {
        List<Tag> tags = tagService.getTagsByCertificateId(certificateId);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Certificate> addCertificate(@RequestBody Certificate certificate) {
        Certificate addedCertificate = certificateService.addCertificate(certificate);
        return new ResponseEntity<>(addedCertificate, HttpStatus.CREATED);
    }

    @PutMapping("/{certificate_id}")
    public ResponseEntity<Certificate> updateCertificateById(
            @PathVariable("certificate_id") Long certificateId,
            @RequestBody Map<String, Object> body) {
        Certificate updatedCertificate = certificateService.updateCertificateById(certificateId, body);
        return new ResponseEntity<>(updatedCertificate, HttpStatus.OK);
    }

    @DeleteMapping("/{certificate_id}")
    public ResponseEntity<Certificate> deleteCertificateById(@PathVariable("certificate_id") Long certificateId) {
        Certificate certificate = certificateService.deleteCertificateById(certificateId);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }
}
