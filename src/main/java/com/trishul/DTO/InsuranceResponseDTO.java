package com.trishul.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceResponseDTO {

    private Long policyId;
    private String policyNumber;
    private String holderName;
    private LocalDate holderDob;
    private String holderMobile;
    private String holderEmail;
    private String holderAddress;
    private String registrationNumber;
    private String vehicleType;
    private String vehicleModel;
    private Integer vehicleYear;
    private String engineNumber;
    private String chassisNumber;
    private String policyType;
    private String insuranceCompany;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double premiumAmount;
    private Double sumInsured;
    private List<String> addonCovers;
    private String policyStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long daysRemaining;
    private Boolean isActive;
    private String policyTerm;
}