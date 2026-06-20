/**
 * PdfHelper.js — Client-side PDF generation helper
 * Licence Management System
 *
 * Provides three ways to trigger a PDF download from the UI:
 *   1. PdfHelper.downloadFromHtml(html, filename)   — send raw HTML string
 *   2. PdfHelper.downloadFromDiv(divId, filename)   — extract a div and send it
 *   3. PdfHelper.downloadFromUrl(url, filename)     — server-side template endpoint
 *
 * Dependencies: none (vanilla JS, fetch API)
 * Browser support: Chrome 66+, Firefox 57+, Safari 12+, Edge 79+
 */

const PdfHelper = (() => {

    // ── Configuration ─────────────────────────────────────────────────────────

    const API_BASE = '/api/pdf';            // adjust if your context path differs
    const DEFAULT_TIMEOUT_MS = 30_000;      // 30-second timeout for large pages

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Send the current full page HTML (or a custom HTML string) to the backend
     * and download the resulting PDF.
     *
     * @param {string} [html]       HTML to send. Defaults to full document HTML.
     * @param {string} [filename]   PDF filename. Defaults to page title.
     * @returns {Promise<void>}
     *
     * @example
     * // Download entire current page as PDF
     * PdfHelper.downloadFromHtml();
     *
     * // Download a custom HTML string
     * PdfHelper.downloadFromHtml('<h1>Hello</h1><p>World</p>', 'hello.pdf');
     */
    async function downloadFromHtml(html, filename) {
        const htmlContent = html || document.documentElement.outerHTML;
        const pdfFilename = filename || _pageTitle() + '.pdf';

        await _postAndDownload(`${API_BASE}/from-html`, { htmlContent, filename: pdfFilename }, pdfFilename);
    }

    /**
     * Extract a specific div by its element id, then send it to the backend
     * and download the resulting PDF.
     *
     * @param {string} divId        The id of the div to capture (without '#').
     * @param {string} [filename]   PDF filename.
     * @returns {Promise<void>}
     *
     * @example
     * // Download only the licence details card
     * PdfHelper.downloadFromDiv('licenceDetailsCard', 'licence-123.pdf');
     *
     * // Works from an onclick attribute too:
     * // <button onclick="PdfHelper.downloadFromDiv('reportTable', 'report.pdf')">Download</button>
     */
    async function downloadFromDiv(divId, filename) {
        const el = document.getElementById(divId);
        if (!el) {
            _showError(`Element with id="${divId}" not found on this page.`);
            return;
        }

        const htmlContent = document.documentElement.outerHTML;  // send full page so CSS is present
        const pdfFilename = filename || divId + '.pdf';

        await _postAndDownload(
            `${API_BASE}/from-div`,
            { htmlContent, divId, filename: pdfFilename },
            pdfFilename
        );
    }

    /**
     * Trigger a server-side PDF generation by hitting a GET endpoint
     * (e.g. /api/pdf/licence/42) that returns a PDF response directly.
     *
     * @param {string} url          Server endpoint that returns application/pdf.
     * @param {string} [filename]   PDF filename.
     * @returns {Promise<void>}
     *
     * @example
     * PdfHelper.downloadFromUrl('/api/pdf/licence/42', 'licence-42.pdf');
     */
    async function downloadFromUrl(url, filename) {
        const pdfFilename = filename || 'document.pdf';
        const loaderId = _showLoader('Generating PDF…');

        try {
            const response = await _fetchWithTimeout(url, { method: 'GET' });

            if (!response.ok) {
                const msg = await _extractErrorMessage(response);
                throw new Error(msg);
            }

            const blob = await response.blob();
            _triggerDownload(blob, pdfFilename);

        } catch (err) {
            _showError('PDF download failed: ' + err.message);
            console.error('[PdfHelper] downloadFromUrl error:', err);
        } finally {
            _hideLoader(loaderId);
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    async function _postAndDownload(url, body, filename) {
        const loaderId = _showLoader('Generating PDF…');

        try {
            const response = await _fetchWithTimeout(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(body)
            });

            if (!response.ok) {
                const msg = await _extractErrorMessage(response);
                throw new Error(msg);
            }

            const blob = await response.blob();
            _triggerDownload(blob, filename);

        } catch (err) {
            _showError('PDF generation failed: ' + err.message);
            console.error('[PdfHelper] error:', err);
        } finally {
            _hideLoader(loaderId);
        }
    }

    function _triggerDownload(blob, filename) {
        const url  = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href     = url;
        link.download = filename;
        link.style.display = 'none';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        // Revoke after a short delay to allow Firefox to process the download
        setTimeout(() => URL.revokeObjectURL(url), 1000);
    }

    async function _fetchWithTimeout(url, options) {
        const controller = new AbortController();
        const timerId    = setTimeout(() => controller.abort(), DEFAULT_TIMEOUT_MS);

        try {
            return await fetch(url, { ...options, signal: controller.signal });
        } catch (err) {
            if (err.name === 'AbortError') throw new Error('Request timed out after 30s');
            throw err;
        } finally {
            clearTimeout(timerId);
        }
    }

    async function _extractErrorMessage(response) {
        try {
            const text = await response.text();
            const json = JSON.parse(text);
            return json.message || json.error || text;
        } catch {
            return `HTTP ${response.status} ${response.statusText}`;
        }
    }

    function _pageTitle() {
        return (document.title || 'document')
            .replace(/[^a-zA-Z0-9_\- ]/g, '')
            .trim()
            .replace(/\s+/g, '-')
            .toLowerCase();
    }

    // ── Loading overlay ───────────────────────────────────────────────────────
    // Uses Bootstrap toast if available, otherwise a lightweight fallback overlay.

    function _showLoader(message) {
        const id = 'pdf-loader-' + Date.now();

        // Try Bootstrap toast first (non-intrusive)
        if (typeof bootstrap !== 'undefined') {
            const toastHtml = `
                <div id="${id}" class="toast align-items-center text-bg-primary border-0 position-fixed bottom-0 end-0 m-3" role="status" style="z-index:9999">
                  <div class="d-flex">
                    <div class="toast-body d-flex align-items-center gap-2">
                      <span class="spinner-border spinner-border-sm" role="status"></span>
                      ${message}
                    </div>
                  </div>
                </div>`;
            document.body.insertAdjacentHTML('beforeend', toastHtml);
            const toastEl = document.getElementById(id);
            new bootstrap.Toast(toastEl, { autohide: false }).show();
            return id;
        }

        // Fallback: simple overlay div
        const overlay = document.createElement('div');
        overlay.id = id;
        overlay.style.cssText = [
            'position:fixed', 'inset:0', 'background:rgba(0,0,0,0.45)',
            'display:flex', 'align-items:center', 'justify-content:center',
            'z-index:9999', 'color:#fff', 'font-size:1.1rem', 'font-family:Arial,sans-serif'
        ].join(';');
        overlay.innerHTML = `
            <div style="background:#1a3a5c;padding:24px 36px;border-radius:8px;text-align:center;">
                <div style="margin-bottom:12px;">
                    <svg width="40" height="40" viewBox="0 0 40 40">
                        <circle cx="20" cy="20" r="16" fill="none" stroke="#fff" stroke-width="3"
                                stroke-dasharray="80" stroke-linecap="round">
                            <animateTransform attributeName="transform" type="rotate"
                                from="0 20 20" to="360 20 20" dur="1s" repeatCount="indefinite"/>
                        </circle>
                    </svg>
                </div>
                ${message}
            </div>`;
        document.body.appendChild(overlay);
        return id;
    }

    function _hideLoader(id) {
        const el = document.getElementById(id);
        if (!el) return;

        // Bootstrap toast cleanup
        if (typeof bootstrap !== 'undefined') {
            const instance = bootstrap.Toast.getInstance(el);
            if (instance) instance.hide();
            setTimeout(() => el.remove(), 500);
            return;
        }

        el.remove();
    }

    function _showError(message) {
        // Bootstrap alert toast
        if (typeof bootstrap !== 'undefined') {
            const id = 'pdf-error-' + Date.now();
            const html = `
                <div id="${id}" class="toast align-items-center text-bg-danger border-0 position-fixed bottom-0 end-0 m-3" role="alert" style="z-index:9999">
                  <div class="d-flex">
                    <div class="toast-body">${message}</div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
                  </div>
                </div>`;
            document.body.insertAdjacentHTML('beforeend', html);
            const el = document.getElementById(id);
            new bootstrap.Toast(el, { delay: 6000 }).show();
            el.addEventListener('hidden.bs.toast', () => el.remove());
            return;
        }

        // Fallback: alert
        alert('⚠️ ' + message);
    }

    // ── Public surface ────────────────────────────────────────────────────────

    return { downloadFromHtml, downloadFromDiv, downloadFromUrl };

})();

// Make available as a module export if bundled (optional)
if (typeof module !== 'undefined' && module.exports) {
    module.exports = PdfHelper;
}
