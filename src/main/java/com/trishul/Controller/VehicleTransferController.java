package com.trishul.Controller;

import com.trishul.DTO.VehicleTransferDTO;
import com.trishul.Mapper.VehicleTransferMapper;
import com.trishul.Model.DocumentEntity;
import com.trishul.Model.VehicleTransferEntity;
import com.trishul.service.VehicleTransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/vehicle-transfer")
public class VehicleTransferController {

    @Autowired
    private VehicleTransferService vehicleTransferService;

    @Autowired
    private VehicleTransferMapper vehicleTransferMapper;

    @Value("${file.upload.directory:uploads}")
    private String uploadDirectory;

    // List page with pagination
    @GetMapping("/list")
    public String listTransfers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transferDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String search,
            Model model) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<VehicleTransferEntity> transferPage;
        if (search != null && !search.trim().isEmpty()) {
            transferPage = vehicleTransferService.searchTransfers(search.trim(), pageable);
        } else {
            transferPage = vehicleTransferService.getAllTransfersPaginated(pageable);
        }

        List<VehicleTransferDTO> transferDTOs = transferPage.getContent().stream()
                .map(vehicleTransferMapper::toDTO)
                .toList();

        // Statistics
        long totalTransfers = vehicleTransferService.getTotalTransfersCount();
        long pendingTransfers = vehicleTransferService.getPendingTransfersCount();
        long completedTransfers = vehicleTransferService.getCompletedTransfersCount();
        Long totalDueAmount = vehicleTransferService.getTotalDueAmount();
        Long totalTransferFee = vehicleTransferService.getTotalTransferFee();

        model.addAttribute("transfers", transferDTOs);
        model.addAttribute("totalTransfers", totalTransfers);
        model.addAttribute("pendingTransfers", pendingTransfers);
        model.addAttribute("completedTransfers", completedTransfers);
        model.addAttribute("totalDueAmount", totalDueAmount);
        model.addAttribute("totalTransferFee", totalTransferFee);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", transferPage.getTotalPages());
        model.addAttribute("totalItems", transferPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("search", search);
        model.addAttribute("pageTitle", "Vehicle Transfer Management");
        model.addAttribute("pageSubtitle", "Manage all vehicle transfer applications");

        // Set page content for layout
        model.addAttribute("pageContent", "VehicleTransfer/vehicle-transfer_list");

        return "fragments/layout";
    }

    // New transfer form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("transfer", new VehicleTransferDTO());
        model.addAttribute("pageTitle", "New Vehicle Transfer");
        model.addAttribute("pageSubtitle", "Register a new vehicle transfer application");
       // model.addAttribute("pageContent", "VehicleTransfer/vehicletransfer_form :: pageContent");
        model.addAttribute("pageContent", "VehicleTransfer/vehicletransfer_form");
        return "fragments/layout";
    }

    // Save transfer - UPDATED to handle files separately
    @PostMapping("/save")
    public String saveTransfer(
            @Valid @ModelAttribute("transfer") VehicleTransferDTO transferDTO,
            BindingResult result,
            @RequestParam(value = "documents", required = false) MultipartFile[] documents,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "New Vehicle Transfer");
            model.addAttribute("pageSubtitle", "Register a new vehicle transfer application");
            model.addAttribute("pageContent", "VehicleTransfer/vehicletransfer_form");

            return "fragments/layout";
        }

        try {
            // Set default status
            if (transferDTO.getStatus() == null) {
                transferDTO.setStatus("PENDING");
            }

            VehicleTransferEntity entity = vehicleTransferMapper.toEntity(transferDTO);

            // Handle document uploads
            if (documents != null && documents.length > 0) {
                List<DocumentEntity> documentList = new ArrayList<>();
                for (MultipartFile file : documents) {
                    if (!file.isEmpty()) {
                        String originalFilename = file.getOriginalFilename();
                        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                        String newFilename = UUID.randomUUID().toString() + fileExtension;

                        Path uploadPath = Paths.get(uploadDirectory);
                        if (!Files.exists(uploadPath)) {
                            Files.createDirectories(uploadPath);
                        }

                        Path filePath = uploadPath.resolve(newFilename);
                        Files.write(filePath, file.getBytes());

                        DocumentEntity document = new DocumentEntity();
                        document.setFileName(newFilename);
                        document.setOriginalFileName(originalFilename);
                        document.setFilePath(filePath.toString());
                        document.setFileSize(file.getSize());
                        document.setFileType(file.getContentType());
                        document.setDocumentType(getDocumentType(originalFilename));
                        document.setVehicleTransfer(entity);

                        documentList.add(document);
                    }
                }
               /* entity.setDocuments(documentList);*/
            }

            vehicleTransferService.saveTransfer(entity);
            redirectAttributes.addFlashAttribute("message", "Vehicle transfer saved successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Error saving documents: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:list";
    }

    // Edit transfer
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        VehicleTransferEntity entity = vehicleTransferService.getTransferById(id);
        VehicleTransferDTO transferDTO = vehicleTransferMapper.toDTO(entity);
        model.addAttribute("transfer", transferDTO);
        model.addAttribute("pageTitle", "Edit Vehicle Transfer");
        model.addAttribute("pageSubtitle", "Update vehicle transfer application details");
        model.addAttribute("pageContent", "VehicleTransfer/vehicletransfer_form");
        return "fragments/layout";
    }

    // Update transfer - UPDATED to handle files separately
    @PostMapping("/update/{id}")
    public String updateTransfer(
            @PathVariable Long id,
            @Valid @ModelAttribute("transfer") VehicleTransferDTO transferDTO,
            BindingResult result,
            @RequestParam(value = "documents", required = false) MultipartFile[] documents,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Vehicle Transfer");
            model.addAttribute("pageSubtitle", "Update vehicle transfer application details");
            model.addAttribute("pageContent", "VehicleTransfer/vehicletransfer_form ");
            return "fragments/layout";
        }

        try {
            VehicleTransferEntity existingEntity = vehicleTransferService.getTransferById(id);
            vehicleTransferMapper.updateEntityFromDTO(transferDTO, existingEntity);

            // Handle new document uploads
            if (documents != null && documents.length > 0) {
                List<DocumentEntity> existingDocs = null;
                        //existingEntity.getDocuments();
                if (existingDocs == null) {
                    existingDocs = new ArrayList<>();
                }

                for (MultipartFile file : documents) {
                    if (!file.isEmpty()) {
                        String originalFilename = file.getOriginalFilename();
                        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                        String newFilename = UUID.randomUUID().toString() + fileExtension;

                        Path uploadPath = Paths.get(uploadDirectory);
                        if (!Files.exists(uploadPath)) {
                            Files.createDirectories(uploadPath);
                        }

                        Path filePath = uploadPath.resolve(newFilename);
                        Files.write(filePath, file.getBytes());

                        DocumentEntity document = new DocumentEntity();
                        document.setFileName(newFilename);
                        document.setOriginalFileName(originalFilename);
                        document.setFilePath(filePath.toString());
                        document.setFileSize(file.getSize());
                        document.setFileType(file.getContentType());
                        document.setDocumentType(getDocumentType(originalFilename));
                        document.setVehicleTransfer(existingEntity);

                        existingDocs.add(document);
                    }
                }
               // existingEntity.setDocuments(existingDocs);
            }

            vehicleTransferService.updateTransfer(existingEntity);
            redirectAttributes.addFlashAttribute("message", "Vehicle transfer updated successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Error updating documents: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/VehicleTransfer/vehicle-transfer_list";
    }

    // Delete transfer
    @GetMapping("/delete/{id}")
    public String deleteTransfer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Delete associated documents
            VehicleTransferEntity entity = vehicleTransferService.getTransferById(id);
            /*if (entity.getDocuments() != null) {
                for (DocumentEntity doc : entity.getDocuments()) {
                    Path filePath = Paths.get(doc.getFilePath());
                    Files.deleteIfExists(filePath);
                }
            }*/

            vehicleTransferService.deleteTransfer(id);
            redirectAttributes.addFlashAttribute("message", "Vehicle transfer deleted successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error deleting transfer: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/vehicle-transfer/list";
    }

    // View transfer details
    @GetMapping("/view/{id}")
    public String viewTransfer(@PathVariable Long id, Model model) {
        VehicleTransferEntity entity = vehicleTransferService.getTransferById(id);
        VehicleTransferDTO transferDTO = vehicleTransferMapper.toDTO(entity);
        model.addAttribute("transfer", transferDTO);
        model.addAttribute("pageTitle", "View Vehicle Transfer");
        model.addAttribute("pageSubtitle", "View vehicle transfer application details");
        model.addAttribute("pageContent", "vehicle-transfer/view :: pageContent");
        return "fragments/layout";
    }

    // Update status
    @PostMapping("/status/{id}")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {

        try {
            vehicleTransferService.updateTransferStatus(id, status);
            redirectAttributes.addFlashAttribute("message", "Status updated successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error updating status: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/vehicle-transfer/list";
    }

    private String getDocumentType(String filename) {
        String lowerCase = filename.toLowerCase();
        if (lowerCase.contains("rc") || lowerCase.contains("registration")) {
            return "RC Copy";
        } else if (lowerCase.contains("insurance")) {
            return "Insurance Copy";
        } else if (lowerCase.contains("puc")) {
            return "PUC Certificate";
        } else if (lowerCase.contains("id") || lowerCase.contains("aadhar")) {
            return "ID Proof";
        } else if (lowerCase.contains("address") || lowerCase.contains("bill")) {
            return "Address Proof";
        } else if (lowerCase.contains("transfer") || lowerCase.contains("noc")) {
            return "Transfer Document";
        } else {
            return "Other Document";
        }
    }
}