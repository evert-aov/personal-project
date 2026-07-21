-- Seed SUPERUSER and USER roles
INSERT INTO roles (name, description, level, is_active)
SELECT 'SUPERUSER', 'Superusuario con acceso total a todos los módulos y endpoints', 1000, TRUE
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'SUPERUSER');

INSERT INTO roles (name, description, level, is_active)
SELECT 'USER', 'Usuario cliente con acceso a la parte del cliente de finanzas, taller y dashboard', 1, TRUE
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'USER');

SELECT setval('roles_id_seq', GREATEST((SELECT COALESCE(MAX(id), 0) FROM roles), 1));
