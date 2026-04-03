package it.mapsgroup.gzoom.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import it.mapsgroup.gzoom.mybatis.dao.PartyContentDao;
import it.mapsgroup.gzoom.mybatis.dto.Party;
import it.mapsgroup.gzoom.mybatis.dto.PartyContentEx;
import it.mapsgroup.gzoom.mybatis.dto.QueryConfig;
import it.mapsgroup.gzoom.mybatis.dto.UserPreference;
import it.mapsgroup.gzoom.mybatis.dto.UserLogin;
import it.mapsgroup.gzoom.util.pdf.ConvertPDFtoA3;
import it.mapsgroup.gzoom.util.pdf.PDFA3Components;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Leonardo Minaudo
 */
@Service
public class Html2PdfService {
    private static final Logger LOG = getLogger(Html2PdfService.class);
    private final ConfigurationImpl configuration;
    private String headHTML;
    private String bodyHTML;

    private String structureHtml =
            "<!DOCTYPE html >\n" +
                    "<html>" +
                    "   <head></head>" +
                    "   <body></body>" +
                    "</html>";
    private final UserPreferenceService userPreferenceService;
    private final PartyService partyService;
    private final PartyContentDao partyContentDao;

    private static final boolean IS_WINDOWS = (System.getProperty("os.name").toLowerCase().contains("win"));

    @Autowired
    public Html2PdfService(ConfigurationImpl configuration, UserPreferenceService userPreferenceService, PartyService partyService, PartyContentDao partyContentDao) {
        this.configuration = configuration;
        this.userPreferenceService = userPreferenceService;
        this.partyService = partyService;
        this.partyContentDao = partyContentDao;
    }

    public String htmlToPdfSave(Workbook wb, QueryConfig query, UserLogin userLogin, String outputPath) throws Exception {
        Path dirPath = Paths.get(outputPath.substring(0, outputPath.lastIndexOf("/")));
        if (!Files.exists(dirPath)) {
            Files.createDirectory(dirPath);
        }

        outputPath = convertHtmlToPdf(wb, query, userLogin, outputPath);
        try {
            PDFA3Components pdfa3Components = new PDFA3Components(outputPath, outputPath);
            ConvertPDFtoA3 converter = new ConvertPDFtoA3();
            PDDocument pdfa = converter.ConvertPdfaPDDocument(pdfa3Components);
            pdfa.save(outputPath);
            pdfa.close();
        } catch (Exception e ){
            LOG.error("Error convert htmlToPdf {}", e);
        }
        return outputPath;
    }

    public HttpServletResponse htmlToPdf(HttpServletResponse response, Workbook wb, QueryConfig query, UserLogin userLogin, String outputPath) {
        try {
            outputPath = this.convertHtmlToPdf(wb, query, userLogin, outputPath);
            PDFA3Components pdfa3Components = new PDFA3Components(outputPath, outputPath);
            ConvertPDFtoA3 converter = new ConvertPDFtoA3();
            PDDocument pdfa = converter.ConvertPdfaPDDocument(pdfa3Components);
            pdfa.save(response.getOutputStream());
            pdfa.close();

            File myObj = new File(outputPath);
            myObj.delete();
            if (myObj.delete()) {
                LOG.info("Deleted the file: " + myObj.getName());
            } else {
                LOG.info("Failed to delete the file.");
            }
        } catch (Exception e) {
            LOG.error("Error convert htmlToPdf {}", e);
        }

        return response;
    }

