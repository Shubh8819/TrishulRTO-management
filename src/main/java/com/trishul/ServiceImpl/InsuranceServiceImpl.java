package com.trishul.service.impl;

import com.trishul.DTO.InsuranceResponseDTO;
import com.trishul.DTO.DashboardStats;
import com.trishul.Mapper.InsuranceMapper;
import com.trishul.Model.VehicleInsurance;
import com.trishul.Model.VehicleInsurance.PolicyStatus;
import com.trishul.repository.InsuranceRepository;
import com.trishul.service.InsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InsuranceServiceImpl implements InsuranceService {

    @Autowired
    private InsuranceRepository insuranceRepository;

    @Autowired
    private InsuranceMapper insuranceMapper;

    // ========== CRUD Operations ==========

    @Override
    public void savePolicy(VehicleInsurance insurance) {
        // Generate policy number if not set
        if (insurance.getPolicyNumber() == null) {
            insurance.setPolicyNumber(generatePolicyNumber());
        }

        // Set initial status
        insurance.setPolicyStatus(determinePolicyStatus(insurance.getStartDate(), insurance.getEndDate()));

        insuranceRepository.save(insurance);
    }

    @Override
    public void updatePolicy(Long id, VehicleInsurance insurance) {
        VehicleInsurance existingInsurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insurance policy not found with ID: " + id));

        // Update fields
        existingInsurance.setHolderName(insurance.getHolderName());
        existingInsurance.setHolderDob(insurance.getHolderDob());
        existingInsurance.setHolderMobile(insurance.getHolderMobile());
        existingInsurance.setHolderEmail(insurance.getHolderEmail());
        existingInsurance.setHolderAddress(insurance.getHolderAddress());
        existingInsurance.setRegistrationNumber(insurance.getRegistrationNumber());
        existingInsurance.setVehicleType(insurance.getVehicleType());
        existingInsurance.setVehicleModel(insurance.getVehicleModel());
        existingInsurance.setVehicleYear(insurance.getVehicleYear());
        existingInsurance.setEngineNumber(insurance.getEngineNumber());
        existingInsurance.setChassisNumber(insurance.getChassisNumber());
        existingInsurance.setPolicyType(insurance.getPolicyType());
        existingInsurance.setInsuranceCompany(insurance.getInsuranceCompany());
        existingInsurance.setStartDate(insurance.getStartDate());
        existingInsurance.setEndDate(insurance.getEndDate());
        existingInsurance.setPremiumAmount(insurance.getPremiumAmount());
        existingInsurance.setSumInsured(insurance.getSumInsured());
        existingInsurance.setAddonCovers(insurance.getAddonCovers());

        // Update status
        existingInsurance.setPolicyStatus(determinePolicyStatus(insurance.getStartDate(), insurance.getEndDate()));

        insuranceRepository.save(existingInsurance);
    }

    @Override
    public Optional<VehicleInsurance> getPolicyById(Long id) {
        return insuranceRepository.findById(id);
    }

    @Override
    public Optional<VehicleInsurance> getPolicyByNumber(String policyNumber) {
        return insuranceRepository.findByPolicyNumber(policyNumber);
    }

    @Override
    public List<InsuranceResponseDTO> getAllPolicies(Pageable pageable) {
        Page<VehicleInsurance> policies = insuranceRepository.findAll(pageable);
        return policies.stream()
                .map(insuranceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePolicy(Long id) {
        VehicleInsurance insurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insurance policy not found with ID: " + id));

        // Only allow deletion of pending or expired policies
        if (insurance.getPolicyStatus() == PolicyStatus.ACTIVE) {
            throw new RuntimeException("Cannot delete an active policy. Please cancel it first.");
        }

        insuranceRepository.deleteById(id);
    }

    // ========== Search & Filter ==========

    @Override
    public List<InsuranceResponseDTO> searchPolicies(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            Page<VehicleInsurance> policies = insuranceRepository.findAll(pageable);
            return policies.stream()
                    .map(insuranceMapper::toResponseDTO)
                    .collect(Collectors.toList());
        }

        Page<VehicleInsurance> policies = insuranceRepository.searchAllFields(searchTerm.trim(), pageable);
        return policies.stream()
                .map(insuranceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long countSearchResults(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return insuranceRepository.count();
        }
        return insuranceRepository.countSearchResults(searchTerm.trim());
    }

    @Override
    public List<InsuranceResponseDTO> getPoliciesByStatus(String status, Pageable pageable) {
        try {
            PolicyStatus policyStatus = PolicyStatus.valueOf(status.toUpperCase());
            Page<VehicleInsurance> policies = insuranceRepository.findByPolicyStatus(policyStatus, pageable);
            return policies.stream()
                    .map(insuranceMapper::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    @Override
    public List<InsuranceResponseDTO> getPoliciesByVehicleType(String vehicleType, Pageable pageable) {
        Page<VehicleInsurance> policies = insuranceRepository.findByVehicleType(vehicleType, pageable);
        return policies.stream()
                .map(insuranceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InsuranceResponseDTO> getPoliciesByCompany(String company, Pageable pageable) {
        Page<VehicleInsurance> policies = insuranceRepository.findByInsuranceCompany(company, pageable);
        return policies.stream()
                .map(insuranceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ========== Statistics ==========

    @Override
    public long countAllPolicies() {
        return insuranceRepository.count();
    }

    @Override
    public long countByStatus(String status) {
        try {
            PolicyStatus policyStatus = PolicyStatus.valueOf(status.toUpperCase());
            return insuranceRepository.countByPolicyStatus(policyStatus);
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }

    @Override
    public double getTotalPremium() {
        return insuranceRepository.getTotalPremium();
    }

    @Override
    public double getPremiumByStatus(String status) {
        try {
            PolicyStatus policyStatus = PolicyStatus.valueOf(status.toUpperCase());
            return insuranceRepository.getPremiumByStatus(policyStatus);
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }

    @Override
    public long getExpiringPoliciesCount() {
        LocalDate today = LocalDate.now();
        LocalDate expiryDate = today.plusDays(30);
        return insuranceRepository.countPoliciesExpiringSoon(today, expiryDate);
    }

    @Override
    public List<InsuranceResponseDTO> getRecentPolicies(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<VehicleInsurance> policies = insuranceRepository.findRecentPolicies(pageable);
        return policies.stream()
                .map(insuranceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DashboardStats getDashboardStats() {
        LocalDate today = LocalDate.now();

        long total = insuranceRepository.count();
        long active = insuranceRepository.countActivePolicies(today);
        long expired = insuranceRepository.countExpiredPolicies(today);
        long pending = insuranceRepository.countByPolicyStatus(PolicyStatus.PENDING);
        long cancelled = insuranceRepository.countByPolicyStatus(PolicyStatus.CANCELLED);
        long expiring = getExpiringPoliciesCount();

        double totalPremium = insuranceRepository.getTotalPremium();
        double activePremium = insuranceRepository.getPremiumByStatus(PolicyStatus.ACTIVE);

        return DashboardStats.builder()
                .totalPolicies(total)
                .activePolicies(active)
                .expiredPolicies(expired)
                .pendingPolicies(pending)
                .cancelledPolicies(cancelled)
                .expiringPolicies(expiring)
                .totalPremium(totalPremium)
                .activePremium(activePremium)
                .build();
    }

    // ========== Business Operations ==========

    @Override
    @Transactional
    public void renewPolicy(Long id) {
        VehicleInsurance existingInsurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insurance policy not found with ID: " + id));

        // Check if policy can be renewed
        if (existingInsurance.getPolicyStatus() == PolicyStatus.CANCELLED) {
            throw new RuntimeException("Cancelled policies cannot be renewed");
        }

        // Create new policy from existing data
        VehicleInsurance newPolicy = new VehicleInsurance();
        newPolicy.setHolderName(existingInsurance.getHolderName());
        newPolicy.setHolderDob(existingInsurance.getHolderDob());
        newPolicy.setHolderMobile(existingInsurance.getHolderMobile());
        newPolicy.setHolderEmail(existingInsurance.getHolderEmail());
        newPolicy.setHolderAddress(existingInsurance.getHolderAddress());
        newPolicy.setRegistrationNumber(existingInsurance.getRegistrationNumber());
        newPolicy.setVehicleType(existingInsurance.getVehicleType());
        newPolicy.setVehicleModel(existingInsurance.getVehicleModel());
        newPolicy.setVehicleYear(existingInsurance.getVehicleYear());
        newPolicy.setEngineNumber(existingInsurance.getEngineNumber());
        newPolicy.setChassisNumber(existingInsurance.getChassisNumber());
        newPolicy.setPolicyType(existingInsurance.getPolicyType());
        newPolicy.setInsuranceCompany(existingInsurance.getInsuranceCompany());

        // Set new dates (1 year from now)
        LocalDate today = LocalDate.now();
        newPolicy.setStartDate(today);
        newPolicy.setEndDate(today.plusYears(1));
        newPolicy.setPremiumAmount(existingInsurance.getPremiumAmount());
        newPolicy.setSumInsured(existingInsurance.getSumInsured());
        newPolicy.setAddonCovers(existingInsurance.getAddonCovers());
        newPolicy.setPolicyStatus(PolicyStatus.ACTIVE);
        newPolicy.setPolicyNumber(generatePolicyNumber());

        insuranceRepository.save(newPolicy);

        // Mark old policy as renewed
        existingInsurance.setPolicyStatus(PolicyStatus.RENEWED);
        insuranceRepository.save(existingInsurance);
    }

    @Override
    @Transactional
    public void cancelPolicy(Long id, String reason) {
        VehicleInsurance insurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insurance policy not found with ID: " + id));

        if (insurance.getPolicyStatus() == PolicyStatus.CANCELLED) {
            throw new RuntimeException("Policy is already cancelled");
        }

        if (insurance.getPolicyStatus() == PolicyStatus.EXPIRED) {
            throw new RuntimeException("Expired policies cannot be cancelled");
        }

        insurance.setPolicyStatus(PolicyStatus.CANCELLED);
        insuranceRepository.save(insurance);
    }

    @Override
    public boolean isVehicleInsured(String registrationNumber) {
        Optional<VehicleInsurance> existing = insuranceRepository.findByRegistrationNumber(registrationNumber);
        if (existing.isEmpty()) {
            return false;
        }
        VehicleInsurance insurance = existing.get();
        return insurance.getPolicyStatus() == PolicyStatus.ACTIVE &&
                insurance.getEndDate().isAfter(LocalDate.now());
    }

    // ========== Private Helper Methods ==========

    private String generatePolicyNumber() {
        return "INS-" + LocalDate.now().getYear() + "-" +
                String.format("%06d", (long) (Math.random() * 1000000));
    }

    private PolicyStatus determinePolicyStatus(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return PolicyStatus.PENDING;
        }

        LocalDate today = LocalDate.now();

        if (today.isBefore(startDate)) {
            return PolicyStatus.PENDING;
        } else if (today.isAfter(endDate)) {
            return PolicyStatus.EXPIRED;
        } else {
            return PolicyStatus.ACTIVE;
        }
    }
}