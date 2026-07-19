-- Users are now identified by a manually-assigned username instead of an
-- auto-generated numeric code. Existing numeric codes are preserved as text
-- so current accounts keep working.
ALTER TABLE users RENAME COLUMN code TO username;
ALTER TABLE users ALTER COLUMN username TYPE VARCHAR(50) USING username::varchar;
