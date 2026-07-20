SET SERVEROUTPUT ON;
 
/*=============================================================================
  0. secuencia para ID_ERROR
=============================================================================*/
 BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_error_proceso';
EXCEPTION
    WHEN OTHERS THEN NULL; -- la secuencia no existia, se ignora
END;
/
 
CREATE SEQUENCE seq_error_proceso START WITH 1 INCREMENT BY 1 NOCACHE;
/
/*=============================================================================
  1. funcion almacenada - puntaje por anios de experiencia (1.1)
=============================================================================*/
CREATE OR REPLACE FUNCTION fn_ptje_annos_experiencia (
    p_numrun IN antecedentes_personales.numrun%TYPE
) RETURN NUMBER
IS
    v_fecha_contrato_min  antecedentes_laborales.fecha_contrato%TYPE;
    v_annos_experiencia   NUMBER;
    v_ptje_experiencia    ptje_annos_experiencia.ptje_experiencia%TYPE := 0;
    v_msg_error           error_proceso.mensaje_error%TYPE;
BEGIN
    -- el puntaje se obtiene solo mediante sentecia SELECT
    -- se considera la fecha de contrato mas antigua del postulante (1.1)
    SELECT MIN(fecha_contrato)
      INTO v_fecha_contrato_min
      FROM antecedentes_laborales
     WHERE numrun = p_numrun;
 
    -- calculo de anios de experiencia a la fecha de ejecucion (SYSDATE)
    v_annos_experiencia := TRUNC(MONTHS_BETWEEN(SYSDATE, v_fecha_contrato_min) / 12);
 
    -- se obtienen puntajes segun el rango de anios de experiencia
    SELECT ptje_experiencia
      INTO v_ptje_experiencia
      FROM ptje_annos_experiencia
     WHERE v_annos_experiencia BETWEEN rango_annos_ini AND rango_annos_ter;
 
    -- los calculo se redondean
    RETURN ROUND(v_ptje_experiencia);
 
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        INSERT INTO error_proceso (id_error, rutina_error, mensaje_error)
        VALUES (seq_error_proceso.NEXTVAL, 'FN_PTJE_ANNOS_EXPERIENCIA',
                'Numrun ' || p_numrun || ': no se encontraron datos laborales o '
                || 'rango de puntaje para los años de experiencia calculados.');
        RETURN 0;
    WHEN OTHERS THEN
        v_msg_error := 'Numrun ' || p_numrun || ': ' || SQLERRM;
        INSERT INTO error_proceso (id_error, rutina_error, mensaje_error)
        VALUES (seq_error_proceso.NEXTVAL, 'FN_PTJE_ANNOS_EXPERIENCIA', v_msg_error);
        RETURN 0;
END fn_ptje_annos_experiencia;
/
 
 
/*=============================================================================
  2. funcion almacenada, puntaje por pais al que se postula (1.2)
  - el puntaje depende del pais de la institucion del programa de pasantia al que postulo la persona
=============================================================================*/
CREATE OR REPLACE FUNCTION fn_ptje_pais_postula (
    p_numrun IN antecedentes_personales.numrun%TYPE
) RETURN NUMBER
IS
    v_cod_programa  postulacion_pasantia_perfec.cod_programa%TYPE;
    v_cod_inst      pasantia_perfeccionamiento.cod_inst%TYPE;
    v_cod_pais      institucion.cod_pais%TYPE;
    v_ptje_pais     ptje_pais_postula.ptje_pais%TYPE := 0;
    v_msg_error     error_proceso.mensaje_error%TYPE;
BEGIN
    -- programa de pasantia al que postulo el postulante
    SELECT cod_programa
      INTO v_cod_programa
      FROM postulacion_pasantia_perfec
     WHERE numrun = p_numrun;
 
    -- institucion que dicta el programa
    SELECT cod_inst
      INTO v_cod_inst
      FROM pasantia_perfeccionamiento
     WHERE cod_programa = v_cod_programa;
 
    -- pais al que pertenece la institucion
    SELECT cod_pais
      INTO v_cod_pais
      FROM institucion
     WHERE cod_inst = v_cod_inst;
 
    -- puntaje asociado al pais
    SELECT ptje_pais
      INTO v_ptje_pais
      FROM ptje_pais_postula
     WHERE cod_pais = v_cod_pais;
 
    RETURN ROUND(v_ptje_pais);
 
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        INSERT INTO error_proceso (id_error, rutina_error, mensaje_error)
        VALUES (seq_error_proceso.NEXTVAL, 'FN_PTJE_PAIS_POSTULA',
                'Numrun ' || p_numrun || ': no se pudo determinar el puntaje por pais '
                || '(revisar programa/institucion/pais asociados).');
        RETURN 0;
    WHEN OTHERS THEN
        v_msg_error := 'Numrun ' || p_numrun || ': ' || SQLERRM;
        INSERT INTO error_proceso (id_error, rutina_error, mensaje_error)
        VALUES (seq_error_proceso.NEXTVAL, 'FN_PTJE_PAIS_POSTULA', v_msg_error);
        RETURN 0;
