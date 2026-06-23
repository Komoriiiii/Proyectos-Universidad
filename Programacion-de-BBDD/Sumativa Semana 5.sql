
-- declaracion y asignacion de variables BIND

VARIABLE v_periodo VARCHAR2(6)
VARIABLE v_limite NUMBER

BEGIN
    :v_periodo := '062021';
    :v_limite  := 250000;
END;
/

-- bloque anonimo principal

DECLARE

TYPE t_porc_movil IS VARRAY(5) OF NUMBER;
    v_porc_movil t_porc_movil := t_porc_movil(2, 4, 5, 7, 9);

-- registro con los datos de los profesionales 

TYPE r_profesional IS RECORD (
        numrun_prof      profesional.numrun_prof%TYPE,
        appaterno        profesional.appaterno%TYPE,
        nombre           profesional.nombre%TYPE,
        cod_profesion    profesional.cod_profesion%TYPE,
        nombre_profesion profesion.nombre_profesion%TYPE,
        sueldo           profesional.sueldo%TYPE,
        nom_comuna       comuna.nom_comuna%TYPE,
        cod_tpcontrato   profesional.cod_tpcontrato%TYPE
    );
    v_prof r_profesional;

-- variables de trabajo usando %TYPE

    v_nro_asesorias    asesoria.numrun_prof%TYPE;           -- se usa solo para tipo numerico generico
    v_cant_asesorias   NUMBER(3);
    v_total_honorarios NUMBER(10);
 
    v_incentivo_tpcont tipo_contrato.incentivo%TYPE;
    v_porc_profesion   porcentaje_profesion.asignacion%TYPE;
 
    v_asig_movil  NUMBER(8);
    v_asig_tpcont NUMBER(8);
    v_asig_prof   NUMBER(8);
    v_asig_total  NUMBER(8);
 
    v_mes_proceso  NUMBER(2);
    v_anno_proceso NUMBER(4);
    v_anno_mes     NUMBER(6);
 
    v_run_str            VARCHAR2(15);
    v_nombre_completo    VARCHAR2(50);
    
-- resumen por profesion
    
    v_profesion_ant     profesion.nombre_profesion%TYPE := NULL;
    v_tot_asesorias_grp NUMBER(6) := 0;
    v_tot_honor_grp     NUMBER(10) := 0;
    v_tot_movil_grp     NUMBER(10) := 0;
    v_tot_tpcont_grp    NUMBER(10) := 0;
    v_tot_asigprof_grp  NUMBER(10) := 0;
    v_tot_total_grp     NUMBER(10) := 0;
 
    v_msg_oracle errores_proceso.mensaje_error_oracle%TYPE;
    v_msg_usr    errores_proceso.mensaje_error_usr%TYPE;
    
-- excepcion para cuando el monto supera el tope

   e_tope_superado EXCEPTION;
   
-- cursor sin parametro, solo con datos basicos para insertar y calcular
-- ordenado por profesion, apellido paterno y nombre
   CURSOR c_profesionales IS
        SELECT p.numrun_prof, p.appaterno, p.nombre, p.cod_profesion,
               pr.nombre_profesion, p.sueldo, c.nom_comuna, p.cod_tpcontrato
        FROM   profesional p, profesion pr, comuna c
        WHERE  p.cod_profesion = pr.cod_profesion
        AND    p.cod_comuna    = c.cod_comuna
        AND    EXISTS (SELECT 1
                        FROM   asesoria a
                        WHERE  a.numrun_prof = p.numrun_prof
                        AND    TO_CHAR(a.inicio_asesoria, 'MMYYYY') = :v_periodo)
        ORDER BY pr.nombre_profesion, p.appaterno, p.nombre;
        
BEGIN

--trunc para reejecutar el proceso las veces que sea necesario

    EXECUTE IMMEDIATE 'TRUNCATE TABLE detalle_asignacion_mes';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE resumen_mes_profesion';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE errores_proceso';

    BEGIN
        EXECUTE IMMEDIATE 'DROP SEQUENCE sq_errores';
    EXCEPTION
        WHEN OTHERS THEN
            NULL; 
    END;
    EXECUTE IMMEDIATE 'CREATE SEQUENCE sq_errores START WITH 1 INCREMENT BY 1';
    
    
