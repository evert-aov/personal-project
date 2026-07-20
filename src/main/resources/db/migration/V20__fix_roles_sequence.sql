-- Sync the auto-increment sequence for roles table after manual seed insertions in V11
SELECT setval(pg_get_serial_sequence('roles', 'id'), COALESCE((SELECT MAX(id) FROM roles), 1));
