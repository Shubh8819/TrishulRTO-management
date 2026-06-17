package com.trishul.repository;

import com.trishul.Model.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {

    List<DocumentEntity> findByVehicleTransfer_VehicleTransferId(Long vehicleTransferId);

    List<DocumentEntity> findByLicence_LiId(Long licenceId);

    void deleteByVehicleTransfer_VehicleTransferId(Long vehicleTransferId);

    void deleteByLicence_LiId(Long licenceId);
}