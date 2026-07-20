-- Ensure roles.id has an auto-incrementing sequence set as default
CREATE SEQUENCE IF NOT EXISTS roles_id_seq OWNED BY roles.id;
ALTER TABLE roles ALTER COLUMN id SET DEFAULT nextval('roles_id_seq');
SELECT setval('roles_id_seq', GREATEST((SELECT COALESCE(MAX(id), 0) FROM roles), 1));
