package com.trishul.DTO;

import java.time.LocalDateTime;

public class DocumentDTO {

    private Long id;
    private String fileName;
    private String originalFileName;
    private String filePath;
    private Long fileSize;
    private String fileType;
    private String documentType;
    private LocalDateTime uploadDate;
    private Long vehicleTransferId;
    private Long licenceId;

    // Constructors
    public DocumentDTO() {}

    public DocumentDTO(Long id, String fileName, String originalFileName, String filePath,
                       Long fileSize, String fileType, String documentType) {
        this.id = id;
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.documentType = documentType;
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

    public Long getVehicleTransferId() { return vehicleTransferId; }
    public void setVehicleTransferId(Long vehicleTransferId) { this.vehicleTransferId = vehicleTransferId; }

    public Long getLicenceId() { return licenceId; }
    public void setLicenceId(Long licenceId) { this.licenceId = licenceId; }
}