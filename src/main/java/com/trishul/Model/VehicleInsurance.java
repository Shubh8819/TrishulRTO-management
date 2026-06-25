package com.trishul.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_insurance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleInsurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Long policyId;

    // Policy Holder Details
    @Column(name = "holder_name", nullable = false, length = 100)
    private String holderName;

    @Column(name = "holder_dob", nullable = false)
    private LocalDate holderDob;

    @Column(name = "holder_mobile", nullable = false, length = 10)
    private String holderMobile;

    @Column(name = "holder_email", length = 100)
    private String holderEmail;

    @Column(name = "holder_address", nullable = false, columnDefinition = "TEXT")
    private String holderAddress;

    // Vehicle Details
    @Column(name = "registration_number", nullable = false, unique = true, length = 20)
    private String registrationNumber;

    @Column(name = "vehicle_type", nullable = false, length = 50)
    private String vehicleType;

    @Column(name = "vehicle_model", nullable = false, length = 100)
    private String vehicleModel;

    @Column(name = "vehicle_year", nullable = false)
    private Integer vehicleYear;

    @Column(name = "engine_number", length = 50)
    private String engineNumber;

    @Column(name = "chassis_number", length = 50)
    private String chassisNumber;

    // Policy Coverage
    @Column(name = "policy_type", nullable = false, length = 50)
    private String policyType;

    @Column(name = "insurance_company", nullable = false, length = 100)
    private String insuranceCompany;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "premium_amount", nullable = false)
    private Double premiumAmount;

    @Column(name = "sum_insured", nullable = false)
    private Double sumInsured;

    @Column(name = "addon_covers", columnDefinition = "TEXT")
    private String addonCovers;

    // Policy Status
    @Column(name = "policy_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PolicyStatus policyStatus = PolicyStatus.ACTIVE;

    @Column(name = "policy_number", unique = true, length = 50)
    private String policyNumber;

    // Audit fields
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @PrePersist
    public void generatePolicyNumber() {
        if (this.policyNumber == null) {
            this.policyNumber = "INS-" + LocalDate.now().getYear() + "-" +
                    String.format("%06d", (long)(Math.random() * 1000000));
        }
    }

    public enum PolicyStatus {
        ACTIVE, EXPIRED, CANCELLED, PENDING, RENEWED
    }
}