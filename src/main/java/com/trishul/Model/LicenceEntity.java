package com.trishul.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Licence")

public class LicenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liId")
    private Long liId;


    @Column(name = "customerName")
    private String customerName;

    @Column(name = "father")
    private String father;

    @Column(name = "leaningLinceNo")
    private String leaningLinceNo;

    @Column(name = "divingLincence")
    private String divingLincence;

    @Column(name = "mobNumber")
    private String mobNumber;

    @Column(name="totalamount")
    private Long totalAmount;

    @Column(name="dueAmount")
    private Long dueAmount;

    @Column(name = "applyDate")
    private String applydate;

    @Column(name = "vehicleType")
    private String vehicleType;

    @Column(name = "licenceTypes", columnDefinition = "int default -1")
    private Integer licenceType =-1;


    @OneToMany(mappedBy = "licence", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DocumentEntity> documents = new ArrayList<>();

    @Column(name = "paidAmount")
    Long paidAmount;

    public void setPaidAmount(@PositiveOrZero(message = "paidAmount amount cannot be negative") Long paidAmount) {
        this.paidAmount = paidAmount;
    }

    // Helper methods
    public Long getPaidAmount() {

        return paidAmount;
    }

    // Getters and Setters
    public List<DocumentEntity> getDocuments() { return documents; }
    public void setDocuments(List<DocumentEntity> documents) { this.documents = documents; }

    public LicenceEntity(Long liId, String customerName, String father, String leaningLinceNo, String divingLincence, String mobNumber, Long totalAmount, Long dueAmount, String applydate, String vehicleType) {
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

    public LicenceEntity() {
    }

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

    public Integer getLicenceType() {
        return licenceType;
    }

    public void setLicenceType(Integer licenceType) {
        this.licenceType = licenceType ;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
