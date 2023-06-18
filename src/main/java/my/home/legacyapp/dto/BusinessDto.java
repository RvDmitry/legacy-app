package my.home.legacyapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import my.home.legacyapp.entity.BusinessType;

import java.time.LocalDateTime;

@Data
public class BusinessDto {

    private Long id;

    private BusinessType type;

    private String businessValue;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}
