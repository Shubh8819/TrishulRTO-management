

package com.trishul.Controller;

import com.trishul.DTO.InsuranceRequestDTO;
import com.trishul.DTO.InsuranceResponseDTO;
import com.trishul.Mapper.InsuranceMapper;
import com.trishul.Model.VehicleInsurance;
import com.trishul.service.InsuranceService;
import com.trishul.util.UtilClass;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/insurance")
public class InsuranceController {

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private InsuranceMapper insuranceMapper;

    /**
     * Dashboard page - Shows statistics and overview
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Get statistics
        long totalPolicies = insuranceService.countAllPolicies();
        long activePolicies = insuranceService.countByStatus("ACTIVE");
        long expiredPolicies = insuranceService.countByStatus("EXPIRED");
        long pendingPolicies = insuranceService.countByStatus("PENDING");
        long cancelledPolicies = insuranceService.countByStatus("CANCELLED");

        double totalPremium = insuranceService.getTotalPremium();
        double activePremium = insuranceService.getPremiumByStatus("ACTIVE");
        long expiringPolicies = insuranceService.getExpiringPoliciesCount();

        // Get recent policies for display
        List<InsuranceResponseDTO> recentPolicies = insuranceService.getRecentPolicies(10);

        model.addAttribute("pageTitle", "Insurance Dashboard");
        model.addAttribute("pageSubtitle", "Complete tracking & analytics for vehicle insurance");
        model.addAttribute("activePage", "insurance");
        model.addAttribute("pageContent", "insurance/dashboard");

        // Statistics
        model.addAttribute("totalPolicies", totalPolicies);
        model.addAttribute("activePolicies", activePolicies);
        model.addAttribute("expiredPolicies", expiredPolicies);
        model.addAttribute("pendingPolicies", pendingPolicies);
        model.addAttribute("cancelledPolicies", cancelledPolicies);
        model.addAttribute("totalPremium", totalPremium);
        model.addAttribute("activePremium", activePremium);
        model.addAttribute("expiringPolicies", expiringPolicies);
        model.addAttribute("recentPolicies", recentPolicies);

        return "fragments/layout";
    }

    /**
     * List all insurance policies with pagination and search
     */
    @GetMapping("/list")
    public String listPolicies(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "policyId") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        // Get policies with filters
        List<InsuranceResponseDTO> policies;
        long totalItems;

        if (search != null && !search.trim().isEmpty()) {
            policies = insuranceService.searchPolicies(search, pageable);
            totalItems = insuranceService.countSearchResults(search);
        } else if (status != null && !status.trim().isEmpty()) {
            policies = insuranceService.getPoliciesByStatus(status, pageable);
            totalItems = insuranceService.countByStatus(status);
        } else {
            policies = insuranceService.getAllPolicies(pageable);
            totalItems = insuranceService.countAllPolicies();
        }

        // Calculate total pages
        int totalPages = (int) Math.ceil((double) totalItems / size);

        // Format amounts
        double totalPremiumAmount = insuranceService.getTotalPremium();
        String totalPremiumInString = UtilClass.formatNumber(totalPremiumAmount);

        double avgPremium = totalItems > 0 ? totalPremiumAmount / totalItems : 0;
        String avgPremiumInString = UtilClass.formatNumber(avgPremium);

        model.addAttribute("pageTitle", "Insurance Policies");
        model.addAttribute("pageSubtitle", "Complete tracking & analytics for vehicle insurance");
        model.addAttribute("activePage", "insurance");
        model.addAttribute("pageContent", "insurance/insurance_list");

        // Pagination
        model.addAttribute("policies", policies);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("startRecord", page * size + 1);
        model.addAttribute("endRecord", Math.min((page + 1) * size, totalItems));

        // Filters
        model.addAttribute("searchTerm", search);
        model.addAttribute("statusFilter", status);

        // Statistics
        model.addAttribute("totalPremium", totalPremiumAmount);
        model.addAttribute("totalPremiumInString", totalPremiumInString);
        model.addAttribute("avgPremium", avgPremium);
        model.addAttribute("avgPremiumInString", avgPremiumInString);

