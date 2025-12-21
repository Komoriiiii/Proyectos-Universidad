Duoc UC - Desarrollo Orientado a Objetos I

<div>

Autor del proyecto
Nombre completo: Gustavo Adolfo Gana Luna
Sección: I_002A
Carrera: Analista Programador Computacional
Sede: En Línea

</div>

Descripción General
Sistema de gestión desarrollado en Java para la empresa salmonera Salmontt, ubicada en Puerto Montt, que permite administrar y organizar información de múltiples entidades operativas mediante programación orientada a objetos. El sistema implementa herencia, interfaces, polimorfismo, composición, colecciones genéricas y manejo de excepciones personalizadas para facilitar la gestión de empleados, clientes, proveedores, productos y órdenes de compra.

<div>

📦 Paquetes
1. Paquete model
Contiene las clases del dominio que representan las entidades del negocio, las interfaces de comportamiento común y las excepciones personalizadas.
2. Paquete utils
Contiene herramientas auxiliares para la gestión de archivos y utilidades del sistema.
3. Paquete App
Contiene la clase principal Main que orquesta el funcionamiento completo del sistema.

<div>

🎯 Interfaces Implementadas
Interfaz Registrable
Ubicación: model.Registrable
Propósito: Define el contrato común que todas las personas del sistema deben cumplir.
Métodos:

void registrar(): Registra la entidad en el sistema
String mostrarDatos(): Retorna información completa formateada de la entidad
String obtenerIdentificador(): Retorna el identificador único (RUT)


<div>

🏢 Clases Implementadas
1. Clase Rut
Ubicación: model.Rut
Propósito: Representa y valida un RUT chileno con su dígito verificador.
Características principales:

Validación mediante algoritmo Módulo 11
Formato automático con guión separador
Lanza RutInvalidoException si el RUT es inválido

Atributos principales:

String numero: Número del RUT sin dígito verificador
char digitoVerificador: Dígito verificador (0-9 o K)


2. Clase RutInvalidoException
Ubicación: model.RutInvalidoException
Propósito: Excepción personalizada para manejar errores de validación de RUT.
Hereda de: Exception

3. Clase Direccion
Ubicación: model.Direccion
Propósito: Representa una dirección física completa.
Atributos principales:

String calle: Nombre de la calle
String numero: Número de domicilio
String comuna: Comuna
String ciudad: Ciudad
String region: Región


4. Clase Abstracta Persona
Ubicación: model.Persona
Propósito: Clase base abstracta para todas las personas del sistema.
Implementa: Registrable
Atributos principales:

String nombre: Nombre de la persona
String apellido: Apellido de la persona
Rut rut: RUT validado
String telefono: Teléfono de contacto
String email: Correo electrónico
Direccion direccion: Dirección completa (composición)

Métodos principales:

String getNombreCompleto(): Retorna nombre y apellido concatenados
String obtenerIdentificador(): Retorna el RUT completo


5. Clase Empleado
Ubicación: model.Empleado
Propósito: Representa un empleado de Salmontt.
Hereda de: Persona
Atributos específicos:

String cargo: Cargo que desempeña
double salario: Salario mensual
String fechaContratacion: Fecha de contratación
String departamento: Departamento al que pertenece

Métodos sobrescritos:

@Override void registrar(): Registra el empleado con mensaje personalizado
@Override String mostrarDatos(): Muestra información completa del empleado


6. Clase Cliente
Ubicación: model.Cliente
Propósito: Representa un cliente de Salmontt.
Hereda de: Persona
Atributos específicos:

String codigoCliente: Código único del cliente
String tipoCliente: Tipo de cliente (MAYORISTA, MINORISTA)
double limiteCredito: Límite de crédito asignado

Métodos sobrescritos:

@Override void registrar(): Registra el cliente con su código
@Override String mostrarDatos(): Muestra información completa del cliente


7. Clase Proveedor
Ubicación: model.Proveedor
Propósito: Representa un proveedor externo de Salmontt.
Hereda de: Persona
Atributos específicos:

String razonSocial: Razón social de la empresa
String rubro: Rubro del proveedor
String codigoProveedor: Código único del proveedor

Métodos sobrescritos:

@Override void registrar(): Registra el proveedor con su razón social
@Override String mostrarDatos(): Muestra información completa del proveedor


8. Clase Producto
Ubicación: model.Producto
Propósito: Representa un producto de salmón comercializado por Salmontt.
Atributos principales:

String codigo: Código único del producto
String nombre: Nombre del producto
String categoria: Categoría del producto
double precio: Precio por unidad
int stockDisponible: Cantidad disponible en inventario
String unidadMedida: Unidad de medida (kg, unidades, etc.)

Métodos principales:

void agregarStock(int cantidad): Aumenta el stock disponible
boolean reducirStock(int cantidad): Reduce el stock si hay suficiente disponible


9. Clase ItemOrden
Ubicación: model.ItemOrden
Propósito: Representa un ítem individual dentro de una orden de compra.
Atributos principales:

Producto producto: Producto asociado al ítem (composición)
int cantidad: Cantidad solicitada
double precioUnitario: Precio unitario al momento de la orden

Métodos principales:

double calcularSubtotal(): Calcula cantidad × precioUnitario


10. Clase OrdenDeCompra
Ubicación: model.OrdenDeCompra
Propósito: Representa una orden de compra completa realizada por un cliente.
Atributos principales:

String numeroOrden: Número único de la orden
Cliente cliente: Cliente que realiza la orden (composición)
String fecha: Fecha de la orden
List<ItemOrden> items: Lista de ítems de la orden
String estado: Estado actual (PENDIENTE, PROCESADA, ERROR)

Métodos principales:

void agregarItem(ItemOrden item): Agrega un ítem a la orden
double calcularTotal(): Suma todos los subtotales de los ítems
void procesarOrden(): Procesa la orden y actualiza el stock de productos


11. Clase GestorArchivos
Ubicación: utils.GestorArchivos
Propósito: Proporciona métodos para lectura y escritura de archivos de texto.
Métodos principales:

static List<String> leerArchivo(String nombreArchivo): Lee líneas de un archivo
static void escribirArchivo(String nombreArchivo, String contenido): Escribe contenido en un archivo


12. Clase Main
Ubicación: App.Main
Propósito: Clase principal que gestiona todas las entidades del sistema y demuestra su funcionamiento.
Características principales:

Utiliza ArrayList<Registrable> para almacenar personas polimórficamente
Implementa HashMap<String, Producto> para búsqueda eficiente de productos
Utiliza Stack<OrdenDeCompra> para gestionar órdenes pendientes (LIFO)
Diferencia tipos de objetos usando instanceof
Genera listados específicos por tipo de entidad

Métodos principales:

void agregarRegistrable(Registrable registro): Agrega una persona al sistema
void agregarProducto(Producto producto): Agrega un producto al catálogo
Producto buscarProducto(String codigo): Busca un producto por código
void agregarOrden(OrdenDeCompra orden): Agrega una orden al stack
void procesarUltimaOrden(): Procesa la última orden agregada
void listarEmpleados(): Lista solo empleados usando instanceof
void listarClientes(): Lista solo clientes usando instanceof
void listarProductos(): Lista todos los productos del catálogo

<div>

📅 Información de Entrega
Fecha de entrega: 21/12/2025
Asignatura: Desarrollo Orientado a Objetos I
Institución: Duoc UC
Tipo de evaluación: Evaluación Final Transversal (EFT)
