-- The Konva whiteboard editor stays; OnlyOffice backs NOTES instead.

-- Whiteboards: restore the columns the Konva editor persists to.
ALTER TABLE whiteboards ADD COLUMN content TEXT;
ALTER TABLE whiteboards ADD COLUMN preview_image_url TEXT;
ALTER TABLE whiteboards DROP COLUMN document_key;

-- Notes: OnlyOffice-edited .docx stored on disk (see NoteFileStorage).
-- content stays for the mobile (Quill) note editor.
ALTER TABLE notes ADD COLUMN document_key VARCHAR(100) NOT NULL DEFAULT md5(random()::text || clock_timestamp()::text);
ALTER TABLE notes ALTER COLUMN document_key DROP DEFAULT;
