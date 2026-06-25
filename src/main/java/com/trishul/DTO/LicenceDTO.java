package com.trishul.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

public class LicenceDTO {

    private Long liId;

    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
   // @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Customer name should contain only letters and spaces")
    private String customerName;

    @NotBlank(message = "Father's name is required")
    @Size(min = 2, max = 100, message = "Father's name must be between 2 and 100 characters")
   // @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Father's name should contain only letters and spaces")
    private String father;

    //@NotBlank(message = "Learning licence number is required")
   // @Size(min = 5, max = 50, message = "Learning licence number must be between 5 and 50 characters")
    private String leaningLinceNo;

    @Size(max = 50, message = "Driving licence number cannot exceed 50 characters")
    private String divingLincence;

    @NotBlank(message = "Mobile number is required")
   // @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be exactly 10 digits")
    private String mobNumber;

    @PositiveOrZero(message = "Total amount cannot be negative")
    private Long totalAmount;

    @PositiveOrZero(message = "Due amount cannot be negative")
    private Long dueAmount;

    @PositiveOrZero(message = "paidAmount amount cannot be negative")
    private Long paidAmount;



   // @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format (use YYYY-MM-DD)")
    private String applydate;

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;

    private Long daysOld;

    // Add this field
    private Integer licenceType;

    public Integer getLicenceType() {
        return licenceType;
    }

    public void setLicenceType(Integer licenceType) {
        this.licenceType = licenceType;
    }

    // Add getter and setter for daysOld
    public Long getDaysOld() {
        return daysOld;
    }

    public void setDaysOld(Long daysOld) {
        this.daysOld = daysOld;
    }

    // Constructors
    public LicenceDTO() {
    }

    public LicenceDTO(Long liId, String customerName, String father, String leaningLinceNo,
                      String divingLincence, String mobNumber, Long totalAmount,
                      Long dueAmount, String applydate, String vehicleType) {
        this.liId = liId;
        this.customerName = customerName;
        this.father = father;
        this.leaningLinceNo = leaningLinceNo;
        this.divingLincence = divingLincence;
        this.mobNumber = mobNumber;
        this.totalAmount = totalAmount;
        this.dueAmount = dueAmount;
        this.applydate = applydate;
        this.vehicleType = vehicleType;
    }

    // Getters and Setters
    public Long getLiId() {
        return liId;
    }

    public void setLiId(Long liId) {
        this.liId = liId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getLeaningLinceNo() {
        return leaningLinceNo;
    }

    public void setLeaningLinceNo(String leaningLinceNo) {
        this.leaningLinceNo = leaningLinceNo;
    }

    public String getDivingLincence() {
        return divingLincence;
    }

    public void setDivingLincence(String divingLincence) {
        this.divingLincence = divingLincence;
    }

    public String getMobNumber() {
        return mobNumber;
    }

    public void setMobNumber(String mobNumber) {
        this.mobNumber = mobNumber;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Long dueAmount) {
        this.dueAmount = dueAmount;
    }

    public String getApplydate() {
        return applydate;
    }

    public void setApplydate(String applydate) {
        this.applydate = applydate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setPaidAmount(@PositiveOrZero(message = "paidAmount amount cannot be negative") Long paidAmount) {
        this.paidAmount = paidAmount;
    }

    // Helper methods
    public Long getPaidAmount() {

        return paidAmount;
    }

    public boolean isPaymentComplete() {
        return dueAmount != null && dueAmount == 0;
    }

    public String getPaymentStatus() {
        if (dueAmount == null) return "Pending";
        if (dueAmount == 0) return "Paid";
        if (dueAmount > 0 && totalAmount != null && dueAmount < totalAmount) return "Partial";
        return "Pending";
    }


}