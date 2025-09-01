package com.insurance.policy.web.mapper;

import com.insurance.policy.domain.Policy;
import com.insurance.policy.web.dto.PolicyDto;
import com.insurance.policy.web.dto.PolicyResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PolicyMapper {

    // Create mapping: DTO -> Entity (for POST)
    @Mapping(target = "id", ignore = true)
    Policy toEntity(PolicyDto dto);

    // Entity -> Response DTO
    PolicyResponse toResponse(Policy policy);

    // Update mapping: merge DTO into existing entity (for PUT/PATCH later)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PolicyDto dto, @MappingTarget Policy entity);
}
