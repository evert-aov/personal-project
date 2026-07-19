package com.uagrm.personal.note.service;

import com.uagrm.personal.security.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Builds the JWT-signed editor config OnlyOffice's DocsAPI needs, and verifies
 * the JWT OnlyOffice Document Server attaches to its save callback. Generic
 * over whichever resource is backed by an OnlyOffice-edited file (currently
 * just notes) -- pass its REST path segment, id, title, document key and file type.
 * <p>
 * Note: unlike our own {@code jwt.secret} (base64, see JwtService), OnlyOffice's
 * convention is to use the configured JWT_SECRET string as the raw HMAC key
 * bytes directly -- it must NOT be base64-decoded, or Document Server will
 * reject every token signed here as an invalid signature.
 */
@Service
public class OnlyOfficeService {

    @Value("${onlyoffice.jwt.secret}")
    private String jwtSecret;

    @Value("${onlyoffice.callback-base-url}")
    private String callbackBaseUrl;

    public Map<String, Object> buildEditorConfig(String resourcePath, Long resourceId, String title, String documentKey, String fileType, User user) {
        Map<String, Object> document = new LinkedHashMap<>();
        document.put("fileType", fileType);
        document.put("key", documentKey);
        document.put("title", title + "." + fileType);
        document.put("url", resourceUrl(resourcePath, resourceId, "file"));

        Map<String, Object> editorUser = new LinkedHashMap<>();
        editorUser.put("id", user.getId().toString());
        editorUser.put("name", user.getName() + " " + user.getLastName());

        Map<String, Object> editorConfig = new LinkedHashMap<>();
        editorConfig.put("callbackUrl", resourceUrl(resourcePath, resourceId, "callback"));
        editorConfig.put("user", editorUser);
        editorConfig.put("lang", "es");

        Map<String, Object> config = new LinkedHashMap<>();
        config.put("document", document);
        config.put("documentType", documentTypeFor(fileType));
        config.put("editorConfig", editorConfig);

        String token = Jwts.builder()
                .claims(config)
                .signWith(getSignInKey())
                .compact();
        config.put("token", token);

        return config;
    }

    public boolean isValidToken(String token) {
        if (token == null || token.isBlank()) return false;
        try {
            Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * OnlyOffice groups every editable format under one of three editors.
     */
    private String documentTypeFor(String fileType) {
        return switch (fileType) {
            case "xlsx", "ods" -> "cell";
            case "pdf" -> "pdf";
            default -> "word";
        };
    }

    public String mimeTypeFor(String fileType) {
        return switch (fileType) {
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "odt" -> "application/vnd.oasis.opendocument.text";
            case "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ods" -> "application/vnd.oasis.opendocument.spreadsheet";
            case "pdf" -> "application/pdf";
            default -> "application/octet-stream";
        };
    }

    private String resourceUrl(String resourcePath, Long resourceId, String action) {
        return callbackBaseUrl + "/api/" + resourcePath + "/" + resourceId + "/" + action;
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
