-- se habilita la salida de mensajes en la consola
SET SERVEROUTPUT ON;

-- se trunca la tabla detalle de clientes
TRUNCATE TABLE DETALLE_DE_CLIENTES;

-- variables bind
VARIABLE v_periodo VARCHAR2(6)

-- se asginan los valores a las variables bind
BEGIN
    :v_periodo := TO_CHAR(SYSDATE, 'MMYYYY');
END;
/

-- bloque pl/sql anonimo principal
DECLARE
    CURSOR   cur_clientes IS
        SELECT   c.ID_CLI,
                 c.NUMRUN_CLI,
                 c.APPATERNO_CLI,
                 c.APMATERNO_CLI,
                 c.PNOMBRE_CLI,
                 c.RENTA,
                 c.FECHA_NAC_CLI,
                 c.ID_TIPO_CLI,
                 co.NOMBRE_COMUNA
        FROM     CLIENTE c
                 LEFT JOIN COMUNA co ON c.ID_COMUNA = co.ID_COMUNA;
    -- variables con %type
    v_numrun         CLIENTE.NUMRUN_CLI%TYPE; 
    v_pnombre        CLIENTE.PNOMBRE_CLI%TYPE;
    v_appaterno      CLIENTE.APPATERNO_CLI%TYPE;
    -- variables escalares de calculo y control
    v_mes             VARCHAR2(2);
    v_edad            NUMBER;
    v_puntaje         NUMBER;
    v_correo          VARCHAR2(200);
    v_porcentaje      NUMBER;
    v_nombre_completo VARCHAR2(100);
    v_anio_actual     NUMBER;
    v_contador        NUMBER := 0;
    v_total_clientes  NUMBER;

BEGIN
    v_mes := SUBSTR (:v_periodo, 1, 2);
    v_anio_actual := TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY'));

    SELECT COUNT(*)
    INTO v_total_clientes
    FROM CLIENTE;
    
-- ciclo principal usando for con cursor, recorre todos los clientes uno por uno

FOR reg IN cur_clientes LOOP
    v_contador := v_contador + 1;

    v_numrun         := reg.NUMRUN_CLI;
    v_pnombre        := reg.PNOMBRE_CLI;
    v_appaterno      := reg.APPATERNO_CLI;

    v_edad         := TRUNC(MONTHS_BETWEEN(SYSDATE, reg.FECHA_NAC_CLI)/12); -- se divide entre 12 para convertir a años y se usa trunc para eliminar los decimales
    v_puntaje      := 0;
    v_nombre_completo := reg.PNOMBRE_CLI || ' ' || reg.APPATERNO_CLI
                         || ' ' || reg.APMATERNO_CLI;
 -- regla b
IF reg.RENTA > 800000                  
    AND UPPER(TRIM(reg.NOMBRE_COMUNA)) NOT IN
        ('LA REINA', 'LAS CONDES', 'VITACURA')
    THEN 
        v_puntaje := ROUND(reg.RENTA *0.03);
        
 -- regla c       
    ELSIF reg.ID_TIPO_CLI IN ('B', 'D') THEN  
        v_puntaje := ROUND (v_edad * 30);
    END IF;
    
 -- regla d
IF v_puntaje = 0 THEN  
    BEGIN
        SELECT porcentaje
        INTO v_porcentaje
        FROM TRAMO_EDAD
        WHERE v_edad BETWEEN TRAMO_INF AND TRAMO_SUP
            AND ANNO_VIG = v_anio_actual;
            
        v_puntaje := ROUND(reg.RENTA * (v_porcentaje /100));
        
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                 v_puntaje :=0;
        END;
    END IF;

-- generacion del correo electronico

v_correo := 
    LOWER(v_appaterno)
    || TO_CHAR(v_edad)
    || '*'
    || SUBSTR(v_pnombre, 1, 1)
    || TO_CHAR(reg.FECHA_NAC_CLI, 'DD')
    || v_mes
    || '@LogiCarg.cl';

-- se inserta registro en detalle_de_clientes

INSERT INTO DETALLE_DE_CLIENTES (
    IDC,
    RUT,
    CLIENTE,
    EDAD,
    PUNTAJE,
    CORREO_CORP,
    PERIODO
) VALUES (
    reg.ID_CLI,
    v_numrun,
    v_nombre_completo,
    v_edad,
    v_puntaje,
    v_correo,
    :v_periodo
);
END LOOP;

-- fin del ciclo principal

-- validacion final del proceso con commit y rollback

IF v_contador = v_total_clientes THEN
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('================================================');
    DBMS_OUTPUT.PUT_LINE('Proceso Finalizado Correctamente.');
    DBMS_OUTPUT.PUT_LINE('Total de Clientes Procesados: ' || v_contador);
    DBMS_OUTPUT.PUT_LINE('================================================');
ELSE
    ROLLBACK;
     DBMS_OUTPUT.PUT_LINE('================================================');
     DBMS_OUTPUT.PUT_LINE('Proceso Finalizado con errores.');
     DBMS_OUTPUT.PUT_LINE('Se deshacen las Transacciones.');
     DBMS_OUTPUT.PUT_LINE('Clientes esperados: ' || v_total_clientes);
     DBMS_OUTPUT.PUT_LINE('Clientes Procesados: '|| v_contador);
     DBMS_OUTPUT.PUT_LINE('================================================');
END IF;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error inesperado en el proceso: ' || SQLERRM);
        DBMS_OUTPUT.PUT_LINE('Se deshacen las Transacciones.');
END;
/

