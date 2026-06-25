package com.trishul.ServiceImpl;

import com.trishul.Model.VehicleTransferEntity;
import com.trishul.repository.VehicleTransferRepository;
import com.trishul.service.VehicleTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VehicleTransferServiceImpl implements VehicleTransferService {

    @Autowired
    private VehicleTransferRepository vehicleTransferRepository;

    @Override
    public VehicleTransferEntity saveTransfer(VehicleTransferEntity transfer) {
        if (transfer.getCreatedDate() == null) {
            transfer.setCreatedDate(LocalDateTime.now());
        }
        if (transfer.getStatus() == null) {
            transfer.setStatus("PENDING");
        }
        transfer.setUpdatedDate(LocalDateTime.now());
        return vehicleTransferRepository.save(transfer);
    }

    @Override
    public List<VehicleTransferEntity> getAllTransfers() {
        return vehicleTransferRepository.findAll();
    }

    @Override
    public Page<VehicleTransferEntity> getAllTransfersPaginated(Pageable pageable) {
        return vehicleTransferRepository.findAll(pageable);
    }

    @Override
    public VehicleTransferEntity getTransferById(Long id) {
        return vehicleTransferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle transfer not found with id: " + id));
    }

    @Override
    public Optional<VehicleTransferEntity> getTransferByApplicationNumber(String applicationNumber) {
        return vehicleTransferRepository.findByApplicationNumber(applicationNumber);
    }

    @Override
    public VehicleTransferEntity updateTransfer(VehicleTransferEntity transfer) {
        transfer.setUpdatedDate(LocalDateTime.now());
        return vehicleTransferRepository.save(transfer);
    }

    @Override
    public void deleteTransfer(Long id) {
        vehicleTransferRepository.deleteById(id);
    }

    @Override
    public List<VehicleTransferEntity> getTransfersByStatus(String status) {
        return vehicleTransferRepository.findByStatus(status);
    }

    @Override
    public List<VehicleTransferEntity> getTransfersByType(String transferType) {
        return vehicleTransferRepository.findByTransferType(transferType);
    }

    @Override
    public List<VehicleTransferEntity> getTransfersByDateRange(LocalDate startDate, LocalDate endDate) {
        return vehicleTransferRepository.findByTransferDateBetween(startDate, endDate);
    }

    @Override
    public List<VehicleTransferEntity> getTransfersByVehicleNumber(String vehicleRegNo) {
        return vehicleTransferRepository.findByVehicleRegNo(vehicleRegNo);
    }

    @Override
    public List<VehicleTransferEntity> getTransfersByNewOwnerMobile(String mobile) {
        return vehicleTransferRepository.findByNewOwnerMobile(mobile);
    }

    @Override
    public Page<VehicleTransferEntity> searchTransfers(String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return vehicleTransferRepository.findAll(pageable);
        }
        return vehicleTransferRepository.searchTransfers(search.trim(), pageable);
    }

    @Override
    public long getTotalTransfersCount() {
        return vehicleTransferRepository.count();
    }

    @Override
    public long getPendingTransfersCount() {
        return vehicleTransferRepository.countByStatus("PENDING");
    }

    @Override
    public long getCompletedTransfersCount() {
        return vehicleTransferRepository.countByStatus("COMPLETED");
    }

    @Override
    public Long getTotalDueAmount() {
        Long total = vehicleTransferRepository.getTotalDueAmount();
        return total != null ? total : 0L;
    }

    @Override
    public Long getTotalTransferFee() {
        Long total = vehicleTransferRepository.getTotalTransferFee();
        return total != null ? total : 0L;
    }

    @Override
    public long getCountByTransferType(String transferType) {
        return vehicleTransferRepository.countByTransferType(transferType);
    }

    @Override
    public VehicleTransferEntity updateTransferStatus(Long id, String status) {
        VehicleTransferEntity transfer = getTransferById(id);
        transfer.setStatus(status);
        transfer.setUpdatedDate(LocalDateTime.from(LocalDate.now()));
        return vehicleTransferRepository.save(transfer);
    }

    @Override
    public List<VehicleTransferEntity> getPendingTransfers() {
        return vehicleTransferRepository.findPendingTransfers();
    }
}