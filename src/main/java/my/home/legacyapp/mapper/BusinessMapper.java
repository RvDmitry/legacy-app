package my.home.legacyapp.mapper;

import my.home.legacyapp.dto.BusinessDto;
import my.home.legacyapp.entity.BusinessEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BusinessMapper {

    BusinessDto fromEntityToDto(BusinessEntity entity);

    List<BusinessDto> fromEntityToDto(List<BusinessEntity> entities);
}
