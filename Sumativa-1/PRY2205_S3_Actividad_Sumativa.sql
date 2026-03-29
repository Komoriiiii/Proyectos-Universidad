-- ====================
--       Caso 1
-- ====================

-- Columnas que seran utilizadas segun la figura 2 de la guia.
-- PROPIEDAD | DIRECCION | ARRIENDO | GGCC_ACTUAL | GGCC_AJUSTADO | UBICACION
-- Variable de sustitucion: &VALOR_MAXIMO

SELECT 
    p.nro_propiedad                                         AS "PROPIEDAD",
    UPPER(p.direccion_propiedad)                            AS "DIRECCION",
    TO_CHAR(p.valor_arriendo,'$999,999')                    AS "ARRIENDO",
    TO_CHAR (p.valor_gasto_comun, '$999,999')               AS "GGCC_ACTUAL",
    TO_CHAR (ROUND(p.valor_gasto_comun * 1.10), '$999,999') AS "GGCC_AJUSTADO",
    'Propiedad ubicada en comuna ' || p.id_comuna           AS "UBICACION"
    FROM propiedad p
    WHERE p.valor_arriendo <&VALOR_MAXIMO
    AND p.nro_dormitorios IS NOT NULL
    AND p.id_comuna IN (82, 84, 87)
    ORDER BY p.valor_gasto_comun ASC NULLS LAST,
             p.valor_arriendo    DESC;
             
-- =====================
--        Caso 2
-- =====================

-- Columnas utilizadas segun figura 3 de la guia.
-- Propiedad | Codigo arriendo | Fecha Inicio Arriendo | Fecha Termino Arriendo |Dias Arriendo | Años Arriendo | Clasificacion Estado
-- Variable de sustitucion: &DIAS_MINIMOS

SELECT
    ap.nro_propiedad                                                                              AS "Propiedad",
    ap.numrut_cli                                                                                 AS "Codigo Arriendo",
    TO_CHAR(ap.fecini_arriendo, 'DD.mon.YYYY', 'NLS_DATE_LANGUAGE=SPANISH')                       AS "Fecha Inicio Arriendo",
    NVL(TO_CHAR(ap.fecter_arriendo, 'DD.mon.YYYY', 'NLS_DATE_LANGUAGE=SPANISH'),
        'Propiedad Actualmente Arrendada')                                                        AS "Fecha Termino Arriendo",
    ROUND(NVL(ap.fecter_arriendo, SYSDATE) - ap.fecini_arriendo)                                  AS "Dias Arriendo",
    FLOOR((NVL(ap.fecter_arriendo, SYSDATE) - ap.fecini_arriendo) / 365)                          AS "Años Arriendo",
    CASE
        WHEN FLOOR ((NVL(ap.fecter_arriendo, SYSDATE) - ap.fecini_arriendo) / 365) >= 10
            THEN 'COMPROMISO DE VENTA'
        WHEN FLOOR ((NVL(ap.fecter_arriendo, SYSDATE) - ap.fecini_arriendo) / 365) >= 5
            THEN 'CLIENTE ANTIGUO'
        ELSE 'CLIENTE NUEVO'
    END                                                                                           AS "Clasificacion Estado"
FROM arriendo_propiedad ap 
WHERE ROUND(NVL(ap.fecter_arriendo, SYSDATE) - ap.fecini_arriendo) >= &DIAS_MINIMOS
ORDER BY ROUND(NVL(ap.fecter_arriendo, SYSDATE) - ap.fecini_arriendo) DESC;

-- =====================
--        Caso 3
-- =====================
-- Columnas segun la figura 4 de la guia
-- TIPO PROPIEDAD | DESCRIPCION | PROMEDIO GASTO COMUN | CANTIDAD PROPIEDADES | PROMEDIO VALOR ARRIENDO
-- Variable de sustitucion: &ARRIENDO_PROMEDIO_MINIMO

SELECT
    p.id_tipo_propiedad                                  AS "TIPO PROPIEDAD",
    INITCAP(tp.desc_tipo_propiedad)                      AS "DESCRIPCION",
    TO_CHAR(ROUND(AVG(p.valor_gasto_comun)), '$999,999') AS "PROMEDIO GASTO COMUN",
    COUNT(p.nro_propiedad)                               AS "CANTIDAD PROPIEDADES",
    TO_CHAR(ROUND(AVG(p.valor_arriendo)), '$9,999,999')  AS "PROMEDIO VALOR ARRIENDO"
FROM propiedad p
JOIN tipo_propiedad tp ON p.id_tipo_propiedad = tp.id_tipo_propiedad
GROUP BY p.id_tipo_propiedad,
         INITCAP(tp.desc_tipo_propiedad)
HAVING ROUND (AVG(p.valor_arriendo)) >= &ARRIENDO_PROMEDIO_MINIMO
ORDER BY p.id_tipo_propiedad ASC,
         ROUND (AVG(p.valor_arriendo)) DESC;

