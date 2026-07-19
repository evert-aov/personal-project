-- Seeds the roles referenced by hardcoded ids in the web/mobile clients
-- (role_id 1 = ADMIN, 2 = TEACHER). Without these rows, role assignment on
-- user creation/update silently drops the role (JpaRepository#findAllById
-- ignores ids that don't exist instead of failing), so no user could ever
-- end up with the ADMIN role and admin-only UI never appears.
INSERT INTO roles (id, name, description, level, is_active) VALUES
    (1, 'ADMIN', 'Administrador con acceso total al sistema', 100, TRUE),
    (2, 'TEACHER', 'Usuario estandar con acceso a finanzas y horarios', 10, TRUE)
ON CONFLICT (id) DO NOTHING;