END fn_ptje_pais_postula;
/
 
 
/*=============================================================================
  3. package de puntaje extra por pluriempleo y horas (1.3)
    - el package tiene 2 constructores publicos:
  - una funcion para obtener el puntaje extra (1.3)
  - una variable publica que almacena ese puntaje calculado
=============================================================================*/
CREATE OR REPLACE PACKAGE pkg_ptje_extra AS
 
    -- variable publica que almacena el ultimo puntaje extra calculado
    g_ptje_extra NUMBER := 0;
 
    -- funcion publica, calcula el puntaje extra (1.3)
    -- p_porcentaje se recibe en forma parametrica (ej. 35 = 35%)
    FUNCTION fn_ptje_extra (
        p_numrun      IN antecedentes_personales.numrun%TYPE,
        p_ptje_annos  IN NUMBER,
        p_ptje_pais   IN NUMBER,
        p_porcentaje  IN NUMBER
    ) RETURN NUMBER;
 
END pkg_ptje_extra;
/
 
CREATE OR REPLACE PACKAGE BODY pkg_ptje_extra AS
 
    FUNCTION fn_ptje_extra (
        p_numrun      IN antecedentes_personales.numrun%TYPE,
        p_ptje_annos  IN NUMBER,
        p_ptje_pais   IN NUMBER,
        p_porcentaje  IN NUMBER
    ) RETURN NUMBER
    IS
        v_cant_establecim  NUMBER := 0;
        v_suma_horas       NUMBER := 0;
        v_ptje_extra       NUMBER := 0;
        v_msg_error        error_proceso.mensaje_error%TYPE;
    BEGIN
        -- cantidad de establecimientos distintos y suma de horas semanales trabajadas por el postulante (1.3)
        SELECT COUNT(DISTINCT establecimiento), NVL(SUM(horas_semanales), 0)
          INTO v_cant_establecim, v_suma_horas
          FROM antecedentes_laborales
         WHERE numrun = p_numrun;
 
        -- si trabaja en mas de 1 establecimiento y supera 30 hrs semanales en total se asigna el porcentaje parametrico sobre la suma de los puntajes de las reglas 1.1 y 1.2
        IF v_cant_establecim > 1 AND v_suma_horas > 30 THEN
            v_ptje_extra := ROUND((p_ptje_annos + p_ptje_pais) * p_porcentaje / 100);
        ELSE
            v_ptje_extra := 0;
        END IF;
 
        g_ptje_extra := v_ptje_extra;
        RETURN v_ptje_extra;
 
     EXCEPTION
        WHEN OTHERS THEN
            v_msg_error := 'Numrun ' || p_numrun || ': ' || SQLERRM;
            INSERT INTO error_proceso (id_error, rutina_error, mensaje_error)
            VALUES (seq_error_proceso.NEXTVAL, 'PKG_PTJE_EXTRA.FN_PTJE_EXTRA', v_msg_error);
            g_ptje_extra := 0;
            RETURN 0;
    END fn_ptje_extra;
 
END pkg_ptje_extra;
/
 
 
/*=============================================================================
  4. trigger que genera RESULTADO_POSTULACION (punto g)
=============================================================================*/
CREATE OR REPLACE TRIGGER trg_resultado_postulacion
AFTER INSERT ON detalle_puntaje_postulacion
FOR EACH ROW
DECLARE
    v_ptje_final  NUMBER;
    v_resultado   VARCHAR2(20);
    v_msg_error   error_proceso.mensaje_error%TYPE;