    public String convertHtmlToPdf(Workbook wb,
                                   QueryConfig query,
                                   UserLogin userLogin,
                                   String outputPath) throws Exception {

        // 1️⃣ Costruzione HTML con Jsoup
        Document document = Jsoup.parse(structureHtml, "UTF-8");

        // Aggiungo header e footer HTML con logo base64
        setHeadFooterHTML(document, query.getOrientation(), query.getQueryName(), (userLogin != null) ? userLogin.getUsername() : principal().getUsername());

        // Corpo con dati
        setBodyHTML(document, wb);

        LOG.debug(document.html());

        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        try (OutputStream outputStream = new FileOutputStream(outputPath)) {

            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);

            // BaseUri = file:/ per risolvere eventuali immagini locali
            renderer.setDocumentFromString(document.html(), "file:/");
            renderer.layout();

            renderer.setPDFVersion(PdfWriter.VERSION_1_7);
            renderer.createPDF(outputStream);

            return outputPath;
        }
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }

    private String splitTitle(String title) {
        if (title == null) return "";

        int maxLen = 35; // soglia sicura Flying Saucer
        if (title.length() <= maxLen) return title;

        int split = title.lastIndexOf(' ', maxLen);
        if (split == -1) split = maxLen;

        return title.substring(0, split) + "<br/>" + title.substring(split + 1);
    }

    // -------------------------------------
    // Header/Footer HTML
    // -------------------------------------
    private void setHeadFooterHTML(Document document,
                                   String orientation,
                                   String queryName,
                                   String username) {

        // ---- Party name ----
        String partyName = "";
        UserPreference company =
                userPreferenceService.getUserPreferenceByUsername("ORGANIZATION_PARTY", username);
        Party party =
                partyService.findByPartyId(company != null ? company.getUserPrefValue() : "Company");
        if (party != null) partyName = party.getPartyName();

        // ---- Logo base64 ----
        String base64Logo = "";
        int logoWidth = 32;
        int logoHeight = 32;

        try {
            String pathLogo = getPathLogo();
            if (pathLogo != null && !pathLogo.isEmpty()) {
                byte[] bytes = Files.readAllBytes(new File(pathLogo).toPath());
                base64Logo = Base64.getEncoder().encodeToString(bytes);

                BufferedImage img = ImageIO.read(new File(pathLogo));
                if (img != null) {
                    logoWidth = img.getWidth();
                    logoHeight = img.getHeight();
                }
            }
        } catch (Exception e) {
            LOG.warn("Logo not available", e);
        }

        String now = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());

        // ---- CSS con @page margin boxes ----
        String head =
                "<style>\n" +
                        "@page {\n" +
                        "  size: A4 " + ("VERTICAL".equals(orientation) ? "" : "landscape") + ";\n" +
                        "  margin-top: 12%;\n" +
                        "  margin-bottom: 10%;\n" +
                        "  margin-left: 5%;\n" +
                        "  margin-right: 5%;\n" +

                        "  @top-center {\n" +
                        "    content: element(pageHeader);\n" +
                        "  }\n" +

                        "  @bottom-left {\n" +
                        "    content: \"" + now + "\";\n" +
                        "    font-family: Helvetica;\n" +
                        "    font-size: 8pt;\n" +
                        "  }\n" +

                        "  @bottom-right {\n" +
                        "    content: \"Pag. \" counter(page);\n" +
                        "    font-family: Helvetica;\n" +
                        "    font-size: 8pt;\n" +
                        "  }\n" +
                        "}\n" +

                        "body { font-family: Helvetica; }\n" +
                        "</style>\n";

        String headerHtml = getHeaderHtml(queryName, base64Logo, partyName);


        document.head().html(head);
        document.body().prepend(headerHtml);
    }

    private @NonNull String getHeaderHtml(String queryName, String base64Logo, String partyName) {
        boolean hasLogo = base64Logo != null && !base64Logo.isEmpty();

        String headerHtml =
                "<div id='pageHeader' style='position: running(pageHeader); width:100%;'>"
                        + "  <table style='width:100%; table-layout: fixed; border-collapse: collapse;'>"
                        + "    <tr>";

        if (hasLogo) {
            String safeTitle = splitTitle(escape(queryName));
            // --------- 3 COLONNE (CON LOGO) ---------
            headerHtml +=
                    // SINISTRA
                    "<td style='width:35%;"
                            + "           text-align:left;"
                            + "           font-family:Helvetica;"
                            + "           font-size:10pt;"
                            + "           white-space:nowrap;"
                            + "           overflow:hidden;"
                            + "           text-overflow:ellipsis;'>"
                            + escape(partyName)
                            + "</td>"

                            // CENTRO (LOGO)
                            + "<td style='width:30%; text-align:center; vertical-align:middle;'>"
                            + "<img src='data:image/png;base64," + base64Logo + "' "
                            + "style='max-height:40px; display:inline-block;' />"
                            + "</td>"

                            // DESTRA
                            + "<td style='width:35%;"
                            + "           text-align:right;"
                            + "           font-family:Helvetica;"
                            + "           font-size:10pt;"
                            + "           white-space:nowrap;"
                            + "           overflow:hidden;"
                            + "           text-overflow:ellipsis;'>"
                            + safeTitle
                            + "</td>";
        } else {
            // --------- 2 COLONNE (SENZA LOGO) ---------
            headerHtml +=
                    // SINISTRA
                    "<td style='width:40%;"
                            + "           text-align:left;"
                            + "           font-family:Helvetica;"
                            + "           font-size:10pt;"
                            + "           white-space:nowrap;"
                            + "           overflow:hidden;"
                            + "           text-overflow:ellipsis;'>"
                            + escape(partyName)
                            + "</td>"

                            // DESTRA (USA TUTTO LO SPAZIO)
                            + "<td style='width:60%;"
                            + "           text-align:right;"
                            + "           font-family:Helvetica;"
                            + "           font-size:10pt;'>"
                            + escape(queryName)
                            + "</td>";
        }

        headerHtml +=
                "    </tr>"
                        + "  </table>"
                        + "</div>";
        return headerHtml;
    }

    // -------------------------------------
// Corpo del documento con div .content
// -------------------------------------
    private void setBodyHTML(Document document, Workbook wb) {

        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Row> rowIterator = wb.getSheetAt(0).rowIterator();
        int i = 1, posHtml = 0, pageBreak = 0;

        if (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String str = cell.toString().toLowerCase();
                if (str.equals("html_text")) posHtml = i;
                if (str.equals("page_break")) pageBreak = i;
                i++;
            }
        }

        boolean firstRow = true;

        // Wrapper div content con margini per header/footer
        //bodyBuilder.append("<div class='content'>\n");

        if (posHtml != 0 && pageBreak != 0) {
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell cellHtml = row.getCell(posHtml - 1);
                Cell cellBreak = row.getCell(pageBreak - 1);

                if (!firstRow && cellBreak != null && "Y".equals(cellBreak.toString())) {
                    bodyBuilder.append("<div style='page-break-after: always'></div>\n");
                }

                bodyBuilder.append(cellHtml != null ? cellHtml.toString() : "");
                firstRow = false;
            }
        }

        //bodyBuilder.append("</div>\n");

        document.body().append(bodyBuilder.toString());
    }


    private String getPathLogo() {
        String path = "";
        PartyContentEx partyContentEx = partyContentDao.getPartyContent("Company", "REPORT_SMALL");
        if (partyContentEx != null) {
            path = partyContentEx.getDataResource().getObjectInfo();
        }
        return path;
    }


}
