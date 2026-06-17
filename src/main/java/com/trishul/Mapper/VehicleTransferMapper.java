package com.trishul.Mapper;

import com.trishul.DTO.DocumentDTO;
import com.trishul.DTO.VehicleTransferDTO;
import com.trishul.Model.DocumentEntity;
import com.trishul.Model.VehicleTransferEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class VehicleTransferMapper {

    // Entity to DTO
    public VehicleTransferDTO toDTO(VehicleTransferEntity entity) {
        if (entity == null) {
            return null;
        }

        VehicleTransferDTO dto = new VehicleTransferDTO();
        dto.setVehicleTransferId(entity.getVehicleTransferId());
        dto.setTransferType(entity.getTransferType());
        dto.setTransferDate(entity.getTransferDate());
        dto.setApplicationNumber(entity.getApplicationNumber());
        dto.setVehicleRegNo(entity.getVehicleRegNo());
        dto.setVehicleType(entity.getVehicleType());
        dto.setVehicleModel(entity.getVehicleModel());
        dto.setChassisNumber(entity.getChassisNumber());
        dto.setEngineNumber(entity.getEngineNumber());
        dto.setFuelType(entity.getFuelType());
        dto.setCurrentOwner(entity.getCurrentOwner());
        dto.setNewOwner(entity.getNewOwner());
        dto.setNewOwnerMobile(entity.getNewOwnerMobile());
        dto.setNewOwnerEmail(entity.getNewOwnerEmail());
        dto.setNewOwnerAddress(entity.getNewOwnerAddress());
        dto.setTransferFee(entity.getTransferFee());
        dto.setDueAmount(entity.getDueAmount());
        dto.setRemarks(entity.getRemarks());
        dto.setStatus(entity.getStatus());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());

        // Map documents
        /*if (entity.getDocuments() != null && !entity.getDocuments().isEmpty()) {
            dto.setDocuments(entity.getDocuments().stream()
                    .map(this::toDocumentDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setDocuments(new ArrayList<>());
        }*/

        return dto;
    }

    // DTO to Entity
    public VehicleTransferEntity toEntity(VehicleTransferDTO dto) {
        if (dto == null) {
            return null;
        }

        VehicleTransferEntity entity = new VehicleTransferEntity();
        entity.setVehicleTransferId(dto.getVehicleTransferId());
        entity.setTransferType(dto.getTransferType());
        entity.setTransferDate(dto.getTransferDate());
        entity.setApplicationNumber(dto.getApplicationNumber());
        entity.setVehicleRegNo(dto.getVehicleRegNo());
        entity.setVehicleType(dto.getVehicleType());
        entity.setVehicleModel(dto.getVehicleModel());
        entity.setChassisNumber(dto.getChassisNumber());
        entity.setEngineNumber(dto.getEngineNumber());
        entity.setFuelType(dto.getFuelType());
        entity.setCurrentOwner(dto.getCurrentOwner());
        entity.setNewOwner(dto.getNewOwner());
        entity.setNewOwnerMobile(dto.getNewOwnerMobile());
        entity.setNewOwnerEmail(dto.getNewOwnerEmail());
        entity.setNewOwnerAddress(dto.getNewOwnerAddress());
        entity.setTransferFee(dto.getTransferFee());
        entity.setDueAmount(dto.getDueAmount());
        entity.setRemarks(dto.getRemarks());
        entity.setStatus(dto.getStatus());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setUpdatedDate(dto.getUpdatedDate());

        return entity;
    }

    // Update entity from DTO
    public void updateEntityFromDTO(VehicleTransferDTO dto, VehicleTransferEntity entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getTransferType() != null) {
            entity.setTransferType(dto.getTransferType());
        }
        if (dto.getTransferDate() != null) {
            entity.setTransferDate(dto.getTransferDate());
        }
        if (dto.getApplicationNumber() != null) {
            entity.setApplicationNumber(dto.getApplicationNumber());
        }
        if (dto.getVehicleRegNo() != null) {
            entity.setVehicleRegNo(dto.getVehicleRegNo());
        }
        if (dto.getVehicleType() != null) {
            entity.setVehicleType(dto.getVehicleType());
        }
        if (dto.getVehicleModel() != null) {
            entity.setVehicleModel(dto.getVehicleModel());
        }
        if (dto.getChassisNumber() != null) {
            entity.setChassisNumber(dto.getChassisNumber());
        }
        if (dto.getEngineNumber() != null) {
            entity.setEngineNumber(dto.getEngineNumber());
        }
        if (dto.getFuelType() != null) {
            entity.setFuelType(dto.getFuelType());
        }
        if (dto.getCurrentOwner() != null) {
            entity.setCurrentOwner(dto.getCurrentOwner());
        }
        if (dto.getNewOwner() != null) {
            entity.setNewOwner(dto.getNewOwner());
        }
        if (dto.getNewOwnerMobile() != null) {
            entity.setNewOwnerMobile(dto.getNewOwnerMobile());
        }
        if (dto.getNewOwnerEmail() != null) {
            entity.setNewOwnerEmail(dto.getNewOwnerEmail());
        }
        if (dto.getNewOwnerAddress() != null) {
            entity.setNewOwnerAddress(dto.getNewOwnerAddress());
        }
        if (dto.getTransferFee() != null) {
            entity.setTransferFee(dto.getTransferFee());
        }
        if (dto.getDueAmount() != null) {
            entity.setDueAmount(dto.getDueAmount());
        }
        if (dto.getRemarks() != null) {
            entity.setRemarks(dto.getRemarks());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        entity.setUpdatedDate(LocalDateTime.now());
    }

    // Document Entity to DTO
    public DocumentDTO toDocumentDTO(DocumentEntity entity) {
        if (entity == null) {
            return null;
        }

        DocumentDTO dto = new DocumentDTO();
        dto.setId(entity.getId());
        dto.setFileName(entity.getFileName());
        dto.setOriginalFileName(entity.getOriginalFileName());
        dto.setFilePath(entity.getFilePath());
        dto.setFileSize(entity.getFileSize());
        dto.setFileType(entity.getFileType());
        dto.setDocumentType(entity.getDocumentType());
        dto.setUploadDate(entity.getUploadDate());

        if (entity.getVehicleTransfer() != null) {
            dto.setVehicleTransferId(entity.getVehicleTransfer().getVehicleTransferId());
        }
        if (entity.getLicence() != null) {
            dto.setLicenceId(entity.getLicence().getLiId());
        }

        return dto;
    }

    // Document DTO to Entity
    public DocumentEntity toDocumentEntity(DocumentDTO dto) {
        if (dto == null) {
            return null;
        }

        DocumentEntity entity = new DocumentEntity();
        entity.setId(dto.getId());
        entity.setFileName(dto.getFileName());
        entity.setOriginalFileName(dto.getOriginalFileName());
        entity.setFilePath(dto.getFilePath());
        entity.setFileSize(dto.getFileSize());
        entity.setFileType(dto.getFileType());
        entity.setDocumentType(dto.getDocumentType());
        entity.setUploadDate(dto.getUploadDate());

        return entity;
    }
}