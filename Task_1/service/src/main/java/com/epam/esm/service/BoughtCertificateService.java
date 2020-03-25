package com.epam.esm.service;

import com.epam.esm.dto.BoughtCertificatePaginationDTO;

public interface BoughtCertificateService {

    BoughtCertificatePaginationDTO getBoughtCertificatesByUserLogin(String login, int pageNum, int pageSize);
}
