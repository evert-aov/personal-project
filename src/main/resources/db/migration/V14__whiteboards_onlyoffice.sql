-- Whiteboards are now backed by an OnlyOffice-edited .docx file stored on
-- disk (see WhiteboardFileStorage) instead of Konva JSON/base64 preview data.
ALTER TABLE whiteboards DROP COLUMN content;
ALTER TABLE whiteboards DROP COLUMN preview_image_url;

ALTER TABLE whiteboards ADD COLUMN document_key VARCHAR(100) NOT NULL DEFAULT md5(random()::text || clock_timestamp()::text);
ALTER TABLE whiteboards ALTER COLUMN document_key DROP DEFAULT;
