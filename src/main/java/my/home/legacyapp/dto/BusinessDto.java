package my.home.legacyapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import my.home.legacyapp.entity.BusinessType;

import java.time.LocalDateTime;

public record BusinessDto(
    Long id,
    BusinessType type,
    String businessValue,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    LocalDateTime createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    LocalDateTime updatedAt
    ) {
    public BusinessDto(BusinessType type, String businessValue) {
        this(null, type, businessValue, LocalDateTime.now(), LocalDateTime.now());
    }

    public BusinessDto(Long id, BusinessType type, String businessValue) {
        this(id, type, businessValue, LocalDateTime.now(), LocalDateTime.now());
    }
}
