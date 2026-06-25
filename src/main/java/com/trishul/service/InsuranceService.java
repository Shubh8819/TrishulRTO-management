package com.trishul.service;

import com.trishul.DTO.DashboardStats;
import com.trishul.DTO.InsuranceResponseDTO;
import com.trishul.Model.VehicleInsurance;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface InsuranceService {

    // ========== CRUD Operations ==========

    /**
     * Save a new insurance policy
     */
    void savePolicy(VehicleInsurance insurance);

    /**
     * Update an existing insurance policy
     */
    void updatePolicy(Long id, VehicleInsurance insurance);

    /**
     * Get policy by ID
     */
    Optional<VehicleInsurance> getPolicyById(Long id);

    /**
     * Get policy by policy number
     */
    Optional<VehicleInsurance> getPolicyByNumber(String policyNumber);

    /**
     * Get all policies with pagination
     */
    List<InsuranceResponseDTO> getAllPolicies(Pageable pageable);

    /**
     * Delete policy by ID
     */
    void deletePolicy(Long id);

    // ========== Search & Filter ==========

    /**
     * Search policies across multiple fields
     */
    List<InsuranceResponseDTO> searchPolicies(String searchTerm, Pageable pageable);

    /**
     * Count search results
     */
    long countSearchResults(String searchTerm);

    /**
     * Get policies by status
     */
    List<InsuranceResponseDTO> getPoliciesByStatus(String status, Pageable pageable);

    /**
     * Get policies by vehicle type
     */
    List<InsuranceResponseDTO> getPoliciesByVehicleType(String vehicleType, Pageable pageable);

    /**
     * Get policies by insurance company
     */
    List<InsuranceResponseDTO> getPoliciesByCompany(String company, Pageable pageable);

    // ========== Statistics ==========

    /**
     * Count all policies
     */
    long countAllPolicies();

    /**
     * Count policies by status
     */
    long countByStatus(String status);

    /**
     * Get total premium amount
     */
    double getTotalPremium();

    /**
     * Get premium by status
     */
    double getPremiumByStatus(String status);

    /**
     * Get count of expiring policies
     */
    long getExpiringPoliciesCount();

    /**
     * Get recent policies
     */
    List<InsuranceResponseDTO> getRecentPolicies(int limit);

    /**
     * Get dashboard statistics
     */
    DashboardStats getDashboardStats();

    // ========== Business Operations ==========

    /**
     * Renew an expired policy
     */
    void renewPolicy(Long id);

    /**
     * Cancel an active policy
     */
    void cancelPolicy(Long id, String reason);

    /**
     * Check if vehicle is already insured
     */
    boolean isVehicleInsured(String registrationNumber);
}