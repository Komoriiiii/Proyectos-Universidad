
-- se crea secuencia para la tabla ERROR_CALC

DECLARE
  v_existe NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_existe FROM USER_SEQUENCES WHERE SEQUENCE_NAME = 'SEQ_ERROR';
  IF v_existe = 0 THEN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE SEQ_ERROR START WITH 1 INCREMENT BY 1 NOCACHE';
  END IF;
END;
/

--=============================================================================
-- se crea Package PKG_LIQUIDACION_SPA
-- con procedure pr_inserta_error para registrar errores en ERROR_CALC 
-- funcion FN_PROMEDIO_VENTAS_ANT para el promedio de ventas del anio anterior

CREATE OR REPLACE PACKAGE PKG_LIQUIDACION_SPA IS

  g_promedio_ventas_anio_ant NUMBER := 0;

  PROCEDURE PR_INSERTA_ERROR(
    p_rutina_error IN VARCHAR2,
    p_msg_error    IN VARCHAR2,
    p_descrip_user IN VARCHAR2
  );

  FUNCTION FN_PROMEDIO_VENTAS_ANT(p_anno_proceso IN NUMBER) RETURN NUMBER;

END PKG_LIQUIDACION_SPA;
/

CREATE OR REPLACE PACKAGE BODY PKG_LIQUIDACION_SPA IS

-- se inserta un registro de error en ERROR_CALC, incrementando la SEQ_ERROR
  PROCEDURE PR_INSERTA_ERROR(
    p_rutina_error IN VARCHAR2,
    p_msg_error    IN VARCHAR2,
    p_descrip_user IN VARCHAR2
  ) IS
    PRAGMA AUTONOMOUS_TRANSACTION; -- para que el log de error persista aunque el resto haga rollback
  BEGIN
    INSERT INTO ERROR_CALC (CORREL_ERROR, RUTINA_ERROR, DESCRIP_ERROR, DESCRIP_USER)
    VALUES (SEQ_ERROR.NEXTVAL, p_rutina_error, SUBSTR(p_msg_error, 1, 300), SUBSTR(p_descrip_user, 1, 255));
    COMMIT;
  END PR_INSERTA_ERROR;

  -- retorna el promedio de VALOR_TOTAL del detalle de boletas del anio anterior al indicado, si no hay boletas ese anio, retorna 0
  FUNCTION FN_PROMEDIO_VENTAS_ANT(p_anno_proceso IN NUMBER) RETURN NUMBER IS
    v_promedio NUMBER := 0;
  BEGIN
    SELECT NVL(AVG(db.valor_total), 0)
      INTO v_promedio
      FROM detalle_boleta db
      JOIN boleta b ON b.nro_boleta = db.nro_boleta
     WHERE EXTRACT(YEAR FROM b.fecha) = p_anno_proceso - 1;

    RETURN v_promedio;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN 0;
    WHEN OTHERS THEN
      PR_INSERTA_ERROR('FN_PROMEDIO_VENTAS_ANT', SQLERRM,
                        'Error al calcular el promedio de ventas del anio anterior a ' || p_anno_proceso);
      RETURN 0;
  END FN_PROMEDIO_VENTAS_ANT;

END PKG_LIQUIDACION_SPA;
/

--==============================================================================
-- funciones almacenadas independientes

-- retorna el porcentaje de asignacion segun los anios de antiguedad del empleado
CREATE OR REPLACE FUNCTION FN_PCT_ANTIGUEDAD(
  p_run_empleado  IN EMPLEADO.RUN_EMPLEADO%TYPE,
  p_fecha_proceso IN DATE
) RETURN NUMBER IS
  v_fecha_contrato   EMPLEADO.FECHA_CONTRATO%TYPE;
  v_annos_antiguedad NUMBER;
  v_porcentaje       PCT_ANTIGUEDAD.PORC_ANTIGUEDAD%TYPE;
