Sistema de Gestión Salmontt
Duoc UC - Desarrollo Orientado a Objetos I

Autor del proyecto

Nombre completo: Gustavo Adolfo Gana Luna
Sección: I_002A
Carrera: Analista Programador Computacional
Sede: En Línea


Descripción General
Sistema básico desarrollado en Java para la empresa salmonera Salmontt, con sede en Puerto Montt. Este sistema permite cargar, organizar y gestionar la información de los centros de cultivo mediante la lectura de datos desde archivos externos, aplicando operaciones de recorrido, búsqueda y filtrado sobre colecciones dinámicas (ArrayList).
El proyecto implementa un modelo orientado a objetos que vincula archivos externos con instancias de clases, fortaleciendo la manipulación de estructuras de datos y la gestión de información del negocio.

Paquetes:

Paquete model
Contiene las clases del dominio que representan las entidades del negocio salmonero.
Paquete data
Contiene la clase encargada de gestionar la lectura de archivos y operaciones sobre las colecciones de datos.
Paquete ui
Contiene la clase principal para ejecutar el sistema y mostrar los resultados.
Carpeta resources
Almacena los archivos de datos externos (.txt) utilizados por el sistema.

Clases Implementadas
Clase CentroCultivo

Ubicación: model.CentroCultivo
Propósito: Representa un centro de cultivo de salmón con sus características principales.
Atributos:

nombre (String): Nombre del centro de cultivo
comuna (String): Ubicación geográfica del centro
toneladasProducidas (double): Producción anual en toneladas


Métodos: Getters, Setters, Constructores y toString()

Clase GestorDatos

Ubicación: data.GestorDatos
Propósito: Gestiona la lectura de datos desde archivos y operaciones sobre la colección de centros de cultivo.
Funcionalidades:

cargarDatos(String rutaArchivo): Lee el archivo .txt y crea objetos CentroCultivo
getCentros(): Retorna la lista completa de centros
filtrarPorProduccion(double toneladas): Filtra centros con producción mayor al valor indicado
obtenerCentroMayorProduccion(): Identifica el centro más productivo
filtrarPorComuna(String comuna): Filtra centros por ubicación geográfica



Clase Main

Ubicación: ui.Main
Propósito: Clase principal para ejecutar y probar el sistema completo.
Operaciones implementadas:

Recorrido completo de todos los centros de cultivo
Filtrado de centros con producción mayor a 1000 toneladas
Identificación del centro de mayor producción
Filtrado de centros por comuna específica

Repositorio GitHub
https://github.com/Komoriiiii/Proyectos-Universidad/tree/salmontt-colecciones

Información Adicional

Fecha de entrega: 17/11/2025
Actividad: Construyendo una lista de objetos desde archivo

Desarrollado por
Gustavo Adolfo Gana Luna
Analista Programador Computacional
Duoc UC - Sede En Línea