--Calculo de mes y anio a partir de la variable :v_periodo

    v_mes_proceso  := TO_NUMBER(SUBSTR(:v_periodo, 1, 2));
    v_anno_proceso := TO_NUMBER(SUBSTR(:v_periodo, 3, 4));
    v_anno_mes     := TO_NUMBER(SUBSTR(:v_periodo, 3, 4) || SUBSTR(:v_periodo, 1, 2));
 
    FOR rec IN c_profesionales LOOP

-- Se traspasan los datos del cursor al REGISTRO de trabajo

        v_prof.numrun_prof      := rec.numrun_prof;
        v_prof.appaterno        := rec.appaterno;
        v_prof.nombre           := rec.nombre;
        v_prof.cod_profesion    := rec.cod_profesion;
        v_prof.nombre_profesion := rec.nombre_profesion;
        v_prof.sueldo           := rec.sueldo;
        v_prof.nom_comuna       := rec.nom_comuna;
        v_prof.cod_tpcontrato   := rec.cod_tpcontrato;
        
-- control break en caso de cambio de profesion 

 IF v_profesion_ant IS NOT NULL
    AND v_profesion_ant != v_prof.nombre_profesion THEN
 
    INSERT INTO resumen_mes_profesion (
                anno_mes_proceso, profesion, total_asesorias,
                monto_total_honorarios, monto_total_movil_extra,
                monto_total_asig_tipocont, monto_total_asig_prof,
                monto_total_asignaciones)
    VALUES (
            v_anno_mes, v_profesion_ant, v_tot_asesorias_grp,
            v_tot_honor_grp, v_tot_movil_grp,
            v_tot_tpcont_grp, v_tot_asigprof_grp,
            v_tot_total_grp);
 
            v_tot_asesorias_grp := 0;
            v_tot_honor_grp     := 0;
            v_tot_movil_grp     := 0;
            v_tot_tpcont_grp    := 0;
            v_tot_asigprof_grp  := 0;
            v_tot_total_grp     := 0;
    END IF;
        
-- select aparte

SELECT COUNT(*), NVL(SUM(honorario), 0)
    INTO   v_cant_asesorias, v_total_honorarios
    FROM   asesoria
    WHERE  numrun_prof = v_prof.numrun_prof
    AND    TO_CHAR(inicio_asesoria, 'MMYYYY') = :v_periodo;
        
-- select separado incentivo segun el tipo de contrato

SELECT incentivo
    INTO   v_incentivo_tpcont
    FROM   tipo_contrato
    WHERE  cod_tpcontrato = v_prof.cod_tpcontrato;
    
-- calculo de asignacion por movilizacion extra, evaluando la comuna y el tope 
-- de honorarios 

v_asig_movil := 0;
 
    IF v_prof.nom_comuna = 'Santiago' AND v_total_honorarios < 350000 THEN
            v_asig_movil := ROUND(v_total_honorarios * v_porc_movil(1) / 100);
    ELSIF v_prof.nom_comuna = 'Ñuñoa' THEN
            v_asig_movil := ROUND(v_total_honorarios * v_porc_movil(2) / 100);
    ELSIF v_prof.nom_comuna = 'La Reina' AND v_total_honorarios < 400000 THEN
            v_asig_movil := ROUND(v_total_honorarios * v_porc_movil(3) / 100);
    ELSIF v_prof.nom_comuna = 'La Florida' AND v_total_honorarios < 800000 THEN
            v_asig_movil := ROUND(v_total_honorarios * v_porc_movil(4) / 100);
    ELSIF v_prof.nom_comuna = 'Macul' AND v_total_honorarios < 680000 THEN
            v_asig_movil := ROUND(v_total_honorarios * v_porc_movil(5) / 100);
    END IF;

-- asignacion por tipo de contrato

 v_asig_tpcont := ROUND(v_total_honorarios * v_incentivo_tpcont / 100);
 
 -- calculo asignacion por profesion
 BEGIN
    SELECT asignacion
    INTO   v_porc_profesion
    FROM   porcentaje_profesion
    WHERE  cod_profesion = v_prof.cod_profesion;
 
    v_asig_prof := ROUND(v_prof.sueldo * v_porc_profesion / 100);