BEGIN
  SELECT FECHA_CONTRATO
    INTO v_fecha_contrato
    FROM EMPLEADO
   WHERE RUN_EMPLEADO = p_run_empleado;

  v_annos_antiguedad := TRUNC(MONTHS_BETWEEN(p_fecha_proceso, v_fecha_contrato) / 12);

  SELECT PORC_ANTIGUEDAD
    INTO v_porcentaje
    FROM PCT_ANTIGUEDAD
   WHERE v_annos_antiguedad BETWEEN ANNOS_ANTIGUEDAD_INF AND ANNOS_ANTIGUEDAD_SUP;

  RETURN v_porcentaje;
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    PKG_LIQUIDACION_SPA.PR_INSERTA_ERROR('FN_PCT_ANTIGUEDAD', SQLERRM,
      'No se encontro tramo de antiguedad para el empleado ' || p_run_empleado);
    RETURN 0;
  WHEN TOO_MANY_ROWS THEN
    PKG_LIQUIDACION_SPA.PR_INSERTA_ERROR('FN_PCT_ANTIGUEDAD', SQLERRM,
      'Mas de un tramo de antiguedad coincide para el empleado ' || p_run_empleado);
    RETURN 0;
  WHEN OTHERS THEN
    PKG_LIQUIDACION_SPA.PR_INSERTA_ERROR('FN_PCT_ANTIGUEDAD', SQLERRM,
      'Error inesperado calculando antiguedad para el empleado ' || p_run_empleado);
    RETURN 0;
END FN_PCT_ANTIGUEDAD;
/

-- retorna el porcentaje de asignacion segun el nivel de estudios del empleado
CREATE OR REPLACE FUNCTION FN_PCT_NIVEL_ESTUDIOS(
  p_cod_escolaridad IN EMPLEADO.COD_ESCOLARIDAD%TYPE
) RETURN NUMBER IS
  v_porcentaje PCT_NIVEL_ESTUDIOS.PORC_ESCOLARIDAD%TYPE;
BEGIN
  SELECT PORC_ESCOLARIDAD
    INTO v_porcentaje
    FROM PCT_NIVEL_ESTUDIOS
   WHERE COD_ESCOLARIDAD = p_cod_escolaridad;

  RETURN v_porcentaje;
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    PKG_LIQUIDACION_SPA.PR_INSERTA_ERROR('FN_PCT_NIVEL_ESTUDIOS', SQLERRM,
      'No se encontro porcentaje para el codigo de escolaridad ' || p_cod_escolaridad);
    RETURN 0;
  WHEN TOO_MANY_ROWS THEN
    PKG_LIQUIDACION_SPA.PR_INSERTA_ERROR('FN_PCT_NIVEL_ESTUDIOS', SQLERRM,
      'Mas de un porcentaje coincide para el codigo de escolaridad ' || p_cod_escolaridad);
    RETURN 0;
  WHEN OTHERS THEN
    PKG_LIQUIDACION_SPA.PR_INSERTA_ERROR('FN_PCT_NIVEL_ESTUDIOS', SQLERRM,
      'Error inesperado obteniendo porcentaje de escolaridad ' || p_cod_escolaridad);
    RETURN 0;
END FN_PCT_NIVEL_ESTUDIOS;
/

-- determina si es que el vendedor cumple la condicion de ventas de 7%> promedio del anio anterior 
CREATE OR REPLACE FUNCTION FN_CUMPLE_ASIG_ANTIGUEDAD(
  p_run_empleado IN EMPLEADO.RUN_EMPLEADO%TYPE,
  p_anno_proceso IN NUMBER
) RETURN NUMBER IS
  v_total_ventas_vendedor NUMBER := 0;
  v_pct_7                 NUMBER;
BEGIN
  SELECT NVL(SUM(b.monto_total_boleta), 0)
    INTO v_total_ventas_vendedor
    FROM BOLETA b
   WHERE b.run_empleado = p_run_empleado
     AND EXTRACT(YEAR FROM b.fecha) = p_anno_proceso;

  v_pct_7 := v_total_ventas_vendedor * 0.07;

  IF v_pct_7 > PKG_LIQUIDACION_SPA.g_promedio_ventas_anio_ant THEN
    RETURN 1;
  ELSE
    RETURN 0;
  END IF;
EXCEPTION
  WHEN OTHERS THEN
    PKG_LIQUIDACION_SPA.PR_INSERTA_ERROR('FN_CUMPLE_ASIG_ANTIGUEDAD', SQLERRM,
      'Error verificando condicion de ventas para el vendedor ' || p_run_empleado);
    RETURN 0;
END FN_CUMPLE_ASIG_ANTIGUEDAD;
/

--=============================================================================
-- procedimiento almacenado principal

