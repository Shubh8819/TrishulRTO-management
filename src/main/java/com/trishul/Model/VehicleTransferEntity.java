package com.trishul.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicle_transfer")
public class VehicleTransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private Long vehicleTransferId;

    @Column(name = "transfer_type", nullable = false)
    private String transferType;

    @Column(name = "transfer_date")
    private LocalDate transferDate;

    @Column(name = "application_number", nullable = false, unique = true)
    private String applicationNumber;

    @Column(name = "vehicle_reg_no", nullable = false)
    private String vehicleRegNo;

    @Column(name = "vehicle_type", nullable = false)
    private String vehicleType;

    @Column(name = "vehicle_model")
    private String vehicleModel;

    @Column(name = "chassis_number")
    private String chassisNumber;

    @Column(name = "engine_number")
    private String engineNumber;

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "current_owner", nullable = false)
    private String currentOwner;

    @Column(name = "new_owner", nullable = false)
    private String newOwner;

    @Column(name = "new_owner_mobile", nullable = false)
    private String newOwnerMobile;

    @Column(name = "new_owner_email")
    private String newOwnerEmail;

    @Column(name = "new_owner_address", nullable = false, length = 500)
    private String newOwnerAddress;

    @Column(name = "transfer_fee")
    private Long transferFee;

    @Column(name = "due_amount")
    private Long dueAmount;

    @Column(name = "remarks", length = 1000)
    private String remarks;

    @Column(name = "status")
    private String status;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    // ===== RELATIONSHIP WITH DOCUMENTS =====
    /*@OneToMany(mappedBy = "vehicleTransfer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<DocumentEntity> documents = new ArrayList<>();
*/
    // Constructors
    public VehicleTransferEntity() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    public VehicleTransferEntity(Long vehicleTransferId, String transferType, LocalDate transferDate,
                                 String applicationNumber, String vehicleRegNo, String vehicleType,
                                 String vehicleModel, String chassisNumber, String engineNumber,
                                 String fuelType, String currentOwner, String newOwner,
                                 String newOwnerMobile, String newOwnerEmail, String newOwnerAddress,
                                 Long transferFee, Long dueAmount, String remarks, String status) {
        this.vehicleTransferId = vehicleTransferId;
        this.transferType = transferType;
        this.transferDate = transferDate;
        this.applicationNumber = applicationNumber;
        this.vehicleRegNo = vehicleRegNo;
        this.vehicleType = vehicleType;
        this.vehicleModel = vehicleModel;
        this.chassisNumber = chassisNumber;
        this.engineNumber = engineNumber;
        this.fuelType = fuelType;
        this.currentOwner = currentOwner;
        this.newOwner = newOwner;
        this.newOwnerMobile = newOwnerMobile;
        this.newOwnerEmail = newOwnerEmail;
        this.newOwnerAddress = newOwnerAddress;
        this.transferFee = transferFee;
        this.dueAmount = dueAmount;
        this.remarks = remarks;
        this.status = status;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getVehicleTransferId() { return vehicleTransferId; }
    public void setVehicleTransferId(Long vehicleTransferId) { this.vehicleTransferId = vehicleTransferId; }

    public String getTransferType() { return transferType; }
    public void setTransferType(String transferType) { this.transferType = transferType; }

    public LocalDate getTransferDate() { return transferDate; }
    public void setTransferDate(LocalDate transferDate) { this.transferDate = transferDate; }

    public String getApplicationNumber() { return applicationNumber; }
    public void setApplicationNumber(String applicationNumber) { this.applicationNumber = applicationNumber; }

    public String getVehicleRegNo() { return vehicleRegNo; }
    public void setVehicleRegNo(String vehicleRegNo) { this.vehicleRegNo = vehicleRegNo; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getVehicleModel() { return vehicleModel; }
    public void setVehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; }

    public String getChassisNumber() { return chassisNumber; }
    public void setChassisNumber(String chassisNumber) { this.chassisNumber = chassisNumber; }

    public String getEngineNumber() { return engineNumber; }
    public void setEngineNumber(String engineNumber) { this.engineNumber = engineNumber; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public String getCurrentOwner() { return currentOwner; }
    public void setCurrentOwner(String currentOwner) { this.currentOwner = currentOwner; }

    public String getNewOwner() { return newOwner; }
    public void setNewOwner(String newOwner) { this.newOwner = newOwner; }

    public String getNewOwnerMobile() { return newOwnerMobile; }
    public void setNewOwnerMobile(String newOwnerMobile) { this.newOwnerMobile = newOwnerMobile; }

    public String getNewOwnerEmail() { return newOwnerEmail; }
    public void setNewOwnerEmail(String newOwnerEmail) { this.newOwnerEmail = newOwnerEmail; }

    public String getNewOwnerAddress() { return newOwnerAddress; }
    public void setNewOwnerAddress(String newOwnerAddress) { this.newOwnerAddress = newOwnerAddress; }

    public Long getTransferFee() { return transferFee; }
    public void setTransferFee(Long transferFee) { this.transferFee = transferFee; }

    public Long getDueAmount() { return dueAmount; }
    public void setDueAmount(Long dueAmount) { this.dueAmount = dueAmount; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }

    /*public List<DocumentEntity> getDocuments() { return documents; }
    public void setDocuments(List<DocumentEntity> documents) { this.documents = documents; }*/

    // Helper Methods
   /* public void addDocument(DocumentEntity document) {
        documents.add(document);
        document.setVehicleTransfer(this);
    }*/

 /*   public void removeDocument(DocumentEntity document) {
        documents.remove(document);
        document.setVehicleTransfer(null);
    }
*/
    public Long getPaidAmount() {
        if (transferFee != null && dueAmount != null) {
            return transferFee - dueAmount;
        }
        return 0L;
    }

    public boolean isPaymentComplete() {
        return dueAmount != null && dueAmount == 0;
    }

    public String getPaymentStatus() {
        if (dueAmount == null) return "Pending";
        if (dueAmount == 0) return "Paid";
        if (dueAmount > 0 && transferFee != null && dueAmount < transferFee) return "Partial";
        return "Pending";
    }
}