package com.uagrm.personal.note.service;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import jakarta.annotation.PostConstruct;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Stores the file backing each note in Azure Blob Storage (or on disk as fallback),
 * keyed by "{noteId}.{fileType}".
 * The note's file_type column remains the source of truth for Content-Type/OnlyOffice
 * config; the extension is just for readability. OnlyOffice Document Server
 * fetches/edits these through NoteController.
 */
@Service
public class NoteFileStorage {

    private static final Logger log = LoggerFactory.getLogger(NoteFileStorage.class);

    @Value("${app.storage.notes-dir:./storage/notes}")
    private String storageDir;

    @Value("${azure.storage.connection-string:}")
    private String connectionString;

    @Value("${azure.storage.container-name:notes}")
    private String containerName;

    private BlobContainerClient blobContainerClient;

    @PostConstruct
    public void init() {
        if (connectionString != null && !connectionString.trim().isEmpty()) {
            try {
                BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                        .connectionString(connectionString.trim())
                        .buildClient();
                this.blobContainerClient = serviceClient.getBlobContainerClient(containerName);
                this.blobContainerClient.createIfNotExists();
                log.info("Azure Blob Storage initialized successfully for container '{}'.", containerName);
            } catch (Exception e) {
                log.error("Could not initialize Azure Blob Storage: {}", e.getMessage(), e);
            }
        } else {
            log.info("Azure Storage connection string is not configured. Falling back to local disk storage.");
        }
    }

    public void createBlankDocument(Long noteId, String title, String fileType) {
        byte[] blank = switch (fileType) {
            case "docx" -> blankDocx();
            case "xlsx" -> blankXlsx();
            case "odt" -> blankOpenDocument("odt", "application/vnd.oasis.opendocument.text", ODT_CONTENT_XML);
            case "ods" -> blankOpenDocument("ods", "application/vnd.oasis.opendocument.spreadsheet", ODS_CONTENT_XML);
            case "pdf" -> blankPdf();
            default -> throw new IllegalArgumentException("Unsupported note file type: " + fileType);
        };
        write(noteId, title, fileType, blank);
    }

    public byte[] read(Long noteId, String title, String fileType) {
        String filename = blobName(noteId, title, fileType);
        String legacyFilename = legacyBlobName(noteId, fileType);

        if (blobContainerClient != null) {
            try {
                BlobClient blobClient = blobContainerClient.getBlobClient(filename);
                if (blobClient.exists()) {
                    return blobClient.downloadContent().toBytes();
                }
                BlobClient legacyBlobClient = blobContainerClient.getBlobClient(legacyFilename);
                if (legacyBlobClient.exists()) {
                    byte[] data = legacyBlobClient.downloadContent().toBytes();
                    blobClient.upload(BinaryData.fromBytes(data), true);
                    legacyBlobClient.delete();
                    return data;
                }
                return blobClient.downloadContent().toBytes();
            } catch (Exception e) {
                throw new RuntimeException("Could not read note document " + noteId + " from Azure Storage", e);
            }
        }

        try {
            Path currentPath = pathFor(filename);
            if (Files.exists(currentPath)) {
                return Files.readAllBytes(currentPath);
            }
            Path legacyPath = pathFor(legacyFilename);
            if (Files.exists(legacyPath)) {
                byte[] data = Files.readAllBytes(legacyPath);
                Files.createDirectories(currentPath.getParent());
                Files.write(currentPath, data);
                Files.deleteIfExists(legacyPath);
                return data;
            }
            return Files.readAllBytes(currentPath);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read note document " + noteId, e);
        }
    }

    public void write(Long noteId, String title, String fileType, byte[] content) {
        String filename = blobName(noteId, title, fileType);
        if (blobContainerClient != null) {
            try {
                BlobClient blobClient = blobContainerClient.getBlobClient(filename);
                blobClient.upload(BinaryData.fromBytes(content), true);
                return;
            } catch (Exception e) {
                throw new RuntimeException("Could not write note document " + noteId + " to Azure Storage", e);
            }
        }

        try {
            Files.createDirectories(Path.of(storageDir));
            Files.write(pathFor(filename), content);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not write note document " + noteId, e);
        }
    }

    public void delete(Long noteId, String title, String fileType) {
        String filename = blobName(noteId, title, fileType);
        String legacyFilename = legacyBlobName(noteId, fileType);

        if (blobContainerClient != null) {
            try {
                blobContainerClient.getBlobClient(filename).deleteIfExists();
                blobContainerClient.getBlobClient(legacyFilename).deleteIfExists();
                return;
            } catch (Exception e) {
                throw new RuntimeException("Could not delete note document " + noteId + " from Azure Storage", e);
            }
        }

        try {
            Files.deleteIfExists(pathFor(filename));
            Files.deleteIfExists(pathFor(legacyFilename));
        } catch (IOException e) {
            throw new UncheckedIOException("Could not delete note document " + noteId, e);
        }
    }

