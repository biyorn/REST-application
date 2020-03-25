package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.CertificatePaginationDTO;
import com.epam.esm.dto.SearchParamsDTO;

public interface CertificateService {

    CertificatePaginationDTO getById(Long id);

    CertificatePaginationDTO getCertificates(SearchParamsDTO searchParamsDTO, int pageNum, int pageSize);

    CertificateDTO addCertificate(CertificateDTO certificateDTO);

    CertificateDTO updateCertificate(Long id, CertificateDTO certificateDTO);

    CertificateDTO patchCertificate(Long id, CertificateDTO certificateDTO);

    void deleteCertificate(Long id);
}
