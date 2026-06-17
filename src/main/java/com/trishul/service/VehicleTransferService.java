package com.trishul.service;

import com.trishul.Model.VehicleTransferEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VehicleTransferService {

    // Save transfer
    VehicleTransferEntity saveTransfer(VehicleTransferEntity transfer);

    // Get all transfers
    List<VehicleTransferEntity> getAllTransfers();

    // Get paginated transfers
    Page<VehicleTransferEntity> getAllTransfersPaginated(Pageable pageable);

    // Get transfer by ID
    VehicleTransferEntity getTransferById(Long id);

    // Get transfer by application number
    Optional<VehicleTransferEntity> getTransferByApplicationNumber(String applicationNumber);

    // Update transfer
    VehicleTransferEntity updateTransfer(VehicleTransferEntity transfer);

    // Delete transfer
    void deleteTransfer(Long id);

    // Get transfers by status
    List<VehicleTransferEntity> getTransfersByStatus(String status);

    // Get transfers by transfer type
    List<VehicleTransferEntity> getTransfersByType(String transferType);

    // Get transfers by date range
    List<VehicleTransferEntity> getTransfersByDateRange(LocalDate startDate, LocalDate endDate);

    // Get transfers by vehicle number
    List<VehicleTransferEntity> getTransfersByVehicleNumber(String vehicleRegNo);

    // Get transfers by new owner mobile
    List<VehicleTransferEntity> getTransfersByNewOwnerMobile(String mobile);

    // Search transfers
    Page<VehicleTransferEntity> searchTransfers(String search, Pageable pageable);

    // Get statistics
    long getTotalTransfersCount();
    long getPendingTransfersCount();
    long getCompletedTransfersCount();
    Long getTotalDueAmount();
    Long getTotalTransferFee();
    long getCountByTransferType(String transferType);

    // Update status
    VehicleTransferEntity updateTransferStatus(Long id, String status);

    // Get pending transfers
    List<VehicleTransferEntity> getPendingTransfers();
}