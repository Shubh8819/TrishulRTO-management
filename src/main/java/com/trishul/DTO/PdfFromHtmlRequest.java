package com.trishul.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request body for POST /api/pdf/from-html
 */
@Data
public class PdfFromHtmlRequest {

    /** Raw HTML content (full page or fragment). Required. */
    @NotBlank(message = "htmlContent must not be blank")
    private String htmlContent;

    /** Desired PDF filename (e.g. "licence-report.pdf"). Optional. */
    private String filename = "document.pdf";
}
