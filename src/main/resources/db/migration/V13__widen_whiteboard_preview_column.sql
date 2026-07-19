-- The web whiteboard editor stores a small base64 PNG data URI as the preview
-- thumbnail, which doesn't fit in VARCHAR(255).
ALTER TABLE whiteboards ALTER COLUMN preview_image_url TYPE TEXT;
