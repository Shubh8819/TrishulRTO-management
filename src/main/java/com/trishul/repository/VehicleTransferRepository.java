package com.trishul.repository;

import com.trishul.Model.VehicleTransferEntity;
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
public interface VehicleTransferRepository extends JpaRepository<VehicleTransferEntity, Long> {

    // Find by application number
    Optional<VehicleTransferEntity> findByApplicationNumber(String applicationNumber);

    // Find by vehicle registration number
    List<VehicleTransferEntity> findByVehicleRegNo(String vehicleRegNo);

    // Find by status
    List<VehicleTransferEntity> findByStatus(String status);

    // Find by transfer type
    List<VehicleTransferEntity> findByTransferType(String transferType);

    // Find by date range
    List<VehicleTransferEntity> findByTransferDateBetween(LocalDate startDate, LocalDate endDate);

    // Find by new owner mobile
    List<VehicleTransferEntity> findByNewOwnerMobile(String mobile);

    // Count by status
    long countByStatus(String status);

    // Count by transfer type
    long countByTransferType(String transferType);

    // Get total due amount
    @Query("SELECT SUM(v.dueAmount) FROM VehicleTransferEntity v WHERE v.dueAmount > 0")
    Long getTotalDueAmount();

    // Get total transfer fee
    @Query("SELECT SUM(v.transferFee) FROM VehicleTransferEntity v")
    Long getTotalTransferFee();

    // Find pending transfers
    @Query("SELECT v FROM VehicleTransferEntity v WHERE v.status = 'PENDING' OR v.status IS NULL")
    List<VehicleTransferEntity> findPendingTransfers();

    // Search by multiple fields
    @Query("SELECT v FROM VehicleTransferEntity v WHERE " +
            "LOWER(v.applicationNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(v.vehicleRegNo) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(v.currentOwner) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(v.newOwner) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(v.newOwnerMobile) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<VehicleTransferEntity> searchTransfers(@Param("search") String search, Pageable pageable);

    // Find by status with pagination
    Page<VehicleTransferEntity> findByStatus(String status, Pageable pageable);

    // Find by transfer type with pagination
    Page<VehicleTransferEntity> findByTransferType(String transferType, Pageable pageable);
}