BEGIN
    -- puntaje final = suma de las 3 reglas de negocio (1.1 + 1.2 + 1.3)
    v_ptje_final := ROUND(:NEW.ptje_annos_exp + :NEW.ptje_pais_postula + :NEW.ptje_extra);
 
    IF v_ptje_final >= 2500 THEN
        v_resultado := 'SELECCIONADO';
    ELSE
        v_resultado := 'NO SELECCIONADO';
    END IF;
 
    INSERT INTO resultado_postulacion (run_postulante, ptje_final_post, resultado_post)
    VALUES (:NEW.run_postulante, v_ptje_final, v_resultado);
 
EXCEPTION
    WHEN OTHERS THEN
        v_msg_error := 'Run ' || :NEW.run_postulante || ': ' || SQLERRM;
        INSERT INTO error_proceso (id_error, rutina_error, mensaje_error)
        VALUES (seq_error_proceso.NEXTVAL, 'TRG_RESULTADO_POSTULACION', v_msg_error);
END trg_resultado_postulacion;
/
 
 
/*=============================================================================
  5. procesa a todos los postulantes (punto f)
  - trunca en tiempo de ejecucion las 3 tablas resultantes.
  - recorre a todos los postulantes (POSTULACION_PASANTIA_PERFEC) usando 
    las 2 funciones almacenadas y el package.
  - recibe como parametro el porcentaje parametrico del punto 1.3.
=============================================================================*/
CREATE OR REPLACE PROCEDURE pr_procesa_postulantes (
    p_porcentaje IN NUMBER
)
IS
    v_ptje_annos   NUMBER;
    v_ptje_pais    NUMBER;
    v_ptje_extra   NUMBER;
    v_run_format   detalle_puntaje_postulacion.run_postulante%TYPE;
    v_nombre_full  detalle_puntaje_postulacion.nombre_postulante%TYPE;
 
    -- cursor con todos los postulantes ordenados por run (punto f)
    CURSOR c_postulantes IS
        SELECT pp.numrun,
               ap.dvrun,
               ap.pnombre || ' ' || NVL(ap.snombre || ' ', '')
                          || ap.apaterno || ' ' || NVL(ap.amaterno, '') AS nombre_postulante
          FROM postulacion_pasantia_perfec pp
          JOIN antecedentes_personales ap ON ap.numrun = pp.numrun
         ORDER BY pp.numrun;
BEGIN
    -- trunc tablas resultantes en tiempo de ejecucion
    EXECUTE IMMEDIATE 'TRUNCATE TABLE detalle_puntaje_postulacion';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE resultado_postulacion';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE error_proceso';
 
    FOR r_post IN c_postulantes LOOP
 
        -- 1.1 puntaje por anios de experiencia
        v_ptje_annos := fn_ptje_annos_experiencia(r_post.numrun);
 
        -- 1.2 puntaje por pais al que se postula
        v_ptje_pais := fn_ptje_pais_postula(r_post.numrun);
 
        -- 1.3 puntaje extra (funcion del package + variable publica)
        v_ptje_extra := pkg_ptje_extra.fn_ptje_extra(
                            r_post.numrun, v_ptje_annos, v_ptje_pais, p_porcentaje
                         );
 
        -- rut formateado numrun-dvrun para la tabla de detalle
        v_run_format  := TO_CHAR(r_post.numrun) || '-' || r_post.dvrun;
        v_nombre_full := r_post.nombre_postulante;
 
        -- insert que dispara automaticamente el trigger trg_resultado_postulacion
        INSERT INTO detalle_puntaje_postulacion (
            run_postulante, nombre_postulante, ptje_annos_exp,
            ptje_pais_postula, ptje_extra
        ) VALUES (
            v_run_format, v_nombre_full, v_ptje_annos, v_ptje_pais, v_ptje_extra
        );
 
    END LOOP;
 
    COMMIT;
 
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END pr_procesa_postulantes;
/
 
/*=============================================================================
  6. ejecucion del proceso (variable bind parametrica)
=============================================================================*/
VARIABLE v_porcentaje NUMBER

-- 35% parametrico
EXEC :v_porcentaje := 35;
 
BEGIN
    pr_procesa_postulantes(:v_porcentaje);
END;
/
 
/*=============================================================================
 7. validacion de resultados
=============================================================================*/
SELECT * FROM detalle_puntaje_postulacion ORDER BY run_postulante;
SELECT * FROM resultado_postulacion ORDER BY run_postulante;
SELECT * FROM error_proceso ORDER BY id_error;