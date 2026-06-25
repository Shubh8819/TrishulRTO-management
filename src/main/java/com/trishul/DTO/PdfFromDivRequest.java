package com.trishul.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request body for POST /api/pdf/from-div
 */
@Data
public class PdfFromDivRequest {

    /** Full page HTML from which the target div will be extracted. Required. */
    @NotBlank(message = "htmlContent must not be blank")
    private String htmlContent;

    /** The id attribute of the div to extract (without the '#'). Required. */
    @NotBlank(message = "divId must not be blank")
    private String divId;

    /** Desired PDF filename. Optional. */
    private String filename = "document.pdf";
}
