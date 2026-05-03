-- caso 1
-- se crean usuarios
 
CREATE USER PRY2205_USER1
    IDENTIFIED BY password1
    DEFAULT TABLESPACE users
    TEMPORARY TABLESPACE temp
    QUOTA UNLIMITED ON users;
 
CREATE USER PRY2205_USER2
    IDENTIFIED BY password2
    DEFAULT TABLESPACE users
    TEMPORARY TABLESPACE temp
    QUOTA UNLIMITED ON users;
 
-- se crean roles
CREATE ROLE PRY2205_ROL_D;
CREATE ROLE PRY2205_ROL_P;
 
-- se asignan los privilegios del sistema al rol_d para el user 1
-- el user 1 necesita crear tablas indices vistas sinonimos publicos y privados
GRANT CREATE SESSION        TO PRY2205_ROL_D;
GRANT CREATE TABLE          TO PRY2205_ROL_D;
GRANT CREATE INDEX          TO PRY2205_ROL_D;
GRANT CREATE VIEW           TO PRY2205_ROL_D;
GRANT CREATE SYNONYM        TO PRY2205_ROL_D;
GRANT CREATE PUBLIC SYNONYM TO PRY2205_ROL_D;
 
-- se asignan los privilegios del sistema al rol_p user 2
-- el user 2 necesita crear vistas, perfiles, usuarios y acceder via sinonimos publicos
GRANT CREATE SESSION  TO PRY2205_ROL_P;
GRANT CREATE VIEW     TO PRY2205_ROL_P;
GRANT CREATE PROFILE  TO PRY2205_ROL_P;
GRANT CREATE USER     TO PRY2205_ROL_P;
 
-- se asignan los roles a los usuarios
GRANT PRY2205_ROL_D TO PRY2205_USER1;
GRANT PRY2205_ROL_P TO PRY2205_USER2;

