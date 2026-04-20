-- === Caso 1 ===

DROP TABLE RECAUDACION_BONOS_MEDICOS;

CREATE TABLE RECAUDACION_BONOS_MEDICOS (
    RUT_MEDICO      VARCHAR2(15),
    NOMBRE_MEDICO   VARCHAR2(80),
    TOTAL_RECAUDADO NUMBER(10),
    UNIDAD_MEDICA   VARCHAR2(40)
);

INSERT INTO RECAUDACION_BONOS_MEDICOS (RUT_MEDICO, NOMBRE_MEDICO, TOTAL_RECAUDADO, UNIDAD_MEDICA)
SELECT
    TO_CHAR(m.rut_med, 'FM99G999G999') || '-' || m.dv_run     AS RUT_MEDICO,
    m.pnombre || ' ' || m.apaterno || ' ' || m.amaterno       AS NOMBRE_MEDICO,
    ROUND(SUM(bc.costo))                                      AS TOTAL_RECAUDADO,
    uc.nombre                                                 AS UNIDAD_MEDICA
FROM
    MEDICO                    m
    JOIN DET_ESPECIALIDAD_MED dem ON dem.rut_med = m.rut_med
    JOIN BONO_CONSULTA        bc  ON bc.rut_med  = m.rut_med
                                  AND bc.esp_id   = dem.esp_id
    JOIN UNIDAD_CONSULTA      uc  ON uc.uni_id   = m.uni_id
WHERE
    EXTRACT(YEAR FROM bc.fecha_bono) = EXTRACT(YEAR FROM SYSDATE) - 1
    AND m.car_id NOT IN (100, 500, 600)
GROUP BY
    m.rut_med, m.dv_run, m.pnombre, m.apaterno, m.amaterno, uc.nombre
ORDER BY
    TOTAL_RECAUDADO ASC;

SELECT * FROM RECAUDACION_BONOS_MEDICOS;


-- === Caso 2 ===

SELECT
    em.nombre                                                  AS ESPECIALIDAD_MEDICA,
    COUNT(bc.id_bono)                                          AS CANTIDAD_BONOS,
    '$' || TO_CHAR(ROUND(SUM(bc.costo)), 'FM999G999')          AS MONTO_PERDIDA,
    TO_CHAR(MIN(bc.fecha_bono), 'DD-MM-YYYY')                  AS FECHA_BONO,
    CASE
        WHEN EXTRACT(YEAR FROM MIN(bc.fecha_bono)) >= EXTRACT(YEAR FROM SYSDATE) - 1
        THEN 'COBRABLE'
        ELSE 'INCOBRABLE'
        END                                                    AS ESTADO_DE_COBRO
FROM
    BONO_CONSULTA            bc
    JOIN ESPECIALIDAD_MEDICA em ON em.esp_id = bc.esp_id
WHERE
    bc.id_bono IN (
        SELECT id_bono FROM BONO_CONSULTA
        MINUS
        SELECT id_bono FROM PAGOS
    )
GROUP BY
    em.nombre
ORDER BY
    COUNT(bc.id_bono) ASC,
    SUM(bc.costo) DESC;


-- === Caso 3 ===

DELETE FROM CANT_BONOS_PACIENTES_ANNIO;

INSERT INTO CANT_BONOS_PACIENTES_ANNIO (
    ANNIO_CALCULO, PAC_RUN, DV_RUN, EDAD,
    CANTIDAD_BONOS, MONTO_TOTAL_BONOS, SISTEMA_SALUD
)
SELECT
    EXTRACT(YEAR FROM SYSDATE),
    p.pac_run,
    p.dv_run,
    ROUND(MONTHS_BETWEEN(SYSDATE, p.fecha_nacimiento) / 12),
    NVL(COUNT(bc.id_bono), 0),
    NVL(ROUND(SUM(bc.costo)), 0),
    UPPER(ss.descripcion)                                      -- para que el ouput este todo en mayusculas y mas ordenado
FROM
    PACIENTE           p
    JOIN SALUD         sa ON sa.sal_id      = p.sal_id
    JOIN SISTEMA_SALUD ss ON ss.tipo_sal_id = sa.tipo_sal_id
    LEFT JOIN BONO_CONSULTA bc ON bc.pac_run = p.pac_run
                               AND EXTRACT(YEAR FROM bc.fecha_bono) = EXTRACT(YEAR FROM SYSDATE) - 1
WHERE
    sa.tipo_sal_id IN ('F', 'P', 'FA')
GROUP BY
    p.pac_run, p.dv_run, p.fecha_nacimiento, ss.descripcion
HAVING
    NVL(COUNT(bc.id_bono), 0) <= (
        SELECT ROUND(AVG(total_pac))
        FROM (
            SELECT COUNT(id_bono) AS total_pac
            FROM BONO_CONSULTA
            WHERE EXTRACT(YEAR FROM fecha_bono) = EXTRACT(YEAR FROM SYSDATE) - 1
            GROUP BY pac_run
        )
    )
ORDER BY
    NVL(ROUND(SUM(bc.costo)), 0) ASC,
    ROUND(MONTHS_BETWEEN(SYSDATE, p.fecha_nacimiento) / 12) DESC;

SELECT * FROM CANT_BONOS_PACIENTES_ANNIO;

COMMIT;