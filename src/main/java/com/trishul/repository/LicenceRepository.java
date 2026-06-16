package com.trishul.repository;

import com.trishul.Model.LicenceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LicenceRepository extends JpaRepository<LicenceEntity, Long> {

    // Custom finder methods
    List<LicenceEntity> findByCustomerNameContainingIgnoreCase(String customerName);
    List<LicenceEntity> findByMobNumber(String mobNumber);
    List<LicenceEntity> findByVehicleType(String vehicleType);

    // Custom update query
    @Modifying
    @Transactional
    @Query("UPDATE LicenceEntity l SET l.dueAmount = :dueAmount WHERE l.liId = :id")
    int updateDueAmount(@Param("id") Long id, @Param("dueAmount") Long dueAmount);

    // Custom delete query
    @Modifying
    @Transactional
    @Query("DELETE FROM LicenceEntity l WHERE l.liId = :id")
    int deleteLicenceById(@Param("id") Long id);

    @Query("SELECT COALESCE(SUM(l.dueAmount), 0) FROM LicenceEntity l")
    Double getTotalDueAmount();
    @Query("""
    SELECT l
    FROM LicenceEntity l
    WHERE (:search IS NULL OR :search = '')
       OR LOWER(l.customerName) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(l.leaningLinceNo) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(l.divingLincence) LIKE LOWER(CONCAT('%', :search, '%'))
       OR l.mobNumber LIKE CONCAT('%', :search, '%')
""")
    Page<LicenceEntity> licenceWithSearch(
            @Param("search") String search,
            Pageable pageable);
}