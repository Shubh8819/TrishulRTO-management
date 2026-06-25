package com.trishul.Controller;

import com.trishul.DTO.LicenceDTO;
import com.trishul.Mapper.LicenceMapper;
import com.trishul.Model.LicenceEntity;
import com.trishul.service.LicenceService;
import com.trishul.util.UtilClass;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/licence")
public class LicenceController {

    @Autowired
    private LicenceService licenceService;

    @Autowired
    private LicenceMapper licenceMapper;

    @GetMapping("/list")
    public String listLicences(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "liId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        List<LicenceDTO> licencePage = licenceService.getLicenceWithSearch(search, pageable);
        //List<LicenceEntity> licences = licenceService.getAllLicences();

        double totalDueAmount=licenceService.totalDueAmount();
        String totalDueAmountInstring = UtilClass.formatNumber( totalDueAmount);
        int totalNumberOfLincence=licenceService.getTotalNumberOfLincence();

        model.addAttribute("pageTitle", "Licence Management");
        model.addAttribute("pageSubtitle", "Complete tracking & analytics for driving licences");
        model.addAttribute("activePage", "licences");
        model.addAttribute("licenceCount", totalNumberOfLincence);
        model.addAttribute("totalLicences", 0);
        model.addAttribute("licencesOver30Days", 0);
        model.addAttribute("totalDueAmount", totalDueAmountInstring);
        model.addAttribute("totalItems", totalNumberOfLincence);
        model.addAttribute("totalPages", totalNumberOfLincence/size);
        model.addAttribute("currentPage", page);
        model.addAttribute("licences", licencePage);
        model.addAttribute("pageSize", size);
        model.addAttribute("startRecord", page * size + 1);
        model.addAttribute("endRecord", Math.min((page + 1) * size, 150));


        model.addAttribute("pageContent", "licence/licence_list");
       // model.addAttribute("pageContent", "licence/dashboard");

        return "fragments/layout";
       // return "licence/licence_list";
    }


    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("licence", new LicenceDTO());
        model.addAttribute("pageTitle", "Add New Licence");
        model.addAttribute("pageContent", "licence/licence_form");
        // model.addAttribute("pageContent", "licence/dashboard");

        return "fragments/layout";

    }

    @PostMapping("/save")
    public String saveLicence(@Valid @ModelAttribute("licence") LicenceDTO licenceDTO,BindingResult result, Model model,RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Add New Licence");
            model.addAttribute("pageContent", "licence/licence_form");
            // model.addAttribute("pageContent", "licence/dashboard");

            return "fragments/layout";
        }

        LicenceEntity entity = licenceMapper.toEntity(licenceDTO);
        licenceService.saveLicence(entity);
        redirectAttributes.addFlashAttribute("message", "Licence saved successfully!");
        return "redirect:/licence/list";
    }

   @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        LicenceEntity entity = licenceService.getLicenceById(id).orElse(null);

        LicenceDTO licenceDTO = licenceMapper.toDTO(entity);
        model.addAttribute("licence", licenceDTO);
        model.addAttribute("pageTitle", "Edit Licence");
       model.addAttribute("pageContent", "licence/licence_form");
       return "fragments/layout";
    }

   @PostMapping("/update/{id}")
    public String updateLicence(@PathVariable Long id,
                                @Valid @ModelAttribute("licence") LicenceDTO licenceDTO,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Licence");
            return "licence_form";
        }
       LicenceEntity licenceEntity= licenceMapper.toEntity(licenceDTO) ;
        licenceService.updateLicence(id,licenceEntity);
        redirectAttributes.addFlashAttribute("message", "Licence updated successfully!");
        return "redirect:/licence/list";
    }

   @PostMapping("/delete/{id}")
    public String deleteLicence(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        licenceService.deleteLicence(id);
        redirectAttributes.addFlashAttribute("message", "Licence deleted successfully!");
        return "redirect:/licence/list";
    }
    // View Licence (Read-Only)
    @GetMapping("/view/{id}")
    public String showViewForm(@PathVariable Long id, Model model) {
        LicenceEntity entity = licenceService.getLicenceById(id).orElse(null);
        LicenceDTO licenceDTO = licenceMapper.toDTO(entity);

        // Set default values if null


        model.addAttribute("licence", licenceDTO);
        model.addAttribute("pageTitle", "View Licence");
        model.addAttribute("pageSubtitle", "View complete licence details");
        model.addAttribute("activePage", "licences");
        model.addAttribute("pageContent", "licence/licence_view");

        return "fragments/layout";
    }
}