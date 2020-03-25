package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.CertificatePaginationDTO;
import com.epam.esm.dto.SearchParamsDTO;
import com.epam.esm.entity.CertificateSortEnum;
import com.epam.esm.entity.impl.Certificate;
import com.epam.esm.entity.impl.CertificatePagination;
import com.epam.esm.entity.impl.SearchParams;
import com.epam.esm.entity.impl.Tag;
import com.epam.esm.exception.FailedAddObjectException;
import com.epam.esm.exception.FailedUpdateObjectException;
import com.epam.esm.exception.NotFoundObjectException;
import com.epam.esm.mapper.CommonModelMapper;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.specification.orderby.impl.certificate.factory.CertificateOrderByFactory;
import com.epam.esm.specification.predicate.impl.bought.factory.BoughtCertificatePredicateSpecificationFactory;
import com.epam.esm.specification.predicate.impl.certificate.factory.CertificatePredicateSpecificationFactory;
import com.epam.esm.specification.predicate.impl.tag.factory.TagSpecificationFactory;
import com.epam.esm.validation.CertificateDTOPatchValidator;
import com.epam.esm.validation.CertificateDtoValidator;
import com.epam.esm.validation.PageValidator;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class CertificateServiceImpl implements CertificateService {

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 1;

    private CertificateRepository certificateRepository;
    private CertificateDtoValidator certificateDtoValidator;
    private PageValidator pageValidator;
    private CertificateDTOPatchValidator certificateDTOPatchValidator;
    private CertificatePredicateSpecificationFactory certificatePredicateSpecificationFactory;
    private BoughtCertificatePredicateSpecificationFactory boughtCertificateSpecificationFactory;
    private TagRepository tagRepository;
    private TagSpecificationFactory tagSpecificationFactory;
    private CertificateOrderByFactory certificateOrderByFactory;
    private CommonModelMapper<Certificate, CertificateDTO> certificateModelMapper;
    private CommonModelMapper<SearchParams, SearchParamsDTO> searchModelMapper;
    private CommonModelMapper<CertificatePagination, CertificatePaginationDTO> certificatePaginationModelMapper;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository,
                                  CertificateDtoValidator certificateDtoValidator,
                                  PageValidator pageValidator,
                                  CertificateDTOPatchValidator certificateDTOPatchValidator,
                                  CertificatePredicateSpecificationFactory certificatePredicateSpecificationFactory,
                                  BoughtCertificatePredicateSpecificationFactory boughtCertificateSpecificationFactory,
                                  TagRepository tagRepository,
                                  TagSpecificationFactory tagSpecificationFactory,
                                  CertificateOrderByFactory certificateOrderByFactory,
                                  CommonModelMapper<Certificate, CertificateDTO> certificateModelMapper,
                                  CommonModelMapper<SearchParams, SearchParamsDTO> searchModelMapper,
                                  CommonModelMapper<CertificatePagination, CertificatePaginationDTO> certificatePaginationModelMapper) {
        this.certificateRepository = certificateRepository;
        this.certificateDtoValidator = certificateDtoValidator;
        this.pageValidator = pageValidator;
        this.certificateDTOPatchValidator = certificateDTOPatchValidator;
        this.certificatePredicateSpecificationFactory = certificatePredicateSpecificationFactory;
        this.boughtCertificateSpecificationFactory = boughtCertificateSpecificationFactory;
        this.tagRepository = tagRepository;
        this.tagSpecificationFactory = tagSpecificationFactory;
        this.certificateOrderByFactory = certificateOrderByFactory;
        this.certificateModelMapper = certificateModelMapper;
        this.searchModelMapper = searchModelMapper;
        this.certificatePaginationModelMapper = certificatePaginationModelMapper;
    }

    /**
     * This method returns a certificate by id, if it exists.
     *
     * @param id
     * @return Certificate by Id
     * @throws throw NotFoundObjectException when certificate does not exist.
     */
    @Override
    @Transactional
    public CertificatePaginationDTO getById(Long id) {
        CertificatePagination certificatePagination = certificateRepository
                .query(certificatePredicateSpecificationFactory.getCertificatePredicateSpecificationById(id),
                        certificateOrderByFactory.getCertificateOrderBySpecificationDefault(),
                        DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
        certificatePagination.getList().stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundObjectException("Not found a certificate by id - [" + id + "]"));
        return certificatePaginationModelMapper.toDto(certificatePagination);
    }

    @Override
    @Transactional
    public CertificatePaginationDTO getCertificates(SearchParamsDTO searchParamsDTO, int pageNum, int pageSize) {
        pageValidator.verifyPageNumbers(pageNum, pageSize);
        CertificatePagination certificatePagination;
        if (hasNoParams(searchParamsDTO)) {
            certificatePagination = certificateRepository
                    .query(certificatePredicateSpecificationFactory.getCertificatePredicateSpecificationGetAll(),
                            certificateOrderByFactory.getCertificateOrderBySpecificationDefault(),
                            pageNum, pageSize);
        } else {
            SearchParams searchParams = searchModelMapper.toEntity(searchParamsDTO);
            CertificateSortEnum sortEnum = checkSort(searchParams.getSort());
            certificatePagination = certificateRepository
                    .query(certificatePredicateSpecificationFactory.getCertificatePredicateSpecificationSearchByParams(searchParams),
                            certificateOrderByFactory.getCertificateOrderBySpecificationSort(sortEnum),
                            pageNum, pageSize);
        }
        return certificatePaginationModelMapper.toDto(certificatePagination);
    }

    @Override
    @Transactional
    public CertificateDTO addCertificate(CertificateDTO certificateDTO) {
        certificateDtoValidator.isValid(certificateDTO);
        try {
            Certificate certificate = certificateModelMapper.toEntity(certificateDTO);
            certificate.setName(certificate.getName().trim());
            certificate.setDescription(certificate.getDescription().trim());
            Set<Tag> tags = certificate.getTags();
            certificate.setTags(checkTags(tags));
            certificateRepository.add(certificate);
            return certificateModelMapper.toDto(certificate);
        } catch (Exception e) {
            throw new FailedAddObjectException("Failed to add certificate", e);
        }
    }

    @Override
    @Transactional
    public CertificateDTO updateCertificate(Long id, CertificateDTO certificateDTO) {
        certificateDtoValidator.isValid(certificateDTO);
        Certificate certificate = certificateModelMapper.toEntity(certificateDTO);
        certificate.setName(certificate.getName().trim());
        certificate.setDescription(certificate.getDescription().trim());
        Certificate local = certificateRepository.query(
                certificatePredicateSpecificationFactory.getCertificatePredicateSpecificationById(id),
                certificateOrderByFactory.getCertificateOrderBySpecificationDefault(),
                DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE).getList().stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundObjectException("Not found a certificate by id - [" + id + "]"));
        checkStatus(local);
        certificate.setCreationDate(local.getCreationDate());
        Set<Tag> tags = certificate.getTags();
        certificate.setTags(checkTags(tags));
        certificate = certificateRepository.update(id, certificate);
        return certificateModelMapper.toDto(certificate);
    }

    @Override
    @Transactional
    public CertificateDTO patchCertificate(Long id, CertificateDTO certificateDTO) {
        certificateDTOPatchValidator.isValid(certificateDTO);
        Certificate certificate = certificateModelMapper.toEntity(certificateDTO);
        Certificate local = certificateRepository.query(
                certificatePredicateSpecificationFactory.getCertificatePredicateSpecificationById(id),
                certificateOrderByFactory.getCertificateOrderBySpecificationDefault(),
                DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE).getList().stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundObjectException("Not found a certificate by id - [" + id + "]"));
        checkStatus(local);
        Set<Tag> tags = certificate.getTags();
        if (tags != null) {
            certificate.setTags(checkTags(tags));
        }
        certificate = certificateRepository.patch(id, certificate);
        return certificateModelMapper.toDto(certificate);
    }

    @Override
    @Transactional
    public void deleteCertificate(Long id) {
        certificateRepository
                .query(certificatePredicateSpecificationFactory.getCertificatePredicateSpecificationById(id),
                        certificateOrderByFactory.getCertificateOrderBySpecificationDefault(),
                        DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE)
                .getList()
                .stream()
                .findFirst()
                .ifPresentOrElse(value -> certificateRepository.delete(id), () -> {
                    throw new NotFoundObjectException("Not found a certificate by id - [" + id + "]");
                });
    }

    private void checkStatus(Certificate certificate) {
        if (!certificate.isActive()) {
            throw new FailedUpdateObjectException("You can't update the certificate, because it was deleted");
        }
    }

    private boolean hasNoParams(SearchParamsDTO searchParamsDTO) {
        return Objects.isNull(searchParamsDTO.getPartName())
                && Objects.isNull(searchParamsDTO.getSort())
                && (Objects.isNull(searchParamsDTO.getTagName()) || searchParamsDTO.getTagName().isEmpty());
    }

    private CertificateSortEnum checkSort(String sort) {
        if (Objects.isNull(sort) || !EnumUtils.isValidEnumIgnoreCase(CertificateSortEnum.class, sort)) {
            return CertificateSortEnum.CREATION_DATE;
        } else {
            return CertificateSortEnum.valueOf(sort.toUpperCase());
        }

    }

    private Set<Tag> checkTags(Set<Tag> tags) {
        List<Tag> existTags = tagRepository
                .query(tagSpecificationFactory.getTagSpecificationGetExistTags(tags), DEFAULT_PAGE_NUM, tags.size());
        Set<Tag> result = new HashSet<>();
        for (Tag first : tags) {
            if (contains(first, existTags)) {
                addTagInSet(first, existTags, result);
            } else {
                result.add(first);
            }
        }
        return result;
    }

    private void addTagInSet(Tag element, List<Tag> existTags, Set<Tag> result) {
        for (Tag second : existTags) {
            if (element.getTitle().equals(second.getTitle())) {
                result.add(second);
            }
        }
    }

    private boolean contains(Tag element, List<Tag> tags) {
        return tags
                .stream()
                .anyMatch(value -> value.getTitle().equals(element.getTitle()));
    }
}
