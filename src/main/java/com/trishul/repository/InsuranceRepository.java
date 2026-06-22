package com.trishul.repository;

import com.trishul.Model.VehicleInsurance;
import com.trishul.Model.VehicleInsurance.PolicyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InsuranceRepository extends JpaRepository<VehicleInsurance, Long> {

    // Find by policy number
    Optional<VehicleInsurance> findByPolicyNumber(String policyNumber);

    // Find by registration number
    Optional<VehicleInsurance> findByRegistrationNumber(String registrationNumber);

    // Find by holder mobile number
    List<VehicleInsurance> findByHolderMobile(String holderMobile);

    // Find by policy status with pagination
    Page<VehicleInsurance> findByPolicyStatus(PolicyStatus status, Pageable pageable);

    // Count by policy status
    long countByPolicyStatus(PolicyStatus status);

    // Find expiring policies
    @Query("SELECT v FROM VehicleInsurance v WHERE v.endDate BETWEEN :today AND :expiryDate AND v.policyStatus = 'ACTIVE'")
    List<VehicleInsurance> findPoliciesExpiringSoon(@Param("today") LocalDate today,
                                                    @Param("expiryDate") LocalDate expiryDate);

    // Count expiring policies
    @Query("SELECT COUNT(v) FROM VehicleInsurance v WHERE v.endDate BETWEEN :today AND :expiryDate AND v.policyStatus = 'ACTIVE'")
    long countPoliciesExpiringSoon(@Param("today") LocalDate today,
                                   @Param("expiryDate") LocalDate expiryDate);

    // Search across multiple fields
    @Query("SELECT v FROM VehicleInsurance v WHERE " +
            "LOWER(v.holderName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(v.registrationNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(v.policyNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(v.holderMobile) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(v.vehicleModel) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<VehicleInsurance> searchAllFields(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Count search results
    @Query("SELECT COUNT(v) FROM VehicleInsurance v WHERE " +
            "LOWER(v.holderName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(v.registrationNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(v.policyNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(v.holderMobile) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(v.vehicleModel) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    long countSearchResults(@Param("searchTerm") String searchTerm);

    // Get total premium sum
    @Query("SELECT COALESCE(SUM(v.premiumAmount), 0) FROM VehicleInsurance v")
    double getTotalPremium();

    // Get premium sum by status
    @Query("SELECT COALESCE(SUM(v.premiumAmount), 0) FROM VehicleInsurance v WHERE v.policyStatus = :status")
    double getPremiumByStatus(@Param("status") PolicyStatus status);

    // Get recent policies
    @Query("SELECT v FROM VehicleInsurance v ORDER BY v.createdAt DESC")
    List<VehicleInsurance> findRecentPolicies(Pageable pageable);

    // Find by vehicle registration number (for duplicate check)
    boolean existsByRegistrationNumber(String registrationNumber);

    // Find by policy number (for duplicate check)
    boolean existsByPolicyNumber(String policyNumber);

    // Get policies by date range
    @Query("SELECT v FROM VehicleInsurance v WHERE v.startDate BETWEEN :startDate AND :endDate")
    List<VehicleInsurance> findPoliciesByDateRange(@Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

    // Get active policies count
    @Query("SELECT COUNT(v) FROM VehicleInsurance v WHERE v.policyStatus = 'ACTIVE' AND v.endDate > :today")
    long countActivePolicies(@Param("today") LocalDate today);

    // Get expired policies count
    @Query("SELECT COUNT(v) FROM VehicleInsurance v WHERE v.policyStatus = 'EXPIRED' OR v.endDate < :today")
    long countExpiredPolicies(@Param("today") LocalDate today);

    // Get policies by vehicle type
    Page<VehicleInsurance> findByVehicleType(String vehicleType, Pageable pageable);

    // Get policies by insurance company
    Page<VehicleInsurance> findByInsuranceCompany(String insuranceCompany, Pageable pageable);
}