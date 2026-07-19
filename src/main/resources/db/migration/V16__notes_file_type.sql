-- Notes can now be created as docx/odt (word), xlsx/ods (spreadsheet) or pdf,
-- edited through OnlyOffice. Existing notes were all created as docx.
ALTER TABLE notes ADD COLUMN file_type VARCHAR(10) NOT NULL DEFAULT 'docx';
ALTER TABLE notes ALTER COLUMN file_type DROP DEFAULT;
