package com.trishul.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Common PDF Generation Service for Licence Management System.
 *
 * Converts HTML content (full page or specific div) into a professional A4 PDF.
 * Uses OpenHTMLtoPDF + PDFBox under the hood.
 */
@Slf4j
@Service
public class PdfGenerationService {

    // ─── Professional A4 CSS injected into every PDF ──────────────────────────

    private static final String BASE_CSS = """
            @page {
                size: A4;
                margin: 20mm 15mm 20mm 15mm;
            }
            * {
                  box-sizing: border-box;
              }
            body {
                font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif;
                font-size: 10pt;
                line-height: 1.5;
                color: #1a1a1a;
                margin: 0;
                padding: 0;
                background: #ffffff;
            }
            h1, h2, h3, h4, h5, h6 {
                color: #1a3a5c;
                margin-top: 0;
                page-break-after: avoid;
            }
            h1 { font-size: 18pt; border-bottom: 2px solid #1a3a5c; padding-bottom: 6px; margin-bottom: 12px; }
            h2 { font-size: 14pt; border-bottom: 1px solid #ccc;    padding-bottom: 4px; margin-bottom: 10px; }
            h3 { font-size: 12pt; margin-bottom: 8px; }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 16px;
                font-size: 9pt;
                page-break-inside: auto;
            }
            thead { display: table-header-group; }
            tr     { page-break-inside: avoid; }
            th {
                background-color: #1a3a5c;
                color: #ffffff;
                padding: 8px 10px;
                text-align: left;
                font-weight: bold;
                font-size: 9pt;
            }
            td {
                padding: 7px 10px;
                border-bottom: 1px solid #e0e0e0;
                vertical-align: top;
            }
            tr:nth-child(even) td { background-color: #f7f9fc; }
            .card, .licence-card {
                border: 1px solid #d0d9e4;
                border-radius: 6px;
                padding: 16px;
                margin-bottom: 16px;
                background: #fafcff;
                page-break-inside: avoid;
            }
            .badge, .status-badge {
                display: inline-block;
                padding: 3px 10px;
                border-radius: 12px;
                font-size: 8pt;
                font-weight: bold;
            }
            .badge-active,   .status-active   { background: #d4edda; color: #155724; }
            .badge-expired,  .status-expired  { background: #f8d7da; color: #721c24; }
            .badge-pending,  .status-pending  { background: #fff3cd; color: #856404; }
            .badge-inactive, .status-inactive { background: #e2e3e5; color: #383d41; }
            .label {
                font-size: 8pt;
                color: #6c757d;
                text-transform: uppercase;
                letter-spacing: 0.5px;
                font-weight: bold;
            }
            .value {
                font-size: 10pt;
                color: #1a1a1a;
                font-weight: 500;
            }
            .section-divider {
                border: none;
                border-top: 1px solid #dee2e6;
                margin: 16px 0;
            }
            .text-muted  { color: #6c757d; }
            .text-right  { text-align: right; }
            .text-center { text-align: center; }
            .font-bold   { font-weight: bold; }
            .page-break  { page-break-before: always; }
            /* Bootstrap grid compat — simple two-column helper */
            .row { display: table; width: 100%; }
            .col-6 { display: table-cell; width: 50%; vertical-align: top; padding: 0 8px; }
            /* Utility spacing */
            .mt-0 { margin-top: 0; }
            .mb-0 { margin-bottom: 0; }
            .mb-2 { margin-bottom: 8px; }
            .mb-4 { margin-bottom: 16px; }
            .p-3  { padding: 12px; }
            /* Hide elements that must not appear in PDF */
            .no-print, .btn, button, .navbar, .sidebar, .footer-nav,
            [data-pdf-hide], .modal-backdrop { display: none !important; }
            /* PDF-specific helpers */
            [data-pdf-show] { display: block !important; }
            """;

    // ─── Public API ────────────────────────────────────────────────────────────

