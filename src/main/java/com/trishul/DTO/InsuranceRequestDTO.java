package com.trishul.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceRequestDTO {

    @NotBlank(message = "Holder name is required")
    @Size(max = 100, message = "Holder name must be less than 100 characters")
    private String holderName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate holderDob;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String holderMobile;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String holderEmail;

    @NotBlank(message = "Address is required")
    private String holderAddress;

    @NotBlank(message = "Registration number is required")
    @Size(max = 20, message = "Registration number must be less than 20 characters")
    private String registrationNumber;

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;

    @NotBlank(message = "Vehicle model is required")
    private String vehicleModel;

    @NotNull(message = "Vehicle year is required")
    @Min(value = 1990, message = "Vehicle year must be 1990 or later")
    @Max(value = 2026, message = "Vehicle year must be 2026 or earlier")
    private Integer vehicleYear;

    @Size(max = 50, message = "Engine number must be less than 50 characters")
    private String engineNumber;

    @Size(max = 50, message = "Chassis number must be less than 50 characters")
    private String chassisNumber;

    @NotBlank(message = "Policy type is required")
    private String policyType;

    @NotBlank(message = "Insurance company is required")
    private String insuranceCompany;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @NotNull(message = "Premium amount is required")
    @Positive(message = "Premium amount must be positive")
    private Double premiumAmount;

    @NotNull(message = "Sum insured is required")
    @Positive(message = "Sum insured must be positive")
    private Double sumInsured;

    private List<String> addonCovers;
}