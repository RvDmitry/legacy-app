package my.home.legacyapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.home.legacyapp.dto.Action;
import my.home.legacyapp.dto.BusinessDto;
import my.home.legacyapp.dto.MessageDto;
import my.home.legacyapp.entity.BusinessEntity;
import my.home.legacyapp.entity.BusinessType;
import my.home.legacyapp.exception.BusinessNotFoundException;
import my.home.legacyapp.mapper.BusinessMapper;
import my.home.legacyapp.repository.BusinessRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private static final String LOG_ERROR = "Sending a message to Kafka failed";
    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;
    private final SendMessageService sendMessageService;

    @Override
    public BusinessDto getById(Long id) {
        return businessMapper.fromEntityToDto(findById(id));
    }

    @Override
    public Page<BusinessDto> pageByCreatedDate(LocalDate from, LocalDate to, Pageable pageable) {
        var entityPage = businessRepository.findByCreatedAtBetween(from.atStartOfDay(), to.atTime(LocalTime.MAX), pageable);
        return entityPage.map(businessMapper::fromEntityToDto);
    }

    @Override
    public Page<BusinessDto> pageByType(Set<BusinessType> types, Pageable pageable) {
        var entityPage = businessRepository.findByTypeIn(types, pageable);
        return entityPage.map(businessMapper::fromEntityToDto);
    }

    @Transactional
    @Override
    public BusinessDto save(BusinessDto dto) {
        var entity = new BusinessEntity();
        entity.setType(dto.type());
        entity.setBusinessValue(dto.businessValue());
        businessRepository.save(entity);
        sendMessage(entity.getId(), Action.CREATE);
        return businessMapper.fromEntityToDto(entity);
    }

    @Transactional
    @Override
    public BusinessDto update(BusinessDto dto) {
        var entity = findById(dto.id());
        entity.setType(dto.type());
        entity.setBusinessValue(dto.businessValue());
        entity.setUpdatedAt(LocalDateTime.now());
        businessRepository.save(entity);
        sendMessage(entity.getId(), Action.UPDATE);
        return businessMapper.fromEntityToDto(entity);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        findById(id);
        businessRepository.deleteById(id);
        sendMessage(id, Action.DELETE);
    }

    private BusinessEntity findById(Long id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new BusinessNotFoundException(String.format("Value with id[%d] not found", id)));
    }

    private void sendMessage(long id, Action action) {
        try {
            sendMessageService.sendMessage(new MessageDto(id, action, System.currentTimeMillis() / 1000L));
        } catch (Exception e) {
            log.error(LOG_ERROR, e);
        }
    }
}