    public void rename(Long noteId, String oldTitle, String newTitle, String fileType) {
        String oldFilename = blobName(noteId, oldTitle, fileType);
        String newFilename = blobName(noteId, newTitle, fileType);

        if (oldFilename.equals(newFilename)) {
            return;
        }

        try {
            byte[] content = read(noteId, oldTitle, fileType);
            write(noteId, newTitle, fileType, content);
            delete(noteId, oldTitle, fileType);
        } catch (Exception e) {
            log.warn("Could not rename note document {} from '{}' to '{}': {}", noteId, oldFilename, newFilename, e.getMessage());
        }
    }

    private String blobName(Long noteId, String title, String fileType) {
        if (title == null || title.isBlank()) {
            return legacyBlobName(noteId, fileType);
        }
        String safeTitle = title.trim().replaceAll("[\\\\/:*?\"<>|]", "_");
        return noteId + "_" + safeTitle + "." + fileType;
    }

    private String legacyBlobName(Long noteId, String fileType) {
        return noteId + "." + fileType;
    }

    private Path pathFor(String filename) {
        return Path.of(storageDir, filename);
    }

    private byte[] blankDocx() {
        try (XWPFDocument document = new XWPFDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            document.createParagraph();
            document.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Could not create blank docx", e);
        }
    }

    private byte[] blankXlsx() {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.createSheet("Sheet1");
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Could not create blank xlsx", e);
        }
    }

    private byte[] blankPdf() {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            document.addPage(new PDPage());
            document.save(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Could not create blank pdf", e);
        }
    }

    private static final String ODT_CONTENT_XML = """
            <?xml version="1.0" encoding="UTF-8"?>
            <office:document-content xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0"
                xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0" office:version="1.2">
              <office:body>
                <office:text>
                  <text:p></text:p>
                </office:text>
              </office:body>
            </office:document-content>
            """;

    private static final String ODS_CONTENT_XML = """
            <?xml version="1.0" encoding="UTF-8"?>
            <office:document-content xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0"
                xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0" office:version="1.2">
              <office:body>
                <office:spreadsheet>
                  <table:table table:name="Sheet1">
                    <table:table-column table:number-columns-repeated="10"/>
                    <table:table-row table:number-rows-repeated="20">
                      <table:table-cell table:number-columns-repeated="10"/>
                    </table:table-row>
                  </table:table>
                </office:spreadsheet>
              </office:body>
            </office:document-content>
            """;

    /**
     * Hand-rolls a minimal, spec-valid ODF package (ODT/ODS are just ZIP files with a
     * fixed structure) instead of pulling in a whole ODF library for two static files.
     * The "mimetype" entry MUST be first and stored uncompressed per the ODF spec.
     */
    private byte[] blankOpenDocument(String extension, String mimeType, String contentXml) {
        String manifestXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <manifest:manifest xmlns:manifest="urn:oasis:names:tc:opendocument:xmlns:manifest:1.0" manifest:version="1.2">
                  <manifest:file-entry manifest:full-path="/" manifest:version="1.2" manifest:media-type="%s"/>
                  <manifest:file-entry manifest:full-path="content.xml" manifest:media-type="text/xml"/>
                </manifest:manifest>
                """.formatted(mimeType);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); ZipOutputStream zip = new ZipOutputStream(out, StandardCharsets.UTF_8)) {
            writeStoredEntry(zip, "mimetype", mimeType.getBytes(StandardCharsets.UTF_8));

            zip.putNextEntry(new ZipEntry("META-INF/manifest.xml"));
            zip.write(manifestXml.getBytes(StandardCharsets.UTF_8));
            zip.closeEntry();

            zip.putNextEntry(new ZipEntry("content.xml"));
            zip.write(contentXml.getBytes(StandardCharsets.UTF_8));
            zip.closeEntry();

            zip.finish();
            return out.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Could not create blank " + extension, e);
        }
    }

    private void writeStoredEntry(ZipOutputStream zip, String name, byte[] content) throws IOException {
        ZipEntry entry = new ZipEntry(name);
        entry.setMethod(ZipEntry.STORED);
        entry.setSize(content.length);
        entry.setCompressedSize(content.length);
        CRC32 crc = new CRC32();
        crc.update(content);
        entry.setCrc(crc.getValue());
        zip.putNextEntry(entry);
        zip.write(content);
        zip.closeEntry();
    }
}
