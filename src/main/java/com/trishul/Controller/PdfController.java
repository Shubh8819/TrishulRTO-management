package com.trishul.Controller;

import com.trishul.DTO.PdfFromDivRequest;
import com.trishul.DTO.PdfFromHtmlRequest;

import com.trishul.DTO.PdfFromHtmlRequest;
import com.trishul.service.PdfGenerationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller exposing PDF generation endpoints.
 *
 * Endpoints:
 *   POST /api/pdf/from-html        — full HTML page → PDF
 *   POST /api/pdf/from-div         — extract div#id from HTML → PDF
 *   GET  /api/pdf/licence/{id}     — server-side: render template → PDF
 */
@Slf4j
@RestController
@RequestMapping("/api/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final PdfGenerationService pdfGenerationService;

    // ─── 1. Full HTML → PDF ───────────────────────────────────────────────────

    /**
     * Accept any raw HTML string from the frontend, return a downloadable PDF.
     *
     * Front-end usage:
     *   const html = document.documentElement.outerHTML;
     *   PdfHelper.downloadFromHtml(html, 'my-document.pdf');
     */
    @PostMapping("/from-html")
    public ResponseEntity<byte[]> generateFromHtml(
            @Valid @RequestBody PdfFromHtmlRequest request,
            HttpServletRequest httpRequest) {

        log.info("PDF request: from-html, filename={}", request.getFilename());

        String baseUri = resolveBaseUri(httpRequest);
        byte[] pdf = pdfGenerationService.generateFromHtml(request.getHtmlContent(), baseUri);

        return buildPdfResponse(pdf, request.getFilename());
    }

    // ─── 2. Div#id extraction → PDF ──────────────────────────────────────────

    /**
     * Extract a specific div from the full page HTML and render only that section as PDF.
     *
     * Front-end usage:
     *   PdfHelper.downloadFromDiv('licenceDetailsCard', 'licence-123.pdf');
     */
    @PostMapping("/from-div")
    public ResponseEntity<byte[]> generateFromDiv(
            @Valid @RequestBody PdfFromDivRequest request,
            HttpServletRequest httpRequest) {

        log.info("PDF request: from-div, divId={}, filename={}", request.getDivId(), request.getFilename());

        String baseUri = resolveBaseUri(httpRequest);
        byte[] pdf = pdfGenerationService.generateFromDivId(
                request.getHtmlContent(),
                request.getDivId(),
                baseUri
        );

        return buildPdfResponse(pdf, request.getFilename());
    }

    // ─── 3. Server-side template → PDF (example for licence detail) ──────────
    //
    //  Wire in your Thymeleaf TemplateEngine + licence service here.
    //  Uncomment and adapt once those beans are available.
    //
    //  @Autowired private TemplateEngine templateEngine;
    //  @Autowired private LicenceService  licenceService;
    //
    //  @GetMapping("/licence/{id}")
    //  public ResponseEntity<byte[]> generateLicencePdf(
    //          @PathVariable Long id,
    //          HttpServletRequest httpRequest) {
    //
    //      LicenceDto licence = licenceService.findById(id);
    //
    //      Context ctx = new Context();
    //      ctx.setVariable("licence", licence);
    //
    //      String html = templateEngine.process("pdf/licence-detail", ctx);
    //      byte[] pdf  = pdfGenerationService.generateFromTemplate(html, resolveBaseUri(httpRequest));
    //
    //      String filename = "licence-" + id + ".pdf";
    //      return buildPdfResponse(pdf, filename);
    //  }

    // ─── Shared helpers ───────────────────────────────────────────────────────

    private ResponseEntity<byte[]> buildPdfResponse(byte[] pdf, String filename) {
        String safeFilename = (filename != null && !filename.isBlank())
                ? filename.replaceAll("[^a-zA-Z0-9._\\-]", "_")
                : "document.pdf";

        if (!safeFilename.toLowerCase().endsWith(".pdf")) {
            safeFilename += ".pdf";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + safeFilename + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .body(pdf);
    }

    private String resolveBaseUri(HttpServletRequest request) {
        // Build "http://localhost:8080/" so relative CSS/image URLs resolve correctly
        return request.getScheme() + "://"
                + request.getServerName() + ":"
                + request.getServerPort()
                + request.getContextPath() + "/";
    }
}
