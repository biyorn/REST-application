package com.epam.esm.controller;

import com.epam.esm.dto.BoughtCertificatePaginationDTO;
import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.CertificatePaginationDTO;
import com.epam.esm.dto.SearchParamsDTO;
import com.epam.esm.service.impl.BoughtCertificateServiceImpl;
import com.epam.esm.service.impl.CertificateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("certificates")
@CrossOrigin
public class CertificateController {

    private static final String DEFAULT_PAGE_NUM = "1";
    private static final String DEFAULT_PAGE_SIZE = "5";

    private CertificateServiceImpl certificateService;
    private BoughtCertificateServiceImpl boughtCertificateService;

    @Autowired
    public CertificateController(CertificateServiceImpl certificateService,
                                 BoughtCertificateServiceImpl boughtCertificateService) {
        this.certificateService = certificateService;
        this.boughtCertificateService = boughtCertificateService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificatePaginationDTO> getById(@PathVariable Long id) {
        return new ResponseEntity<>(certificateService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CertificatePaginationDTO> getCertificates(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUM, required = false) int pageNum,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            SearchParamsDTO searchParamsDTO) {
        return new ResponseEntity<>(certificateService.getCertificates(searchParamsDTO, pageNum, pageSize), HttpStatus.OK);
    }

    @GetMapping("/user")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<BoughtCertificatePaginationDTO> getUserCertificates(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUM, required = false) int pageNum,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(boughtCertificateService
                .getBoughtCertificatesByUserLogin(authentication.getName(), pageNum, pageSize), HttpStatus.OK);
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<CertificateDTO> addCertificate(@RequestBody CertificateDTO certificateDTO) {
        certificateDTO = certificateService.addCertificate(certificateDTO);
        String id = certificateDTO.getId().toString();
        return ResponseEntity.created(buildLocation(id)).body(certificateDTO);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<CertificateDTO> updateCertificate(@PathVariable Long id,
                                                            @RequestBody CertificateDTO certificateDTO) {
        return new ResponseEntity<>(certificateService.updateCertificate(id, certificateDTO), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<CertificateDTO> patchCertificate(@PathVariable Long id,
                                                           @RequestBody CertificateDTO certificateDTO) {
        return new ResponseEntity<>(certificateService.patchCertificate(id, certificateDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCertificate(@PathVariable Long id) {
        certificateService.deleteCertificate(id);
    }

    private URI buildLocation(String fileName) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{fileName}")
                .buildAndExpand(fileName)
                .toUri();
    }
}
