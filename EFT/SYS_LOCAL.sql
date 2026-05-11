-- =============================================================================
--   caso 1: creacion de usuarios, roles, privilegios y sinonimos publicos
-- =============================================================================
 
-- 1.1 creacion de usuarios

-- dueño de las tablas
CREATE USER PRY2205_EFT IDENTIFIED BY "CSF_Dueno12"
    DEFAULT TABLESPACE USERS
    TEMPORARY TABLESPACE TEMP
    QUOTA 10M ON USERS;
 
-- desarrollador
CREATE USER PRY2205_EFT_DES IDENTIFIED BY "CSF_Developer34"
    DEFAULT TABLESPACE USERS
    TEMPORARY TABLESPACE TEMP
    QUOTA 10M ON USERS;
 
-- consultor
CREATE USER PRY2205_EFT_CON IDENTIFIED BY "CSF_Consultor56"
    DEFAULT TABLESPACE USERS
    TEMPORARY TABLESPACE TEMP
    QUOTA 10M ON USERS;
 
-- 1.2 se dan los privilegios al usuario dueno.
 
GRANT CREATE SESSION        TO PRY2205_EFT;
GRANT CREATE TABLE          TO PRY2205_EFT;
GRANT CREATE VIEW           TO PRY2205_EFT;
GRANT CREATE SEQUENCE       TO PRY2205_EFT;
GRANT CREATE SYNONYM        TO PRY2205_EFT;
GRANT CREATE PUBLIC SYNONYM TO PRY2205_EFT;
GRANT DROP PUBLIC SYNONYM   TO PRY2205_EFT;
 
-- 1.3 creacion de roles (principio de menor privilegio + reutilizacion)

CREATE ROLE PRY2205_ROL_D;  -- rol para el desarrollador 
CREATE ROLE PRY2205_ROL_C;  -- rol para el consultor 
 
-- 1.4 privilegios de sistema al usuario desarrollador (PRY2205_EFT_DES)
 
GRANT CREATE SESSION   TO PRY2205_EFT_DES;
GRANT CREATE SEQUENCE  TO PRY2205_EFT_DES;
GRANT CREATE PROCEDURE TO PRY2205_EFT_DES;
GRANT CREATE VIEW      TO PRY2205_EFT_DES;
 

-- 1.5 privilegios de sistema para el usuario consultor (PRY2205_EFT_CON)
-- solo necesita conectarse accede a datos via vistas/sinonimos

 
GRANT CREATE SESSION TO PRY2205_EFT_CON;
 
-- 1.6 asignacion de roles a los usuarios

GRANT PRY2205_ROL_D TO PRY2205_EFT_DES;
GRANT PRY2205_ROL_C TO PRY2205_EFT_CON;