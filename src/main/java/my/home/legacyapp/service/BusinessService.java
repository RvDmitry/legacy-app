package my.home.legacyapp.service;

import my.home.legacyapp.dto.BusinessDto;
import my.home.legacyapp.entity.BusinessType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Set;

public interface BusinessService {

    BusinessDto getById(Long id);

    Page<BusinessDto> pageByCreatedDate(LocalDate from, LocalDate to, Pageable pageable);

    Page<BusinessDto> pageByType(Set<BusinessType> types, Pageable pageable);

    BusinessDto save(BusinessDto dto);

    BusinessDto update(BusinessDto dto);

    void delete(Long id);
}
