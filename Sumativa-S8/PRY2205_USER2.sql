-- caso 2
--     pauta seguida segun el word de formato de respuesta
--   - consultas despues de las 17:15 del año anterior al año de ejecucion
--   - pacientes isapre y fonasa
--   - reajuste
--       entre 15.000 y 25.000 -> +15%
--       costo > 25.000        -> +20%
--       otro caso             -> se mantiene el costo
--   - ordenado por fecha_bono ASC y pac_run ASC
--   - accede mediante los sinonimos publicos q se crearon con el user1
--   - se siguen las columnas segun la figura 2 de la guia:
--     RUT_PACIENTE, NOMBRE_PACIENTE, SISTEMA_SALUD, COSTO,
--     HORARIO_ATENCION, FECHA_CONSULTA, REAJUSTE
 
CREATE OR REPLACE VIEW VW_RECALCULO_COSTOS AS
SELECT
    TO_CHAR(p.pac_run) || '-' || p.dv_run                                  AS "RUT_PACIENTE",
    INITCAP(p.pnombre) || ' ' || INITCAP(p.apaterno) || ' ' || INITCAP(p.snombre)
                                                                           AS "NOMBRE_PACIENTE",
    NVL(s.descripcion, 'Sin información')                                  AS "SISTEMA_SALUD",
    ROUND(bc.costo)                                                        AS "COSTO",
    bc.hr_consulta                                                         AS "HORARIO_ATENCION",
    TO_CHAR(bc.fecha_bono, 'MM-YYYY')                                      AS "FECHA_CONSULTA",
    ROUND(
        CASE
            WHEN bc.costo BETWEEN 15000 AND 25000  THEN bc.costo * 1.15
            WHEN bc.costo > 25000                  THEN bc.costo * 1.20
            ELSE                                        bc.costo
        END
    )                                                                      AS "REAJUSTE"
FROM
    pub_bono_consulta bc
    JOIN pub_paciente      p  ON bc.pac_run = p.pac_run
    JOIN pub_salud         s  ON p.sal_id   = s.sal_id
    JOIN pub_sistema_salud ss ON s.tipo_sal_id = ss.tipo_sal_id
WHERE
    EXTRACT(YEAR FROM bc.fecha_bono) = EXTRACT(YEAR FROM SYSDATE) - 1
    AND bc.hr_consulta > '17:15'
    AND ss.tipo_sal_id IN ('I', 'F')
ORDER BY
    bc.fecha_bono ASC,
    p.pac_run     ASC;
    