        return "fragments/layout";
    }

    /**
     * Show create insurance policy form
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("insurance", new InsuranceRequestDTO());
        model.addAttribute("pageTitle", "Add New Insurance Policy");
        model.addAttribute("pageSubtitle", "Register a new vehicle insurance policy");
        model.addAttribute("activePage", "insurance");
        model.addAttribute("pageContent", "insurance/insurance_form");
        model.addAttribute("isEdit", false);

        // Add dropdown options
        addDropdownOptions(model);

        return "fragments/layout";
    }

    /**
     * Save new insurance policy
     */
    @PostMapping("/save")
    public String savePolicy(
            @Valid @ModelAttribute("insurance") InsuranceRequestDTO insuranceDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Add New Insurance Policy");
            model.addAttribute("pageSubtitle", "Register a new vehicle insurance policy");
            model.addAttribute("activePage", "insurance");
            model.addAttribute("pageContent", "insurance/insurance_form");
            model.addAttribute("isEdit", false);
            addDropdownOptions(model);
            return "fragments/layout";
        }

        try {
            VehicleInsurance entity = insuranceMapper.toEntity(insuranceDTO);
            insuranceService.savePolicy(entity);
            redirectAttributes.addFlashAttribute("message", "Insurance policy saved successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error saving policy: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/insurance/list";
    }

    /**
     * Show edit insurance policy form
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        VehicleInsurance entity = insuranceService.getPolicyById(id).orElse(null);

        if (entity == null) {
            model.addAttribute("message", "Policy not found with ID: " + id);
            model.addAttribute("messageType", "error");
            return "redirect:/insurance/list";
        }

        InsuranceRequestDTO insuranceDTO = insuranceMapper.toDTO(entity);
        model.addAttribute("insurance", insuranceDTO);
        model.addAttribute("policyId", id);
        model.addAttribute("pageTitle", "Edit Insurance Policy");
        model.addAttribute("pageSubtitle", "Update vehicle insurance policy details");
        model.addAttribute("activePage", "insurance");
        model.addAttribute("pageContent", "insurance/insurance_form");
        model.addAttribute("isEdit", true);

        addDropdownOptions(model);

        return "fragments/layout";
    }

    /**
     * Update existing insurance policy
     */
    @PostMapping("/update/{id}")
    public String updatePolicy(
            @PathVariable Long id,
            @Valid @ModelAttribute("insurance") InsuranceRequestDTO insuranceDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("policyId", id);
            model.addAttribute("pageTitle", "Edit Insurance Policy");
            model.addAttribute("pageSubtitle", "Update vehicle insurance policy details");
            model.addAttribute("activePage", "insurance");
            model.addAttribute("pageContent", "insurance/insurance_form");
            model.addAttribute("isEdit", true);
            addDropdownOptions(model);
            return "fragments/layout";
        }

        try {
            VehicleInsurance entity = insuranceMapper.toEntity(insuranceDTO);
            insuranceService.updatePolicy(id, entity);
            redirectAttributes.addFlashAttribute("message", "Insurance policy updated successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error updating policy: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/insurance/list";
    }

    /**
     * View insurance policy details (Read-Only)
     */
    @GetMapping("/view/{id}")
    public String showViewForm(@PathVariable Long id, Model model) {
        VehicleInsurance entity = insuranceService.getPolicyById(id).orElse(null);

        if (entity == null) {
            model.addAttribute("message", "Policy not found with ID: " + id);
            model.addAttribute("messageType", "error");
            return "redirect:/insurance/list";
        }

        InsuranceResponseDTO insuranceDTO = insuranceMapper.toResponseDTO(entity);
        model.addAttribute("insurance", insuranceDTO);
        model.addAttribute("pageTitle", "View Insurance Policy");
        model.addAttribute("pageSubtitle", "View complete insurance policy details");
        model.addAttribute("activePage", "insurance");
        model.addAttribute("pageContent", "insurance/insurance_view");

        return "fragments/layout";
    }

    /**
     * Delete insurance policy
     */
    @GetMapping("/delete/{id}")
    public String deletePolicy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            insuranceService.deletePolicy(id);
            redirectAttributes.addFlashAttribute("message", "Insurance policy deleted successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error deleting policy: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/insurance/list";
    }

    /**
     * Renew insurance policy
     */
    @GetMapping("/renew/{id}")
    public String renewPolicy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            insuranceService.renewPolicy(id);
            redirectAttributes.addFlashAttribute("message", "Insurance policy renewed successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error renewing policy: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/insurance/list";
    }

    /**
     * Cancel insurance policy
     */
    @GetMapping("/cancel/{id}")
    public String cancelPolicy(
            @PathVariable Long id,
            @RequestParam(required = false) String reason,
            RedirectAttributes redirectAttributes) {
        try {
            insuranceService.cancelPolicy(id, reason);
            redirectAttributes.addFlashAttribute("message", "Insurance policy cancelled successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error cancelling policy: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/insurance/list";
    }

    // ========== Private Helper Methods ==========

    private void addDropdownOptions(Model model) {
        model.addAttribute("vehicleTypes", getVehicleTypes());
        model.addAttribute("policyTypes", getPolicyTypes());
        model.addAttribute("insuranceCompanies", getInsuranceCompanies());
        model.addAttribute("addonCovers", getAddonCovers());
        model.addAttribute("policyStatus", getPolicyStatuses());
    }

    private List<String> getVehicleTypes() {
        return List.of(
                "2 Wheeler",
                "3 Wheeler",
                "4 Wheeler (Private)",
                "4 Wheeler (Commercial)",
                "Heavy Vehicle"
        );
    }

    private List<String> getPolicyTypes() {
        return List.of(
                "Comprehensive (Full Coverage)",
                "Third Party (Liability Only)",
                "Zero Depreciation (Bumper to Bumper)",
                "Standalone Own Damage"
        );
    }

    private List<String> getInsuranceCompanies() {
        return List.of(
                "New India Assurance",
                "United India Insurance",
                "National Insurance",
                "Oriental Insurance",
                "ICICI Lombard",
                "Bajaj Allianz",
                "HDFC Ergo"
        );
    }

    private List<String> getAddonCovers() {
        return List.of(
                "Roadside Assistance",
                "Engine Protection",
                "Consumables Cover",
                "No Claim Bonus Protection",
                "Personal Accident Cover",
                "Return to Invoice"
        );
    }

    private List<String> getPolicyStatuses() {
        return List.of(
                "ACTIVE",
                "EXPIRED",
                "CANCELLED",
                "PENDING",
                "RENEWED"
        );
    }
}