CREATE OR REPLACE PROCEDURE PR_LIQUIDACION_EMPLEADOS(
  p_anno IN NUMBER,
  p_mes  IN NUMBER
) IS
  v_fecha_proceso  DATE;
  v_tipo_vendedor  TIPO_EMPLEADO.TIPO_EMPLEADO%TYPE;
  v_cod_fonasa     PREVISION_SALUD.COD_SALUD%TYPE;

  v_asig_especial  NUMBER;
  v_asig_estudios  NUMBER;
  v_total_haberes  NUMBER;
  v_pct_antiguedad NUMBER;
  v_pct_estudios   NUMBER;

  CURSOR c_empleados IS
    SELECT RUN_EMPLEADO,
           NOMBRE || ' ' || PATERNO || ' ' || MATERNO AS NOMBRE_COMPLETO,
           SUELDO_BASE, TIPO_EMPLEADO, COD_ESCOLARIDAD, COD_SALUD
      FROM EMPLEADO;
BEGIN
  -- truncar tablas para reejecutar el proceso las veces que sea necesario
  EXECUTE IMMEDIATE 'TRUNCATE TABLE LIQUIDACION_EMPLEADO';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE ERROR_CALC';

  v_fecha_proceso := TO_DATE('01/' || LPAD(p_mes, 2, '0') || '/' || p_anno, 'DD/MM/YYYY');

  -- se calcula una sola vez el promedio de ventas del anio anterior con un package
  PKG_LIQUIDACION_SPA.g_promedio_ventas_anio_ant := PKG_LIQUIDACION_SPA.FN_PROMEDIO_VENTAS_ANT(p_anno);

  -- para ubicar el codigo de tipo de empleado "VENDEDOR"
  BEGIN
    SELECT TIPO_EMPLEADO INTO v_tipo_vendedor
      FROM TIPO_EMPLEADO WHERE UPPER(DESCRIPCION) = 'VENDEDOR';
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      v_tipo_vendedor := NULL;
      PKG_LIQUIDACION_SPA.PR_INSERTA_ERROR('PR_LIQUIDACION_EMPLEADOS', 'NO_DATA_FOUND',
        'No se encontro el tipo de empleado VENDEDOR en TIPO_EMPLEADO');
  END;

  -- para ubicar el codigo de de fonasa
  BEGIN
    SELECT COD_SALUD INTO v_cod_fonasa
      FROM PREVISION_SALUD WHERE UPPER(NOM_SALUD) = 'FONASA';
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      v_cod_fonasa := NULL;
      PKG_LIQUIDACION_SPA.PR_INSERTA_ERROR('PR_LIQUIDACION_EMPLEADOS', 'NO_DATA_FOUND',
        'No se encontro el codigo de prevision de salud FONASA en PREVISION_SALUD');
  END;

  FOR emp IN c_empleados LOOP
    v_asig_especial := 0;
    v_asig_estudios := 0;

    -- asignacion especial por antiguedad solo para los vendedores que superan la condicion de ventas
    IF v_tipo_vendedor IS NOT NULL AND emp.TIPO_EMPLEADO = v_tipo_vendedor THEN
      IF FN_CUMPLE_ASIG_ANTIGUEDAD(emp.RUN_EMPLEADO, p_anno) = 1 THEN
        v_pct_antiguedad := FN_PCT_ANTIGUEDAD(emp.RUN_EMPLEADO, v_fecha_proceso);
        v_asig_especial := ROUND(emp.SUELDO_BASE * v_pct_antiguedad / 100, 0);
      END IF;
    END IF;

    -- asignacion por nivel de estudios solo para empleados con fonasa
    IF v_cod_fonasa IS NOT NULL AND emp.COD_SALUD = v_cod_fonasa THEN
      v_pct_estudios  := FN_PCT_NIVEL_ESTUDIOS(emp.COD_ESCOLARIDAD);
      v_asig_estudios := ROUND(emp.SUELDO_BASE * v_pct_estudios / 100, 0);
    END IF;

    -- total de haberes
    v_total_haberes := emp.SUELDO_BASE + v_asig_especial + v_asig_estudios;

    INSERT INTO LIQUIDACION_EMPLEADO (
      MES, ANNO, RUN_EMPLEADO, NOMBRE_EMPLEADO, SUELDO_BASE, ASIG_ESPECIAL, ASIG_ESTUDIOS, TOTAL_HABERES
    ) VALUES (
      p_mes, p_anno, emp.RUN_EMPLEADO, emp.NOMBRE_COMPLETO, emp.SUELDO_BASE, v_asig_especial, v_asig_estudios, v_total_haberes
    );
  END LOOP;

  COMMIT;
EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    PKG_LIQUIDACION_SPA.PR_INSERTA_ERROR('PR_LIQUIDACION_EMPLEADOS', SQLERRM,
      'Error general durante el proceso de liquidacion de ' || p_mes || '/' || p_anno);
    RAISE;
END PR_LIQUIDACION_EMPLEADOS;
/

-- prueba del proceso principal, liquidacion junio 2024

BEGIN
  PR_LIQUIDACION_EMPLEADOS(2024, 6);
END;
/

SELECT * FROM LIQUIDACION_EMPLEADO;
SELECT * FROM ERROR_CALC;

-- al revisar ERROR_CALC despues de esta corrida con el poblado real se deberia ver el error TOO_MANY_ROWS para los empleados con COD_ESCOLARIDAD = 5


-- ============================================================================
--  trigger para proteger producto
--  bloquea INSERT (-20501) y DELETE (-20500) de lunes a viernes
--  permite UPDATE del valor unitario cualquier dia
--  en caso de que el nuevo valor unitario supere el 10% del promedio de ventas del anio anterior se recalcula VALOR_TOTAL en DETALLE_BOLETA para ese producto

CREATE OR REPLACE TRIGGER TRG_PROTEGE_PRODUCTO
BEFORE INSERT OR DELETE OR UPDATE OF VALOR_UNITARIO ON PRODUCTO
FOR EACH ROW
DECLARE
  v_dia_semana      NUMBER;
  v_promedio_ventas NUMBER;
  v_umbral          NUMBER;
BEGIN
  -- 0 = lunes, 1 = martes, 2 = miercoles, 3 = jueves, 4 = viernes, 5-6 = fin de semana
  v_dia_semana := TRUNC(SYSDATE) - TRUNC(SYSDATE, 'IW');

  IF v_dia_semana BETWEEN 0 AND 4 THEN
    IF INSERTING THEN
      RAISE_APPLICATION_ERROR(-20501, 'TABLA DE PRODUCTO PROTEGIDA');
    ELSIF DELETING THEN
      RAISE_APPLICATION_ERROR(-20500, 'TABLA DE PRODUCTO PROTEGIDA');
    END IF;
  END IF;

  IF UPDATING('VALOR_UNITARIO') THEN
    v_promedio_ventas := PKG_LIQUIDACION_SPA.FN_PROMEDIO_VENTAS_ANT(EXTRACT(YEAR FROM SYSDATE));
    v_umbral := v_promedio_ventas * 0.10;

    IF :NEW.VALOR_UNITARIO > v_umbral THEN
      UPDATE DETALLE_BOLETA
         SET VALOR_TOTAL = CANTIDAD * :NEW.VALOR_UNITARIO
       WHERE COD_PRODUCTO = :NEW.COD_PRODUCTO;
    END IF;
  END IF;
END;
/

--=============================================================================

-- pruebas del trigger

-- prueba 1 intentar agregar o eliminar un producto un dia habil (en base al trigger TRG_PROTEGE_PRODUCTO deberian fallar con los errores especificados)
INSERT INTO PRODUCTO (COD_PRODUCTO, DESCRIPCION, COD_UNIDAD, VALOR_UNITARIO, TOTAL_STOCK, STOCK_MINIMO, PROCEDENCIA)
VALUES (999, 'PRODUCTO PRUEBA', 'UN', 500, 10, 2, 'N'); -- debe arrojar ORA-20501

DELETE FROM PRODUCTO WHERE COD_PRODUCTO = 19; -- debe arrojar ORA-20500

-- prueba 2 actualiza el valor unitario del producto 19 a 1000 (no deberia superar el 10% del promedio, el valor total deberia mostrar que se actualizo correctamente el valor del producto aunque en VUNITARIO aun aparezca como 2000)
UPDATE PRODUCTO SET VALOR_UNITARIO = 1000 WHERE COD_PRODUCTO = 19;
COMMIT;

-- prueba 3 actualiza el valor unitario del producto 19 a 10000 (deberia superar el 10% del promedio  y recalcular VALOR_TOTAL en DETALLE_BOLETA para el producto
-- igual que en la prueba anterior el resultado de la actualizacion se deberia ver reflejado en el valor total)
UPDATE PRODUCTO SET VALOR_UNITARIO = 10000 WHERE COD_PRODUCTO = 19;
COMMIT;

SELECT * FROM DETALLE_BOLETA WHERE COD_PRODUCTO = 19;