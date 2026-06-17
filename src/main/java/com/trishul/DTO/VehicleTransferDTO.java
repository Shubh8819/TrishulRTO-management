package com.trishul.DTO;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VehicleTransferDTO {

    private Long vehicleTransferId;

    @NotBlank(message = "Transfer type is required")
    private String transferType;

    private LocalDate transferDate;

    @NotBlank(message = "Application number is required")
    @Size(min = 5, max = 50, message = "Application number must be between 5 and 50 characters")
    private String applicationNumber;

    @NotBlank(message = "Vehicle registration number is required")
    @Size(min = 4, max = 20, message = "Vehicle registration number must be between 4 and 20 characters")
    private String vehicleRegNo;

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;

    private String vehicleModel;

    private String chassisNumber;

    private String engineNumber;

    private String fuelType;

    @NotBlank(message = "Current owner name is required")
    @Size(min = 2, max = 100, message = "Current owner name must be between 2 and 100 characters")
    private String currentOwner;

    @NotBlank(message = "New owner name is required")
    @Size(min = 2, max = 100, message = "New owner name must be between 2 and 100 characters")
    private String newOwner;

    @NotBlank(message = "New owner mobile is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be exactly 10 digits")
    private String newOwnerMobile;

    @Email(message = "Invalid email format")
    private String newOwnerEmail;

    @NotBlank(message = "New owner address is required")
    @Size(min = 5, max = 500, message = "Address must be between 5 and 500 characters")
    private String newOwnerAddress;

    @PositiveOrZero(message = "Transfer fee cannot be negative")
    private Long transferFee;

    @PositiveOrZero(message = "Due amount cannot be negative")
    private Long dueAmount;

    private String remarks;

    private String status;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    // Documents are handled separately in controller
    // This field is just for displaying existing documents
   // private List<DocumentDTO> documents = new ArrayList<>();

    // Constructors
    public VehicleTransferDTO() {}

    public VehicleTransferDTO(Long vehicleTransferId, String transferType, LocalDate transferDate,
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

    /*public List<DocumentDTO> getDocuments() { return documents; }
    public void setDocuments(List<DocumentDTO> documents) { this.documents = documents; }
*/
    // Helper Methods
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