package com.trishul.Mapper;

import com.trishul.DTO.InsuranceRequestDTO;
import com.trishul.DTO.InsuranceResponseDTO;
import com.trishul.Model.VehicleInsurance;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class InsuranceMapper {

    public VehicleInsurance toEntity(InsuranceRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        VehicleInsurance insurance = new VehicleInsurance();
        insurance.setHolderName(dto.getHolderName());
        insurance.setHolderDob(dto.getHolderDob());
        insurance.setHolderMobile(dto.getHolderMobile());
        insurance.setHolderEmail(dto.getHolderEmail());
        insurance.setHolderAddress(dto.getHolderAddress());
        insurance.setRegistrationNumber(dto.getRegistrationNumber());
        insurance.setVehicleType(dto.getVehicleType());
        insurance.setVehicleModel(dto.getVehicleModel());
        insurance.setVehicleYear(dto.getVehicleYear());
        insurance.setEngineNumber(dto.getEngineNumber());
        insurance.setChassisNumber(dto.getChassisNumber());
        insurance.setPolicyType(dto.getPolicyType());
        insurance.setInsuranceCompany(dto.getInsuranceCompany());
        insurance.setStartDate(dto.getStartDate());
        insurance.setEndDate(dto.getEndDate());
        insurance.setPremiumAmount(dto.getPremiumAmount());
        insurance.setSumInsured(dto.getSumInsured());

        // Convert list to comma-separated string
        if (dto.getAddonCovers() != null && !dto.getAddonCovers().isEmpty()) {
            insurance.setAddonCovers(String.join(",", dto.getAddonCovers()));
        }

        return insurance;
    }

    public InsuranceRequestDTO toDTO(VehicleInsurance entity) {
        if (entity == null) {
            return null;
        }

        InsuranceRequestDTO dto = new InsuranceRequestDTO();
        dto.setHolderName(entity.getHolderName());
        dto.setHolderDob(entity.getHolderDob());
        dto.setHolderMobile(entity.getHolderMobile());
        dto.setHolderEmail(entity.getHolderEmail());
        dto.setHolderAddress(entity.getHolderAddress());
        dto.setRegistrationNumber(entity.getRegistrationNumber());
        dto.setVehicleType(entity.getVehicleType());
        dto.setVehicleModel(entity.getVehicleModel());
        dto.setVehicleYear(entity.getVehicleYear());
        dto.setEngineNumber(entity.getEngineNumber());
        dto.setChassisNumber(entity.getChassisNumber());
        dto.setPolicyType(entity.getPolicyType());
        dto.setInsuranceCompany(entity.getInsuranceCompany());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setPremiumAmount(entity.getPremiumAmount());
        dto.setSumInsured(entity.getSumInsured());

        // Convert comma-separated string to list
        if (entity.getAddonCovers() != null && !entity.getAddonCovers().isEmpty()) {
            dto.setAddonCovers(Arrays.asList(entity.getAddonCovers().split(",")));
        }

        return dto;
    }

    public InsuranceResponseDTO toResponseDTO(VehicleInsurance entity) {
        if (entity == null) {
            return null;
        }

        LocalDate today = LocalDate.now();
        long daysRemaining = 0;
        boolean isActive = false;
        String policyTerm = "N/A";

        if (entity.getEndDate() != null) {
            daysRemaining = ChronoUnit.DAYS.between(today, entity.getEndDate());
            isActive = daysRemaining > 0 && entity.getPolicyStatus() == VehicleInsurance.PolicyStatus.ACTIVE;
            policyTerm = daysRemaining > 0 ? daysRemaining + " days remaining" : "Expired";
        }

        // Convert comma-separated string to list
        List<String> addonList = new ArrayList<>();
        if (entity.getAddonCovers() != null && !entity.getAddonCovers().isEmpty()) {
            addonList = Arrays.asList(entity.getAddonCovers().split(","));
        }

        return InsuranceResponseDTO.builder()
                .policyId(entity.getPolicyId())
                .policyNumber(entity.getPolicyNumber())
                .holderName(entity.getHolderName())
                .holderDob(entity.getHolderDob())
                .holderMobile(entity.getHolderMobile())
                .holderEmail(entity.getHolderEmail())
                .holderAddress(entity.getHolderAddress())
                .registrationNumber(entity.getRegistrationNumber())
                .vehicleType(entity.getVehicleType())
                .vehicleModel(entity.getVehicleModel())
                .vehicleYear(entity.getVehicleYear())
                .engineNumber(entity.getEngineNumber())
                .chassisNumber(entity.getChassisNumber())
                .policyType(entity.getPolicyType())
                .insuranceCompany(entity.getInsuranceCompany())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .premiumAmount(entity.getPremiumAmount())
                .sumInsured(entity.getSumInsured())
                .addonCovers(addonList)
                .policyStatus(entity.getPolicyStatus() != null ? entity.getPolicyStatus().name() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .daysRemaining(daysRemaining)
                .isActive(isActive)
                .policyTerm(policyTerm)
                .build();
    }
}