# Duoc UC - Desarrollo Orientado a Objetos I

## Autor del proyecto
* **Nombre completo:** Gustavo Adolfo Gana Luna
* **Sección:** I_002A
* **Carrera:** Analista Programador Computacional
* **Sede:** En Línea

---

## Descripción General
Sistema de gestión desarrollado en Java para la empresa salmonera Salmontt, que permite administrar y organizar información de múltiples entidades operativas mediante programación orientada a objetos. El sistema implementa interfaces, polimorfismo, colecciones genéricas y una interfaz gráfica de usuario para facilitar la gestión de centros de cultivo, plantas de proceso, proveedores, empleados y equipos de transporte.

---

## 📦 Paquetes

### 1. Paquete `model`
Contiene las clases del dominio que representan las entidades del negocio y la interfaz de comportamiento común.

### 2. Paquete `data`
Contiene la clase gestora que administra la colección de entidades y aplica lógica de negocio.

### 3. Paquete `ui`
Contiene la interfaz gráfica de usuario del sistema.

---

## 🎯 Interfaces Implementadas

### Interfaz Registrable
* **Ubicación:** `model.Registrable`
* **Propósito:** Define el contrato común que todas las entidades deben cumplir mediante el método `mostrarResumen()`.
* **Método:**
  - `String mostrarResumen()`: Retorna un resumen formateado de la entidad.

---

## 🏢 Clases Implementadas

### 1. Clase CentroCultivo
* **Ubicación:** `model.CentroCultivo`
* **Propósito:** Representa un centro de cultivo de salmones.
* **Implementa:** `Registrable`
* **Atributos principales:**
  - `String id`: Identificador único del centro
  - `String nombre`: Nombre del centro
  - `String ubicacion`: Ubicación geográfica
  - `int jaulas`: Cantidad de jaulas disponibles

### 2. Clase PlantaProceso
* **Ubicación:** `model.PlantaProceso`
* **Propósito:** Representa una planta de procesamiento de salmón.
* **Implementa:** `Registrable`
* **Atributos principales:**
  - `String id`: Identificador único de la planta
  - `String nombre`: Nombre de la planta
  - `String ubicacion`: Ubicación geográfica
  - `int capacidad`: Capacidad de procesamiento en toneladas por día

### 3. Clase Proveedor
* **Ubicación:** `model.Proveedor`
* **Propósito:** Representa un proveedor externo de servicios o productos.
* **Implementa:** `Registrable`
* **Atributos principales:**
  - `String rut`: RUT del proveedor
  - `String nombre`: Razón social
  - `String servicio`: Tipo de servicio que ofrece

### 4. Clase Empleado
* **Ubicación:** `model.Empleado`
* **Propósito:** Representa un empleado de la empresa.
* **Implementa:** `Registrable`
* **Atributos principales:**
  - `String rut`: RUT del empleado
  - `String nombre`: Nombre completo
  - `String cargo`: Cargo que desempeña
  - `double salario`: Salario mensual

### 5. Clase GestorEntidades
* **Ubicación:** `data.GestorEntidades`
* **Propósito:** Administra la colección de todas las entidades del sistema y aplica lógica de clasificación mediante `instanceof`.
* **Características principales:**
  - Utiliza `ArrayList<Registrable>` para almacenar objetos polimórficamente
  - Implementa métodos para agregar, listar y clasificar entidades
  - Diferencia tipos de objetos en tiempo de ejecución usando `instanceof`
  - Genera estadísticas por tipo de entidad

### 6. Clase SalmonttApp
* **Ubicación:** `ui.SalmonttApp`
* **Propósito:** Clase principal que proporciona la interfaz gráfica de usuario mediante `JOptionPane`.
* **Funcionalidades:**
  - Menú interactivo con opciones numeradas
  - Formularios de ingreso para cada tipo de entidad
  - Visualización de todas las entidades registradas
  - Validación de datos de entrada
  - Contador dinámico de entidades totales

---

## 🔍 Conceptos de POO Aplicados

### Polimorfismo
El sistema utiliza polimorfismo mediante la interfaz `Registrable`, permitiendo almacenar diferentes tipos de objetos en una única colección `ArrayList<Registrable>`.

### Uso de instanceof
Se implementa el operador `instanceof` en la clase `GestorEntidades` para identificar el tipo específico de cada objeto durante la ejecución y aplicar lógica diferenciada:
```java
if (entidad instanceof CentroCultivo) {
    CentroCultivo centro = (CentroCultivo) entidad;
    // Lógica específica para centros
} else if (entidad instanceof Proveedor) {
    Proveedor prov = (Proveedor) entidad;
    // Lógica específica para proveedores
}
```

### Colecciones Genéricas
Uso de `ArrayList<Registrable>` para gestionar múltiples tipos de objetos de forma unificada y segura.

### Encapsulamiento
Todos los atributos son privados con acceso mediante métodos getters públicos.


## 📅 Información de Entrega

* **Fecha de entrega:** 15/12/2025
* **Asignatura:** Desarrollo Orientado a Objetos I
* **Institución:** Duoc UC