EXCEPTION
    WHEN OTHERS THEN
        v_msg_oracle := SQLERRM;
        v_msg_usr    := 'Error al obtener porcentaje de asignación para el run Nro. '
                     || v_prof.numrun_prof;
    INSERT INTO errores_proceso (error_id, mensaje_error_oracle, mensaje_error_usr)
    VALUES (sq_errores.NEXTVAL, v_msg_oracle, v_msg_usr);
 
    v_asig_prof := 0;
END;

-- monto total de asignaciones

    v_asig_total := v_asig_movil + v_asig_tpcont + v_asig_prof;
    
-- excepcion para no superar el monto limite acordado

BEGIN
    IF v_asig_total > :v_limite THEN
        RAISE e_tope_superado;
    END IF;
    
EXCEPTION
    WHEN e_tope_superado THEN
         v_msg_usr := 'Se reemplazó el monto total de las asignaciones calculadas de '
                       || v_asig_total || ' por el monto límite de ' || :v_limite
                       || ' para el run Nro. ' || v_prof.numrun_prof;
 
    INSERT INTO errores_proceso (error_id, mensaje_error_oracle, mensaje_error_usr)
    VALUES (sq_errores.NEXTVAL, 'TOPE_SUPERADO', v_msg_usr);
                v_asig_total := :v_limite;
END;

-- armado de datos para el detalle

v_run_str        := TO_CHAR(v_prof.numrun_prof);
v_nombre_completo := v_prof.appaterno || ' ' || v_prof.nombre;

-- insert en tabla de detalle

INSERT INTO detalle_asignacion_mes (
            mes_proceso, anno_proceso, run_profesional, nombre_profesional,
            profesion, nro_asesorias, monto_honorarios, monto_movil_extra,
            monto_asig_tipocont, monto_asig_profesion, monto_total_asignaciones)
VALUES (
        v_mes_proceso, v_anno_proceso, v_run_str, v_nombre_completo,
        v_prof.nombre_profesion, v_cant_asesorias, v_total_honorarios, v_asig_movil,
        v_asig_tpcont, v_asig_prof, v_asig_total);
        
-- acumulacion para el resumen de la profesion

v_tot_asesorias_grp := v_tot_asesorias_grp + v_cant_asesorias;
        v_tot_honor_grp     := v_tot_honor_grp + v_total_honorarios;
        v_tot_movil_grp     := v_tot_movil_grp + v_asig_movil;
        v_tot_tpcont_grp    := v_tot_tpcont_grp + v_asig_tpcont;
        v_tot_asigprof_grp  := v_tot_asigprof_grp + v_asig_prof;
        v_tot_total_grp     := v_tot_total_grp + v_asig_total;
 
        v_profesion_ant := v_prof.nombre_profesion;
 
    END LOOP;

-- se graba el resumen del ultimo grupo de profesion que se proceso

IF v_profesion_ant IS NOT NULL THEN
    INSERT INTO resumen_mes_profesion (
                anno_mes_proceso, profesion, total_asesorias,
                monto_total_honorarios, monto_total_movil_extra,
                monto_total_asig_tipocont, monto_total_asig_prof,
                monto_total_asignaciones)
    VALUES (
         v_anno_mes, v_profesion_ant, v_tot_asesorias_grp,
         v_tot_honor_grp, v_tot_movil_grp,
         v_tot_tpcont_grp, v_tot_asigprof_grp,
         v_tot_total_grp);
    END IF;
 
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Proceso de asignaciones finalizado correctamente para el periodo ' || :v_periodo);
 
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error inesperado en el proceso: ' || SQLERRM);
END;
/


-- probar output

-- cuantos profesionales se procesaron
SELECT * FROM detalle_asignacion_mes
ORDER BY profesion, run_profesional;

-- resumen x profesion
SELECT * FROM resumen_mes_profesion
ORDER BY profesion;

-- errores registrados
SELECT * FROM errores_proceso
ORDER BY error_id;
