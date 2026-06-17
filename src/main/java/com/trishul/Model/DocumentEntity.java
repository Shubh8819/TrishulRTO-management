package com.trishul.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    // ===== RELATIONSHIPS =====

    // For Vehicle Transfer Module
    @ManyToOne
    @JoinColumn(name = "vehicle_transfer_id")
    private VehicleTransferEntity vehicleTransfer;

    // For Licence Module (if needed)
    @ManyToOne
    @JoinColumn(name = "licence_id")
    private LicenceEntity licence;

    // Constructors
    public DocumentEntity() {
        this.uploadDate = LocalDateTime.now();
    }

    public DocumentEntity(String fileName, String originalFileName, String filePath,
                          Long fileSize, String fileType, String documentType) {
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.documentType = documentType;
        this.uploadDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }

    public VehicleTransferEntity getVehicleTransfer() { return vehicleTransfer; }
    public void setVehicleTransfer(VehicleTransferEntity vehicleTransfer) { this.vehicleTransfer = vehicleTransfer; }

    public LicenceEntity getLicence() { return licence; }
    public void setLicence(LicenceEntity licence) { this.licence = licence; }
}