Duoc UC - Desarrollo Orientado a Objetos I
Evaluación Sumativa 3 - Semana 6

Autor del proyecto
Nombre completo: Gustavo Adolfo Gana Luna
Sección: I_002A

Carrera: Analista Programador Computacional
Sede: En Línea

Fecha de entrega: 01/12/2025

Descripción del Proyecto
El sistema permite gestionar información de unidades operativas de la empresa salmonera Salmontt mediante un modelo orientado a objetos con jerarquía de clases, implementando los principios de herencia simple, sobreescritura de métodos y composición entre clases.

Mejoras Implementadas
Arquitectura Orientada a Objetos:

Implementación de jerarquía de clases con herencia simple
Clase base UnidadOperativa con atributos comunes
Subclases especializadas: CentroCultivo y PlantaProceso
Uso correcto de super() en constructores
Sobreescritura de métodos con @Override

Reutilización de Código:

Atributos heredados desde la superclase
Comportamientos compartidos centralizados
Extensibilidad para nuevos tipos de unidades
Modelo escalable y mantenible

Mejoras de Diseño:

Separación clara de responsabilidades por paquetes
Encapsulamiento con modificadores de acceso apropiados
Métodos getters y setters en todas las clases
Representación textual profesional con toString()


- Estructura de Paquetes
Paquete model
Contiene la jerarquía de clases del dominio

UnidadOperativa.java: Clase base con atributos comunes (nombre, comuna)
CentroCultivo.java: Subclase especializada para centros de cultivo
PlantaProceso.java: Subclase especializada para plantas de procesamiento

Paquete data
Contiene la lógica de creación de datos

GestorUnidades.java: Crea instancias de prueba de las unidades operativas

Paquete ui
Contiene la interfaz de usuario

Main.java: Punto de entrada del programa y visualización de resultados


- Clases Implementadas
Clase UnidadOperativa (Superclase)
Ubicación: model.UnidadOperativa
Propósito: Representa una unidad operativa genérica de Salmontt
Atributos:

nombre (String): Nombre de la unidad operativa
comuna (String): Comuna donde se ubica la unidad

Características:

Atributos con modificador protected para acceso desde subclases
Constructor parametrizado que inicializa todos los atributos
Getters y setters para todos los atributos
Método toString() sobrescrito para representación textual


Clase CentroCultivo (Subclase)
Ubicación: model.CentroCultivo
Propósito: Representa un centro de cultivo de salmones
Hereda de: UnidadOperativa
Atributos propios:

toneladasProduccion (double): Toneladas de producción anual

Características:

Hereda nombre y comuna de la superclase
Constructor que utiliza super() para invocar al constructor padre
Getters y setters para atributos propios
Método toString() sobrescrito con anotación @Override
Muestra información completa incluyendo atributos heredados


Clase PlantaProceso (Subclase)
Ubicación: model.PlantaProceso
Propósito: Representa una planta de procesamiento de salmón
Hereda de: UnidadOperativa
Atributos propios:

capacidadProceso (int): Capacidad de procesamiento en toneladas/día

Características:

Hereda nombre y comuna de la superclase
Constructor que utiliza super() para invocar al constructor padre
Getters y setters para atributos propios
Método toString() sobrescrito con anotación @Override
Muestra información completa incluyendo atributos heredados y unidad de medida


Clase GestorUnidades
Ubicación: data.GestorUnidades
Propósito: Gestiona la creación de instancias de unidades operativas
Funcionalidades principales:

crearUnidadesPrueba(): Crea instancias de prueba de ambos tipos de unidades

Características:

Crea al menos 2 objetos de cada subclase (CentroCultivo y PlantaProceso)
Utiliza datos representativos de la Región de Los Lagos
Muestra por consola las unidades creadas usando toString()
Organización clara por tipo de unidad


Clase Main
Ubicación: ui.Main
Propósito: Punto de entrada del programa y visualización de resultados
Funcionalidades:

Banner de bienvenida del sistema
Instancia el GestorUnidades
Invoca el método de creación de unidades de prueba
Muestra resultados por consola de forma organizada

Características:

Presenta información de forma clara y estructurada
Separación visual entre tipos de unidades
Mensaje de confirmación de ejecución exitosa


Conceptos de POO Aplicados
Herencia Simple

CentroCultivo y PlantaProceso heredan de UnidadOperativa
Reutilización de atributos comunes (nombre, comuna)
Jerarquía clara: una superclase, dos subclases

Sobreescritura de Métodos

Todas las clases sobrescriben toString()
Uso correcto de la anotación @Override
Cada clase proporciona su propia implementación

Uso de super()

Constructores de subclases invocan al constructor de la superclase
Inicialización correcta de atributos heredados
Evita duplicación de código de inicialización

Encapsulamiento

Atributos protected en la superclase
Atributos private en las subclases
Acceso controlado mediante getters y setters


====== Ejecucion del Programa ======

- Pasos para ejecutar:

- Abrir el proyecto en el IDE
- Compilar todos los archivos .java
- Ejecutar la clase Main.java del paquete ui
- Observar la salida en consola con las unidades creadas
