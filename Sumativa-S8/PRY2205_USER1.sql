
-- sinonimos caso 1
 
CREATE OR REPLACE SYNONYM syn_bono_consulta      FOR PRY2205_USER1.BONO_CONSULTA;
CREATE OR REPLACE SYNONYM syn_cant_bonos         FOR PRY2205_USER1.CANT_BONOS_PACIENTES_ANNIO;
CREATE OR REPLACE SYNONYM syn_cargo              FOR PRY2205_USER1.CARGO;
CREATE OR REPLACE SYNONYM syn_det_esp_med        FOR PRY2205_USER1.DET_ESPECIALIDAD_MED;
CREATE OR REPLACE SYNONYM syn_esp_medica         FOR PRY2205_USER1.ESPECIALIDAD_MEDICA;
CREATE OR REPLACE SYNONYM syn_medico             FOR PRY2205_USER1.MEDICO;
CREATE OR REPLACE SYNONYM syn_paciente           FOR PRY2205_USER1.PACIENTE;
CREATE OR REPLACE SYNONYM syn_pagos              FOR PRY2205_USER1.PAGOS;
CREATE OR REPLACE SYNONYM syn_pct_descto         FOR PRY2205_USER1.PCT_DESCTO_ADULTO_MAYOR;
CREATE OR REPLACE SYNONYM syn_salud              FOR PRY2205_USER1.SALUD;
CREATE OR REPLACE SYNONYM syn_sistema_salud      FOR PRY2205_USER1.SISTEMA_SALUD;
CREATE OR REPLACE SYNONYM syn_unidad_consulta    FOR PRY2205_USER1.UNIDAD_CONSULTA;
 
--sinonimos publicos para que el user 2 pueda acceder
CREATE OR REPLACE PUBLIC SYNONYM pub_bono_consulta   FOR PRY2205_USER1.BONO_CONSULTA;
CREATE OR REPLACE PUBLIC SYNONYM pub_cant_bonos      FOR PRY2205_USER1.CANT_BONOS_PACIENTES_ANNIO;
CREATE OR REPLACE PUBLIC SYNONYM pub_cargo           FOR PRY2205_USER1.CARGO;
CREATE OR REPLACE PUBLIC SYNONYM pub_det_esp_med     FOR PRY2205_USER1.DET_ESPECIALIDAD_MED;
CREATE OR REPLACE PUBLIC SYNONYM pub_esp_medica      FOR PRY2205_USER1.ESPECIALIDAD_MEDICA;
CREATE OR REPLACE PUBLIC SYNONYM pub_medico          FOR PRY2205_USER1.MEDICO;
CREATE OR REPLACE PUBLIC SYNONYM pub_paciente        FOR PRY2205_USER1.PACIENTE;
CREATE OR REPLACE PUBLIC SYNONYM pub_pagos           FOR PRY2205_USER1.PAGOS;
CREATE OR REPLACE PUBLIC SYNONYM pub_pct_descto      FOR PRY2205_USER1.PCT_DESCTO_ADULTO_MAYOR;
CREATE OR REPLACE PUBLIC SYNONYM pub_salud           FOR PRY2205_USER1.SALUD;
CREATE OR REPLACE PUBLIC SYNONYM pub_sistema_salud   FOR PRY2205_USER1.SISTEMA_SALUD;
CREATE OR REPLACE PUBLIC SYNONYM pub_unidad_consulta FOR PRY2205_USER1.UNIDAD_CONSULTA;
 
-- se otorga select sobre las tablas necesarias al rol_p
-- principio de menor privilegio, solo las tablas estrictamente necesarias para el caso 2 
GRANT SELECT ON PRY2205_USER1.BONO_CONSULTA     TO PRY2205_ROL_P;
GRANT SELECT ON PRY2205_USER1.PACIENTE          TO PRY2205_ROL_P;
GRANT SELECT ON PRY2205_USER1.SALUD             TO PRY2205_ROL_P;
GRANT SELECT ON PRY2205_USER1.SISTEMA_SALUD     TO PRY2205_ROL_P;
GRANT SELECT ON PRY2205_USER1.PAGOS             TO PRY2205_ROL_P;


-- caso 3.1
 
CREATE OR REPLACE VIEW VW_AUM_MEDICO_X_CARGO AS
SELECT
    -- rut con digito verificador
    TO_CHAR(m.rut_med) || '-' || m.dv_run                          AS "RUT_MEDICO",
    c.nombre                                                         AS "CARGO",
    ROUND(m.sueldo_base)                                             AS "SUELDO_ACTUAL",
    ROUND(m.sueldo_base * 1.15)                                      AS "SUELDO_AUMENTADO"
FROM
    syn_medico m
    JOIN syn_cargo c ON m.car_id = c.car_id
WHERE
    UPPER(c.nombre) LIKE '%ATENCION%'
    OR UPPER(c.nombre) LIKE '%ATENCIÓN%'
ORDER BY
    ROUND(m.sueldo_base * 1.15) DESC;
 
-- verificador de vista
SELECT * FROM VW_AUM_MEDICO_X_CARGO;


-- caso 3.2: se crean indices para mejorar el plan de ejecucion
 
CREATE INDEX idx_medico_car_id
    ON PRY2205_USER1.MEDICO (car_id);
 
CREATE INDEX idx_cargo_nombre_upper
    ON PRY2205_USER1.CARGO (UPPER(nombre));
 
-- ejecutar aqui para comparar el plan con indice
EXPLAIN PLAN FOR SELECT * FROM VW_AUM_MEDICO_X_CARGO;
SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY);


