package com.trishul.service;

import com.trishul.DTO.LicenceDTO;
import com.trishul.Mapper.LicenceMapper;
import com.trishul.Model.LicenceEntity;
import com.trishul.repository.LicenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LicenceService {

    @Autowired
    private LicenceRepository licenceRepository;

    @Autowired
    private LicenceMapper licenceMapper;

    // Insert/Create operation
    @Transactional
    public LicenceEntity saveLicence(LicenceEntity licence) {
        return licenceRepository.save(licence);
    }
    public Page<LicenceEntity> getAllLicencesPaginated(Pageable pageable) {
        return (Page<LicenceEntity>) licenceRepository.findAll(pageable);
    }

    // Read operation - Get all licences
    public List<LicenceEntity> getAllLicences() {
        return licenceRepository.findAll();
    }

    // Read operation - Get by ID
    public Optional<LicenceEntity> getLicenceById(Long id) {
        return licenceRepository.findById(id);
    }

    // Update operation
    @Transactional
    public LicenceEntity updateLicence(Long id, LicenceEntity updatedLicence) {
        Optional<LicenceEntity> existingLicence = licenceRepository.findById(id);

        if (existingLicence.isPresent()) {
            LicenceEntity licence = existingLicence.get();
            licence.setCustomerName(updatedLicence.getCustomerName());
            licence.setFather(updatedLicence.getFather());
            licence.setLeaningLinceNo(updatedLicence.getLeaningLinceNo());
            licence.setDivingLincence(updatedLicence.getDivingLincence());
            licence.setMobNumber(updatedLicence.getMobNumber());
            licence.setTotalAmount(updatedLicence.getTotalAmount());
            licence.setDueAmount(updatedLicence.getDueAmount());
            licence.setApplydate(updatedLicence.getApplydate());
            licence.setVehicleType(updatedLicence.getVehicleType());

            return licenceRepository.save(licence);
        }
        return null;
    }

    // Partial update - Update only due amount
    @Transactional
    public int updateDueAmount(Long id, Long dueAmount) {
        return licenceRepository.updateDueAmount(id, dueAmount);
    }

    // Delete operation
    @Transactional
    public boolean deleteLicence(Long id) {
        if (licenceRepository.existsById(id)) {
            licenceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Custom search methods
    public List<LicenceEntity> searchByCustomerName(String customerName) {
        return licenceRepository.findByCustomerNameContainingIgnoreCase(customerName);
    }

    public List<LicenceEntity> searchByMobileNumber(String mobNumber) {
        return licenceRepository.findByMobNumber(mobNumber);
    }

    public List<LicenceEntity> searchByVehicleType(String vehicleType) {
        return licenceRepository.findByVehicleType(vehicleType);
    }
    public double totalDueAmount(){
     return   licenceRepository.getTotalDueAmount();
    }
    public List<LicenceDTO> getLicenceWithSearch(String search,Pageable pageable){
        Page<LicenceEntity> licenceWithSearch =licenceRepository.licenceWithSearch(search, pageable);
        List<LicenceDTO> dto=   licenceWithSearch.stream().map(item->{
           return licenceMapper.toDTO(item);
        }).toList();
        return dto;
    }

}