    /**
     * Generate a PDF from a raw HTML string.
     * The HTML may be a full page or a fragment — both are handled.
     *
     * @param html     raw HTML content
     * @param baseUri  base URI for resolving relative resources (e.g. "http://localhost:8080/")
     * @return PDF bytes
     */
    public byte[] generateFromHtml(String html, String baseUri) {
        log.debug("Generating PDF from raw HTML ({} chars)", html.length());
        String cleanedHtml = prepareFullPageHtml(html);
        return renderToPdf(cleanedHtml, baseUri);
    }

    /**
     * Extract a specific div by its ID and generate a PDF from it.
     *
     * @param fullHtml full page HTML (e.g. from UI)
     * @param divId    the id attribute of the target div
     * @param baseUri  base URI for resolving relative resources
     * @return PDF bytes
     */
    public byte[] generateFromDivId(String fullHtml, String divId, String baseUri) {
        log.debug("Generating PDF from div#{}", divId);

        // Parse and extract target element
        org.jsoup.nodes.Document jsoup = Jsoup.parse(fullHtml);
        Element target = jsoup.getElementById(divId);

        if (target == null) {
            throw new IllegalArgumentException("No element found with id='" + divId + "'");
        }

        // Wrap the extracted fragment in a clean A4 page
        String wrappedHtml = wrapFragment(target.outerHtml(), jsoup.title());
        return renderToPdf(wrappedHtml, baseUri);
    }

    /**
     * Generate a PDF from a pre-rendered Thymeleaf/template HTML string.
     * Use this when you've already rendered the template server-side.
     *
     * @param renderedHtml fully rendered HTML from a Thymeleaf template
     * @param baseUri      base URI for resolving relative resources
     * @return PDF bytes
     */
    public byte[] generateFromTemplate(String renderedHtml, String baseUri) {
        log.debug("Generating PDF from rendered template");
        return generateFromHtml(renderedHtml, baseUri);
    }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Ensure the HTML is a complete, well-formed XHTML page with our base CSS injected.
     */
    private String prepareFullPageHtml(String html) {
        org.jsoup.nodes.Document jsoup = Jsoup.parse(html);
        jsoup.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);

        // Inject charset meta if missing
        if (jsoup.select("meta[charset]").isEmpty()) {
            jsoup.head().prepend("<meta charset=\"UTF-8\"/>");
        }

        // Remove or mark non-print elements
        jsoup.select(".no-print, .btn, button, .navbar, .sidebar, .footer-nav, [data-pdf-hide]")
                .forEach(el -> el.attr("style", "display:none!important"));

        // Inject our base CSS (first, so page-level styles can override)
        jsoup.head().prepend("<style>" + BASE_CSS + "</style>");

        return jsoup.outerHtml();
    }

    /**
     * Wrap a raw HTML fragment (e.g. extracted div) in a complete XHTML skeleton.
     */
    private String wrapFragment(String fragmentHtml, String title) {
        return """
               <?xml version="1.0" encoding="UTF-8"?>
               <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
                   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
               <html xmlns="http://www.w3.org/1999/xhtml">
               <head>
                   <meta charset="UTF-8"/>
                   <title>%s</title>
                   <style>%s</style>
               </head>
               <body>
               %s
               </body>
               </html>
               """.formatted(
                title != null ? title : "Licence Document",
                BASE_CSS,
                fragmentHtml
        );
    }

    /**
     * Core rendering step: Jsoup parse → W3C DOM → OpenHTMLtoPDF → byte[]
     */
    private byte[] renderToPdf(String xhtml, String baseUri) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(xhtml);
            jsoupDoc.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
            org.w3c.dom.Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withW3cDocument(w3cDoc, baseUri != null ? baseUri : "/");
            builder.toStream(baos);
            builder.run();

            byte[] pdfBytes = baos.toByteArray();
            log.info("PDF generated successfully ({} bytes)", pdfBytes.length);
            return pdfBytes;

        } catch (IOException e) {
            log.error("PDF generation failed", e);
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage(), e);
        }
    }
}
