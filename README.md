Duoc UC - Desarrollo Orientado a Objetos I
Evaluación Sumativa 2 - Semana 5

  Autor del proyecto

Nombre completo: Gustavo Adolfo Gana Luna
Sección: I_002A
Carrera: Analista Programador Computacional
Sede: En Línea
Fecha de entrega: 24/11/2025



El sistema permite gestionar información de centros de cultivo mediante un modelo orientado a objetos, solucionando los problemas de duplicación de datos, acceso a información y automatización de procesos manuales.

 Novedades de la Versión 2.0
 Mejoras Implementadas

Validaciones y Manejo de Errores:

- Validación de datos con try-catch en todas las operaciones críticas
- Clase ValidadorDatos para validaciones centralizadas
- Mensajes de error descriptivos y manejo robusto de excepciones
- Contador de errores en carga de archivos

Nuevas Funcionalidades:

- Menú interactivo con 10 opciones de consulta
- Búsqueda por nombre (exacta y parcial)
- Filtros múltiples (producción, comuna, estado)
- Ranking de centros por producción
- Estadísticas generales del sistema
- Agrupación de centros por comuna
- Agregar nuevos centros desde consola
- Vista detallada de cada centro

Mejoras de Arquitectura:

- Reorganización en paquetes funcionales (model, service, util, app)
- Uso de Streams y expresiones lambda
- Clase FormateadorSalida para presentación profesional
- Separación clara de responsabilidades
- Código más mantenible y escalable


Estructura de Paquetes
Paquete model
Contiene las clases del dominio

CentroCultivo.java: Entidad principal del negocio con atributos validados

Paquete service
Contiene la lógica de negocio

GestorCentros.java: CRUD, búsquedas, filtros y estadísticas

Paquete util
Contiene utilidades reutilizables (Librería personalizada)

ValidadorDatos.java: Validaciones centralizadas
FormateadorSalida.java: Formateo profesional de consola

Paquete app
Contiene la interfaz de usuario

Main.java: Menú interactivo y punto de entrada

Carpeta resources
Almacena archivos de datos externos

centros_cultivo.txt: Base de datos en texto plano

Clases Implementadas 

Clase CentroCultivo

Ubicación: model.CentroCultivo
Propósito: Representa un centro de cultivo de salmón
Atributos:

nombre (String): Nombre del centro de cultivo
comuna (String): Ubicación geográfica
toneladasProducidas (double): Producción anual
estado (String): Estado operacional (Activo/Inactivo/En Mantenimiento)
empleados (int): Cantidad de empleados


Características:
 
- Todos los atributos son private
- Getters y setters con validaciones
- 3 constructores (vacío, básico, completo)
- Método toString() personalizado
- Métodos equals() y hashCode() implementados
- Métodos de negocio: esAltaProduccion(), getCategoria(), getProduccionPorEmpleado()



Clase GestorCentros

Ubicación: service.GestorCentros
Propósito: Gestiona todas las operaciones sobre la colección de centros
Funcionalidades principales:

cargarDatos(String ruta): Lee archivo y carga datos con validación
agregarCentro(CentroCultivo): Agrega nuevo centro con validación
buscarPorNombre(String): Búsqueda exacta
buscarPorNombreParcial(String): Búsqueda parcial
filtrarPorProduccion(double): Filtra por toneladas mínimas
filtrarPorComuna(String): Filtra por ubicación
filtrarPorEstado(String): Filtra por estado operacional
obtenerCentroMayorProduccion(): Encuentra el más productivo
obtenerCentroMenorProduccion(): Encuentra el menos productivo
calcularPromedioProduccion(): Estadística de promedio
calcularProduccionTotal(): Suma total de producción
ordenarPorProduccion(): Ranking descendente
obtenerComunasUnicas(): Lista de comunas sin duplicados


Características:

Manejo robusto de excepciones con try-catch
Uso de ArrayList<CentroCultivo> como colección
Validación línea por línea al cargar archivo
Uso de Streams para operaciones funcionales
Contador de errores durante carga



Clase ValidadorDatos (Librería)

Ubicación: util.ValidadorDatos
Propósito: Centraliza todas las validaciones del sistema
Métodos estáticos:

validarTextoNoVacio(String): Verifica texto no nulo ni vacío
validarNumeroPositivo(double): Verifica número > 0
validarNumeroNoNegativo(double): Verifica número >= 0
validarRango(int, int, int): Verifica valor en rango
validarFormatoToneladas(String): Valida conversión a double
validarEstado(String): Verifica estado permitido
validarFormatoLinea(String, int): Valida formato de archivo
normalizarTexto(String): Limpia y normaliza texto


Características:

Clase utilitaria reutilizable
Todos los métodos son static
Sin dependencias externas



  Clase FormateadorSalida (Libreria)

Ubicación: util.FormateadorSalida
Propósito: Estandariza la presentación en consola
Métodos estáticos:

imprimirEncabezado(String): Encabezados principales
imprimirSubtitulo(String): Subtítulos con lineas
imprimirCentroFormato(CentroCultivo, int): Formato tabla
imprimirEncabezadoTabla(): Encabezado de columnas
imprimirExito(String): Mensajes de exito
imprimirError(String): Mensajes de error
imprimirAdvertencia(String): Advertencias 
imprimirDetalleCompleto(CentroCultivo): Vista detallada con bordes
imprimirEstadisticas(...): Resumen estadístico

  Clase Main

Ubicación: app.Main
Propósito: Interfaz de usuario y control del flujo del programa
Funcionalidades:

Menú interactivo con 10 opciones
Validación de entrada del usuario
Llamadas a métodos de GestorCentros
Presentación de resultados con FormateadorSalida


  Características:

- Manejo completo de excepciones
- Navegación intuitiva
- Mensajes claros al usuario
- Banner de bienvenida profesional
