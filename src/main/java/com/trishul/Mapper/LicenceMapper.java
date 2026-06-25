package com.trishul.Mapper;

import com.trishul.DTO.LicenceDTO;
import com.trishul.Model.LicenceEntity;
import org.springframework.stereotype.Component;

@Component
public class LicenceMapper {

    // Convert Entity to DTO
    public LicenceDTO toDTO(LicenceEntity entity) {
        if (entity == null) {
            return null;
        }

        LicenceDTO dto = new LicenceDTO();
        dto.setLiId(entity.getLiId());
        dto.setCustomerName(entity.getCustomerName());
        dto.setFather(entity.getFather());
        dto.setLeaningLinceNo(entity.getLeaningLinceNo());
        dto.setDivingLincence(entity.getDivingLincence());
        dto.setMobNumber(entity.getMobNumber());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setDueAmount(entity.getDueAmount());
        dto.setApplydate(entity.getApplydate());
        dto.setVehicleType(entity.getVehicleType());
        dto.setPaidAmount(entity.getPaidAmount());
        dto.setLicenceType(entity.getLicenceType());

        return dto;
    }

    // Convert DTO to Entity
    public LicenceEntity toEntity(LicenceDTO dto) {
        if (dto == null) {
            return null;
        }

        LicenceEntity entity = new LicenceEntity();
        entity.setLiId(dto.getLiId());
        entity.setCustomerName(dto.getCustomerName());
        entity.setFather(dto.getFather());
        entity.setLeaningLinceNo(dto.getLeaningLinceNo());
        entity.setDivingLincence(dto.getDivingLincence());
        entity.setMobNumber(dto.getMobNumber());
        entity.setTotalAmount(dto.getTotalAmount());
        entity.setDueAmount(dto.getDueAmount());
        entity.setApplydate(dto.getApplydate());
        entity.setVehicleType(dto.getVehicleType());
        entity.setPaidAmount(dto.getPaidAmount());
        entity.setLicenceType(dto.getLicenceType());

        return entity;
    }

    // Update existing entity from DTO (for edit/update operations)
    public void updateEntityFromDTO(LicenceDTO dto, LicenceEntity entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getCustomerName() != null) {
            entity.setCustomerName(dto.getCustomerName());
        }
        if (dto.getFather() != null) {
            entity.setFather(dto.getFather());
        }
        if (dto.getLeaningLinceNo() != null) {
            entity.setLeaningLinceNo(dto.getLeaningLinceNo());
        }
        if (dto.getDivingLincence() != null) {
            entity.setDivingLincence(dto.getDivingLincence());
        }
        if (dto.getMobNumber() != null) {
            entity.setMobNumber(dto.getMobNumber());
        }
        if (dto.getTotalAmount() != null) {
            entity.setTotalAmount(dto.getTotalAmount());
        }
        if (dto.getDueAmount() != null) {
            entity.setDueAmount(dto.getDueAmount());
        }
        if (dto.getApplydate() != null) {
            entity.setApplydate(dto.getApplydate());
        }
        if (dto.getVehicleType() != null) {
            entity.setVehicleType(dto.getVehicleType());
        }
    }
}