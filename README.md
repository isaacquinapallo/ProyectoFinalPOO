 URL DE MANUAL DE USUARIO: 
 https://youtu.be/Pn0yschfBiQ
 
 Proyecto de Gestión para Tienda de Zapatos

 Descripción

Este proyecto es una solución integral para la gestión de una tienda de zapatos, diseñada para cumplir con los requisitos específicos de administración y manejo de transacciones de compra. Implementado en Java utilizando MongoDB para la gestión de datos, el sistema permite tanto a cajeros como a administradores interactuar con la base de datos de productos y ventas, realizar transacciones y generar informes.

 Índice

1. Main
   - Descripción general del punto de entrada de la aplicación.

2. Login
   - Proceso de autenticación para acceder a la interfaz de administrador o cajero.

3. CategoriaVendedor
   - Pantalla principal para la selección de productos y acceso a detalles adicionales.

4. CategoriaVendedor2
   - Vista detallada de los productos y opción para añadir al carrito.

5. Carrito
   - Gestión de productos seleccionados, incluyendo la adición o eliminación de ítems.

6. NotaDeVenta
   - Generación de la nota de venta en PDF al confirmar una compra.

7. IngresoAdmin
   - Interfaz para la selección de opciones administrativas.

8. AdminAñadirVendedores
   - Funcionalidad para buscar y agregar nuevos vendedores a través de su correo.

9. AdminEditarProductos
   - Opciones para añadir nuevos productos, modificar stock y eliminar productos existentes.

10. AdminVerVentasVendedores
    - Visualización de ventas realizadas y detalles específicos de cada venta y vendedor.

 Bases de Datos(ProyectoIsaacQuinapallo)

- Colecciones Permanentes:
  - clientes
  - compras
  - productos
  - userslogin

- Colecciones Temporales:
  - respaldo
  - carrito






1-Main

Descripción General
La clase Main es el punto de entrada de la aplicación de ventas. Esta clase configura y muestra la ventana inicial de la aplicación, que es la pantalla de inicio de sesión.
Componentes de la Clase
-JFrame frame: La ventana principal de la aplicación.
-Login login: Una instancia de la clase Login, que representa la pantalla de inicio de sesión.
Funcionalidad

1.Creación de la Ventana Principal:
-Se crea una instancia de JFrame llamada frame con el título "Login de Usuario".

2.Inicialización del Login:
-Se crea una instancia de la clase Login llamada login. La clase Login contiene la interfaz de usuario para la pantalla de inicio de sesión.

3.Configuración del Contenido de la Ventana:
-Se configura el contenido de la ventana frame usando el método setContentPane. Este método establece el panel principal de la clase Login (login.getFondoPanel()) como el contenido de la ventana. El método getFondoPanel devuelve el panel con el fondo de la pantalla de inicio de sesión.

4Configuración de Propiedades de la Ventana:
-setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE): Configura la operación de cierre de la ventana para que la aplicación se cierre cuando se cierra la ventana.
-setSize(400, 300): Establece el tamaño de la ventana a 400 píxeles de ancho y 300 píxeles de alto.
-setVisible(true): Hace visible la ventana.
![image](https://github.com/user-attachments/assets/bf1f05e6-5f98-40b7-bbb5-63362e06ca55)








2.-Login

 Descripción del Código

Este código define la clase Login, la cual maneja la interfaz gráfica y la funcionalidad de inicio de sesión de la aplicación. A continuación, se explica paso a paso:

1. Importaciones: El código importa varias librerías necesarias para conectarse a MongoDB, crear la interfaz gráfica y manejar eventos.

2. Comentarios de Prueba: Se incluyen comentarios con ejemplos de correos y contraseñas para pruebas.

3. Definición de la Clase Login:
    - Componentes de la Interfaz:
        - mainPanel: Panel principal.
        - textField1: Campo de texto para el correo electrónico.
        - passwordField1: Campo de texto para la contraseña.
        - loginButton: Botón para iniciar sesión.
        - datosIncorrectos: Etiqueta para mostrar mensajes de error.
        - main: Panel que contiene todos los componentes.
    - Conexión a MongoDB:
        - mongoClient: Cliente para conectarse a MongoDB.
        - collection: Colección de usuarios en la base de datos.

4. Clase Interna FondoPanel: Una clase interna que permite establecer una imagen de fondo en un panel.

5. Constructor Login():
    - Conexión a MongoDB: Se establece una conexión con la base de datos proyectoIsaacQuinapallo y se obtiene la colección userslogin.
    - Configuración de la Imagen de Fondo: Se configura una imagen de fondo para el panel principal.
    - Acción del Botón de Inicio de Sesión:
        - Al presionar el botón de inicio de sesión, se obtiene el correo y la contraseña ingresados por el usuario.
        - Se consulta la base de datos para verificar las credenciales del usuario.
        - Si las credenciales son correctas:
            - Se guarda el correo en un archivo local.
            - Se crea una nueva ventana (JFrame) que muestra opciones diferentes para el administrador y el cajero.
            - Se cierra la ventana de inicio de sesión.
        - Si las credenciales son incorrectas:
            - Se muestra un mensaje de error y se limpian los campos de texto.
    - Cerrar la Conexión con MongoDB: Al cerrar la aplicación, se cierra la conexión con MongoDB.

6. Método getFondoPanel(): Devuelve el panel principal con la imagen de fondo configurada.

![image](https://github.com/user-attachments/assets/839587fb-0016-4a19-80b2-b3553fe42ebd)

![LoginClass](https://github.com/user-attachments/assets/689e584b-d97d-42da-b983-9496f2251a13)






3.-CategoriasVendedor

 Descripción General
La clase CategoriasVendedor es una interfaz gráfica de usuario para una aplicación de ventas que permite a los usuarios ver y comprar productos categorizados como deportivos, casuales y formales. La interfaz está conectada a una base de datos MongoDB, de donde extrae los datos de los productos.

 Componentes de la Interfaz
- JPanel mainPanel: El panel principal que contiene todos los componentes de la interfaz.
- JButton deportivoButton, casualButton, formalButton: Botones para seleccionar la categoría de productos.
- JLabel tituloArticulo1, img1, precio1, categoria1, marca1: Etiquetas para mostrar información del primer producto.
- JLabel tituloArticulo2, img2, precio2, categoria2, marca2: Etiquetas para mostrar información del segundo producto.
- JLabel tituloArticulo3, img3, precio3, categoria3, marca3: Etiquetas para mostrar información del tercer producto.
- JButton comprarButton1, comprarButton2, comprarButton3: Botones para comprar el primer, segundo y tercer producto respectivamente.
- JMenuItem Carrito: Elemento de menú para acceder al carrito de compras.

 Conexión con MongoDB
La clase se conecta a una base de datos MongoDB llamada proyectoIsaacQuinapallo y a una colección llamada productos. Esta conexión se establece en el constructor de la clase.

 Funcionalidad
1. Configuración Inicial de Productos:
   - Al iniciar, la clase configura tres productos específicos usando el método handleCategoryButtonClick. Este método toma un ProductID y etiquetas correspondientes, y configura la información de los productos en la interfaz.

2. Manejo de Botones de Categoría:
   - Cada botón de categoría (deportivoButton, casualButton, formalButton) tiene un ActionListener que, al ser presionado, llama a handleCategoryButtonClick para configurar los productos de esa categoría en las etiquetas correspondientes.

3. Acción de Compra:
   - Cada botón de compra (comprarButton1, comprarButton2, comprarButton3) tiene un ActionListener que, al ser presionado, abre una nueva ventana CategoriaVendedor2 con los detalles del producto seleccionado.

4. Acceso al Carrito:
   - El elemento de menú Carrito permite acceder al carrito de compras, abriendo una nueva ventana que muestra los productos añadidos al carrito.

 Métodos Importantes
1. configureProduct:
   - Este método toma un ProductID y varias etiquetas (titulo, precio, categoria, marca, img). Busca en la base de datos el producto correspondiente y configura las etiquetas con la información del producto, incluyendo la descarga y visualización de la imagen del producto.

2. handleCategoryButtonClick:
   - Este método se usa para manejar los clics en los botones de categoría. Llama a configureProduct para cada producto, configurando así la interfaz con los productos de la categoría seleccionada.

![image](https://github.com/user-attachments/assets/fb981510-4dec-4a49-bb1b-e5cd8b3fc41d)

![CategoriasVendedorClass](https://github.com/user-attachments/assets/ac8a6be2-5435-402c-a76c-be4ae0a094a7)







4.-CategoriaVendedor2

 Descripción General
La clase CategoriaVendedor2 gestiona la interfaz gráfica para que los vendedores puedan visualizar los detalles de los productos y añadirlos al carrito. La clase se conecta a una base de datos MongoDB para obtener y mostrar la información del producto.

 Componentes de la Clase

 Variables de Instancia
- Componentes de la Interfaz Gráfica:
  - JPanel mainPanel: Panel principal de la interfaz.
  - JLabel imagenProducto: Etiqueta para mostrar la imagen del producto.
  - JButton añadirCarritoButton: Botón para añadir el producto al carrito.
  - JComboBox<String> colorbox: ComboBox para seleccionar el color del producto.
  - JComboBox<String> tamanobox: ComboBox para seleccionar el tamaño del producto.
  - JLabel nombretxt, marcatxt, categoriatxt, preciotxt, inventariotxt, descripciontxt, reseñastxt: Etiquetas para mostrar diferentes detalles del producto.
  - JSpinner cantidadSpinner: Spinner para seleccionar la cantidad del producto.

- Conexión con la Base de Datos:
  - MongoDatabase database: Base de datos MongoDB.
  - MongoCollection<Document> collection: Colección de productos en MongoDB.

 Funcionalidad

 Constructor CategoriaVendedor2(String tituloProducto)
1. Inicialización de la Conexión a MongoDB:
   - Se crea un cliente de MongoDB para conectarse a la base de datos proyectoIsaacQuinapallo.
   - Se obtiene la colección productos de la base de datos.

2. Configuración del Producto Basado en el Título:
   - Llama al método configureProductByTitle(tituloProducto) para configurar los detalles del producto.

3. Configuración de Listeners:
   - Añade ActionListener a los JComboBox (colorbox y tamanobox) para actualizar los detalles del producto cuando se selecciona un color o tamaño.
   - Añade ActionListener al botón añadirCarritoButton para manejar la acción de añadir al carrito.

 Método configureProductByTitle(String titulo)
1. Búsqueda del Producto:
   - Busca productos en la colección productos que coincidan con el nombre especificado (titulo).

2. Configuración de los Detalles del Producto:
   - Establece los detalles generales del producto (nombre, marca, categoría, precio, descripción, calificación, inventario, imagen).
   - Obtiene y configura los colores y tamaños únicos disponibles para el producto.

3. Actualización de la Interfaz Gráfica:
   - Actualiza las etiquetas y los JComboBox con la información del producto.
   - Carga y muestra la imagen del producto desde una URL.

 Método updateProductDetails(String titulo)
1. Obtención de la Selección de Color y Tamaño:
   - Obtiene los valores seleccionados en los JComboBox.

2. Búsqueda del Producto:
   - Busca el producto en la colección productos que coincida con el nombre, color y tamaño seleccionados.

3. Actualización de los Detalles del Producto:
   - Actualiza las etiquetas y la imagen del producto con los detalles obtenidos de la base de datos.

 Método handleComprarButton(String tituloProducto)
1. Validación de la Cantidad Seleccionada:
   - Valida que la cantidad seleccionada sea mayor a cero.

2. Validación de la Selección de Color y Tamaño:
   - Valida que se haya seleccionado un color y un tamaño.

3. Búsqueda y Validación del Inventario del Producto:
   - Busca el producto en la colección productos que coincida con el nombre, color y tamaño seleccionados.
   - Valida que haya suficiente inventario para la cantidad seleccionada.

4. Adición del Producto al Carrito:
   - Calcula el total de la compra.
   - Inserta un documento en la colección carrito con los detalles del producto y la cantidad seleccionada.

 Métodos Auxiliares

 Método getStringOrDefault(Document document, String key)
- Obtiene el valor de un documento para una clave específica. Si el valor es null, retorna "No disponible". Convierte el valor a String si es de otro tipo.

![image](https://github.com/user-attachments/assets/f5aaae4d-3a07-4396-bf76-25cebd7f7109)

![CategoriasVendedor2Class](https://github.com/user-attachments/assets/6ea6ad3b-1877-4636-9ca3-2f9ddb657845)







5.-Carrito

 Descripción General
La clase Carrito gestiona la interfaz gráfica para que los usuarios puedan visualizar y gestionar los productos en su carrito de compras. La clase se conecta a una base de datos MongoDB para obtener y actualizar la información de los productos en el carrito y calcular el total a pagar.

 Componentes de la Clase
Variables de Instancia
- Componentes de la Interfaz Gráfica:
  - JPanel mainPanel: Panel principal de la interfaz.
  - JTable carritoTable: Tabla para mostrar los productos en el carrito.
  - JButton pagarButton: Botón para proceder con el pago.
  - JSpinner cantidadSpinner: Spinner para seleccionar la cantidad de un producto.
  - JLabel TotalPagartxt: Etiqueta para mostrar el total a pagar.
  - JTextField productIDtxt: Campo de texto para ingresar el ProductID.
  - JButton editarButton: Botón para editar la cantidad de un producto en el carrito.
  - JLabel descripcionImagen: Etiqueta para mostrar la descripción del producto.
  - JLabel imagenProducto: Etiqueta para mostrar la imagen del producto.
  
- Conexión con la Base de Datos:
  - MongoDatabase database: Base de datos MongoDB.
  - MongoCollection<Document> carritoCollection: Colección de carrito en MongoDB.
  - MongoCollection<Document> productosCollection: Colección de productos en MongoDB.

 Funcionalidad
Constructor Carrito()
- Inicialización de la Conexión a MongoDB:
  - Crea un cliente de MongoDB y se conecta a la base de datos proyectoIsaacQuinapallo.
  - Obtiene las colecciones carrito y productos.

- Configuración de Listeners:
  - Añade DocumentListener a productIDtxt para actualizar la información del producto cuando se cambia el texto.
  - Configura los ActionListener para editarButton y pagarButton.

Método loadCarritoData()
- Carga los Datos del Carrito:
  - Obtiene todos los documentos de la colección carrito.
  - Rellena los datos en la tabla carritoTable.

Método actualizarInformacionProducto()
- Actualiza la Información del Producto:
  - Busca el producto en la colección productos por ProductID.
  - Actualiza la imagen y la descripción del producto en la interfaz.

Método editarProducto()
- Edita un Producto en el Carrito:
  - Actualiza la cantidad de un producto en el carrito o lo elimina si la cantidad es cero.
  - Añade el producto al carrito si no está presente y la cantidad es mayor que cero.

Método calcularTotalPagar()
- Calcula el Total a Pagar:
  - Suma los totales de todos los productos en el carrito y actualiza la etiqueta TotalPagartxt.

Método confirmarYProceder()
- Confirma la Compra y Procede:
  - Muestra la nota de venta.

Método reducirStock()
- Reduce el Stock de Productos:
  - Actualiza la cantidad en inventario de los productos según el carrito.
  - Vacía el carrito después de la compra.

Método mostrarNotaDeVenta()
- Muestra la Nota de Venta:
  - Crea y muestra la ventana de la nota de venta.

 Métodos Auxiliares
Método getStringOrDefault(Document document, String key)
- Obtiene el valor de un documento para una clave específica. Si el valor es null, retorna "No disponible". Convierte el valor a String si es de otro tipo.

![image](https://github.com/user-attachments/assets/2bd26985-88a3-454c-95c3-54fb58541aa8)

![CarritoClass](https://github.com/user-attachments/assets/4433eff8-feec-4f3f-a37a-2fc1c43d162f)






6.-NotaDeVenta

Descripción General
Esta clase se encarga de gestionar el proceso de generación de una nota de venta en una aplicación de gestión de productos. Integra funcionalidades para la conexión con MongoDB, la creación de notas de venta en PDF y la verificación de clientes.

 Componentes de la Clase

1. Interfaz de Usuario:
   - JPanel mainPanel: Panel principal que contiene los componentes gráficos.
   - JButton generarNotaDeVentaButton: Botón para generar una nota de venta.
   - JButton crearNuevoClienteButton: Botón para crear un nuevo cliente.
   - JButton generarSinClienteRegistradoButton: Botón para generar una nota de venta sin cliente registrado.
   - JTextField ClienteCedulaField1: Campo de texto para ingresar la cédula del cliente.
   - JLabel confirmadorClientetxt: Etiqueta para mostrar mensajes sobre el estado del cliente.

2. Conexión con MongoDB:
   - MongoDatabase database: Base de datos a la que se conecta la aplicación.
   - MongoCollection<org.bson.Document> clientesCollection: Colección de clientes en la base de datos.
   - MongoCollection<org.bson.Document> carritoCollection: Colección del carrito de compras.
   - MongoCollection<org.bson.Document> productosCollection: Colección de productos.
   - MongoCollection<org.bson.Document> comprasCollection: Colección de compras.

3. Carrito:
   - Carrito carrito: Instancia del carrito de compras.

 Métodos Principales

1. Constructor NotaDeVenta(Carrito carrito):
   - Inicializa la conexión con MongoDB, lee el correo electrónico del usuario, configura los componentes de la interfaz de usuario y define la funcionalidad de los botones.

2. Método initializeMongoDB():
   - Conecta a la base de datos MongoDB y accede a las colecciones necesarias.

3. Método readUserEmail():
   - Lee el correo electrónico del usuario desde un archivo.

4. Método clearUserFile():
   - Vacía el archivo que contiene el correo electrónico del usuario.

5. Método setupUIComponents():
   - Configura los componentes de la interfaz de usuario y agrega un DocumentListener al campo de cédula del cliente para verificar su existencia.

6. Método configureButtons():
   - Configura los eventos de acción para los botones de la interfaz de usuario.

7. Método verificarCliente():
   - Verifica si el cliente existe en la base de datos según la cédula proporcionada y actualiza el estado del botón de generación de nota de venta y el mensaje de confirmación.

8. Método updateConfirmadorCliente():
   - Actualiza el mensaje de confirmación del cliente y el estado del botón de generación de nota de venta.

9. Método generarNotaDeVenta():
   - Genera una nota de venta y muestra un cuadro de confirmación para proceder con la compra o cancelarla.

10. Método mostrarBotonesConfirmacionCompra():
    - Muestra un cuadro de confirmación con opciones para confirmar o cancelar la compra.

11. Método generarPDF():
    - Genera un archivo PDF con los detalles de la nota de venta, incluyendo información del vendedor, cliente y productos comprados.

12. Método addSellerDataToPDF():
    - Agrega datos del vendedor al PDF. Obtiene la información del usuario desde la colección userslogin y actualiza la fecha de la última venta.

13. Método updateLastSaleDate():
    - Actualiza la fecha de la última venta en la base de datos.

14. Método addClientDataToPDF():
    - Agrega datos del cliente al PDF. Si el cliente está registrado, se muestra su información; de lo contrario, se muestra un mensaje adecuado.

15. Método addProductsDataToPDF():
    - Agrega datos de los productos comprados al PDF. Muestra la información de cada producto en el carrito.

16. Método notificarStockReducido():
    - Llama al método reducirStock() del objeto Carrito para actualizar el stock de productos después de la compra.

17. Método crearNuevoCliente():
    - Permite crear un nuevo cliente a través de cuadros de diálogo, verificando si el cliente ya existe y agregando la información a la base de datos.

18. Método generarSinClienteRegistrado():
    - Genera una nota de venta sin cliente registrado, asignando una cédula predeterminada y mostrando información sobre el cliente como "Consumidor Final".

19. Método showErrorMessage():
    - Muestra un mensaje de error en un cuadro de diálogo.

20. Método showInformationMessage():
    - Muestra un mensaje de información en un cuadro de diálogo.

21. Método handleError():
    - Maneja los errores, mostrando un mensaje de error y la traza de la excepción.

22. Método registrarCompra():
    - Registra la compra en la colección compras de MongoDB, generando un nuevo CompraID, y añade cada ítem del carrito como un documento de compra.

![image](https://github.com/user-attachments/assets/d229fe7a-4504-4564-8e34-66f57cf347cd)

![image](https://github.com/user-attachments/assets/83a7c47c-99f1-4497-8c33-399e99671a3d)

![image](https://github.com/user-attachments/assets/1d941a00-5c98-487e-b395-0eda9702eb4e)

![NotaDeVentaClass](https://github.com/user-attachments/assets/1ec8e31d-86e3-4620-a707-7fa25c3040d1)








7.- IngresoAdmin

 Descripción General

La clase IngresoAdmin proporciona la interfaz gráfica para las tareas administrativas en la aplicación. Su propósito es permitir al administrador acceder a diversas funcionalidades a través de botones interactivos en una ventana principal. Cada botón abre una ventana independiente para realizar tareas específicas relacionadas con la gestión de productos, ventas y usuarios.


 Componentes de la Clase

 Variables de Instancia

- mainPanel: Un panel principal que actúa como contenedor para la interfaz gráfica de la clase IngresoAdmin.
- ingresarAlStockDeButton: Un botón que permite al administrador acceder a la funcionalidad para editar el stock de productos.
- revisarVentasDeVendedoresButton: Un botón que permite al administrador revisar las ventas realizadas por los vendedores.
- agregarUsuariosCajerosButton: Un botón que permite al administrador agregar nuevos usuarios o cajeros al sistema.

 Funcionalidad

 Constructor

El constructor de IngresoAdmin se encarga de configurar los botones y asignarles acciones específicas a través de ActionListener. Cada botón, al ser presionado, abre una ventana nueva con la funcionalidad correspondiente:

1. Botón ingresarAlStockDeButton:
    - Función: Abre una ventana que permite al administrador editar los productos en el stock.
    - Acción: Crea e instancia la clase AdminEditarProductos, que gestiona la edición de productos, y la muestra en una nueva ventana.

2. Botón revisarVentasDeVendedoresButton:
    - Función: Abre una ventana que permite al administrador revisar las ventas realizadas por los vendedores.
    - Acción: Crea e instancia la clase AdminVerVentasVendedores, que permite revisar las ventas, y la muestra en una nueva ventana.

3. Botón agregarUsuariosCajerosButton:
    - Función: Abre una ventana que permite al administrador agregar nuevos usuarios o cajeros.
    - Acción: Crea e instancia la clase AdminAñadirVendedores, que gestiona la adición de usuarios o cajeros, y la muestra en una nueva ventana.

![image](https://github.com/user-attachments/assets/dc39002b-bfb8-4111-a51c-ebc1035b447a)

![NotaDeVentaClass](https://github.com/user-attachments/assets/bfee21c0-2484-4693-b0ea-502d03df9340)






8.-AdminAñadirVendedores

Descripción General
La clase AdminAñadirVendedores gestiona la adición de nuevos vendedores a través de una interfaz gráfica en Java. Permite al administrador ingresar información relevante sobre un vendedor y validarla antes de almacenarla en una base de datos MongoDB. La clase incluye funcionalidades para validar y registrar la información del vendedor, así como para manejar errores y mostrar mensajes de confirmación.

Componentes de la Clase

Variables de Instancia

-mainPanel: El panel principal que contiene la interfaz gráfica de la clase.
-crearNuevoVendedorButton: Un botón que, al ser presionado, crea un nuevo vendedor.
-correoTextField: Un campo de texto para ingresar el correo electrónico del nuevo vendedor.
-confirmadorVendedorTxt: Una etiqueta que muestra mensajes de confirmación sobre la validez del correo electrónico.
-database: Instancia de la base de datos de MongoDB donde se almacena la información.
-usersCollection: Colección de documentos en MongoDB que contiene la información de los usuarios.

Funcionalidad

Constructor

-AdminAñadirVendedores(): Configura la interfaz gráfica y la conexión a MongoDB. Llama a los métodos initializeMongoDB(), setupUIComponents(), y configureButtons().

Métodos

-initializeMongoDB(): Establece la conexión con la base de datos MongoDB y obtiene la colección userslogin. Maneja posibles errores de conexión y los reporta.
-setupUIComponents(): Configura los componentes de la interfaz gráfica, incluyendo la adición de un DocumentListener al campo de texto del correo electrónico para verificar la validez del correo en tiempo real.
-configureButtons(): Configura el botón crearNuevoVendedorButton para que al hacer clic ejecute el método crearNuevoVendedor().
-verificarCorreo(): Verifica la validez del correo electrónico ingresado en correoTextField. Usa expresiones regulares para validar el formato del correo y consulta la base de datos para asegurar que no esté registrado previamente. Actualiza el mensaje de confirmación según la disponibilidad del correo.
-esCorreoValido(String correo): Verifica si el correo electrónico cumple con un patrón específico de formato.
-esTelefonoValido(String telefono): Verifica si el número de teléfono es válido según un patrón de 10 dígitos.
-esContrasenaValida(char[] contrasena): Verifica si la contraseña cumple con requisitos específicos, como longitud mínima, presencia de mayúsculas, números y caracteres especiales.
-esNombreValido(String nombre): Verifica si el nombre es válido, permitiendo solo letras y acentos.
-esDireccionValida(String direccion): Verifica si la dirección no está vacía.
-updateConfirmadorVendedor(String message, Color color, boolean enableButton): Actualiza el mensaje de confirmación y el estado del botón crearNuevoVendedorButton basado en el resultado de la verificación.
-crearNuevoVendedor(): Solicita y valida información adicional sobre el vendedor, incluyendo nombre, apellido, dirección, teléfono y contraseña. Genera un nuevo UserID y almacena la información del vendedor en la base de datos. Muestra mensajes de éxito o error según el resultado de la operación.
-capitalizar(String texto): Capitaliza la primera letra del texto y pone el resto en minúsculas.
-showErrorMessage(String message): Muestra un mensaje de error en un cuadro de diálogo.
-showInformationMessage(String message): Muestra un mensaje de información en un cuadro de diálogo.
-handleError(String message, Exception e): Maneja errores, mostrando el mensaje en la consola y rastreando la pila de excepciones.

![image](https://github.com/user-attachments/assets/4ce2345f-642a-4224-a6ac-05e652343c75)

![image](https://github.com/user-attachments/assets/944a273d-f2a6-4403-beb4-5dde9674972f)

![image](https://github.com/user-attachments/assets/cfd372ad-3995-49d8-93ee-ef78570fd73f)

![image](https://github.com/user-attachments/assets/6ddd672f-922a-44a0-b27a-a9f20c60c505)

![image](https://github.com/user-attachments/assets/ea9ed003-f788-4074-aa61-ad7d3e833879)

![AdminAñadirVendedoresClass](https://github.com/user-attachments/assets/4a86eb2f-d681-4a00-b89b-76fb87a9f2a4)







9.-AdminEditarProductos

Descripción General
La clase AdminEditarProductos se encarga de gestionar una interfaz gráfica para administrar productos en una aplicación de Java que utiliza MongoDB como base de datos. Esta clase incluye funcionalidades para editar, añadir, eliminar y mostrar productos.
Componentes
Variables de Interfaz Gráfica:
-mainPanel: Panel principal de la interfaz.
-productosTable: Tabla que muestra los productos.
-productIDtxt: Campo de texto para ingresar el ProductID.
-cantidadSpinner: Componente para seleccionar la cantidad de productos.
-editarButton: Botón para editar un producto.
-imagenProducto: Etiqueta para mostrar la imagen del producto.
-descripcionImagen: Etiqueta para mostrar la descripción del producto.
-añadirButton: Botón para añadir una cantidad adicional de un producto existente.
-descripcionAñadidoModificadoTxt: Etiqueta para mostrar mensajes sobre la adición o modificación del producto.
-gregarProductosNuevosButton: Botón para añadir nuevos productos.
-EliminarProductosbutton: Botón para eliminar un producto.

Variables de Conexión a MongoDB:
-database: Representa la base de datos en MongoDB.
-productosCollection: Colección de productos en la base de datos.
-respaldoCollection: Colección de respaldo para almacenar una copia de los productos.

Constructor (AdminEditarProductos)

Inicialización de MongoDB:
-Se crea una conexión con MongoDB y se accede a la base de datos proyectoIsaacQuinapallo y sus colecciones productos y respaldo.
Verificación y Copia de Respaldo:
-Se llama al método verificarYCopiarRespaldo() para comprobar si la colección respaldo está -vacía o si deben pasar 24 horas desde la última copia. Si es necesario, se copia la colección productos a respaldo con la fecha actual.
Carga de Datos de Productos:
-Se carga la información de productos en la tabla mediante el método loadProductosData().

Configuración de Listeners y Acciones:
-Se añaden DocumentListener al campo productIDtxt para actualizar la información del producto cuando se cambia el ID.
-Se configuran los ActionListener para los botones para manejar la edición, adición, eliminación y adición de nuevos productos.
Métodos

verificarYCopiarRespaldo()
-Verifica si la colección respaldo está vacía. Si está vacía o han pasado más de 24 horas desde la última copia, elimina la colección y copia los datos de productos a respaldo con la fecha actual.
copiarDatosConFechaActual()
-Copia los datos de la colección productos a respaldo, añadiendo la fecha actual a cada documento.
loadProductosData()
-Carga los datos de la colección productos en la tabla productosTable, creando y configurando las columnas y filas con la información de cada producto.
actualizarInformacionProducto()
-Actualiza la imagen y descripción del producto en la interfaz gráfica basándose en el ProductID ingresado.
editarProducto()
-Actualiza la cantidad en inventario de un producto existente basado en el ProductID y la cantidad proporcionada. Muestra un mensaje de éxito o error.
añadirProducto()
-Añade una cantidad adicional al inventario de un producto existente. Actualiza la cantidad y muestra un mensaje de éxito o error.
agregarNuevoProducto()
-Permite al usuario ingresar todos los detalles de un nuevo producto y lo añade a la colección productos. Utiliza varios métodos auxiliares para obtener valores válidos del usuario.
obtenerProductID()
-Solicita al usuario un ProductID, asegurándose de que no esté vacío, contenga al menos una letra y un número, y no esté repetido en la colección.
productIDExists(String productID)
-Verifica si un ProductID ya existe en la colección productos.
obtenerValorNoVacio(String mensaje)
-Solicita un valor al usuario y asegura que no esté vacío.
obtenerValorEntero(String mensaje)
-Solicita un valor entero al usuario y maneja errores de formato.
obtenerValorDecimal(String mensaje)
-Solicita un valor decimal al usuario y maneja errores de formato.
eliminarProducto()
-Elimina un producto basado en el ProductID ingresado después de confirmación del usuario. Muestra un mensaje de éxito o error.

Manejo de Errores

-En caso de errores durante las operaciones con la base de datos, se muestra un mensaje de error al usuario usando JOptionPane.

![image](https://github.com/user-attachments/assets/9c6a406c-d696-49db-9f37-2206c5985161)

![image](https://github.com/user-attachments/assets/ee938696-2ace-4a17-8568-061373fbe578)

![image](https://github.com/user-attachments/assets/241caef5-0e5f-45b4-ba0b-e5279c6dd837)

![image](https://github.com/user-attachments/assets/76467bcd-3b0b-4797-8bea-330c0996e2d4)

![image](https://github.com/user-attachments/assets/4cf012d2-e571-4b97-a7c6-98ff9740cc8f)

![AdminEditarProductosClass](https://github.com/user-attachments/assets/f7d1d738-f3bc-4a69-a91c-bc19ac0fe486)





10.-AdminVerVentasVendedores

Descripción General
La clase AdminVerVentasVendedores gestiona una interfaz gráfica para la visualización y búsqueda de ventas en una aplicación Java que utiliza MongoDB como base de datos. Permite a los administradores revisar un resumen de todas las compras y buscar detalles específicos usando el CompraID o UserID.

 Componentes

Variables de Interfaz Gráfica:
- mainPanel: Panel principal que contiene todos los elementos de la interfaz gráfica.
- tablaGeneral: Tabla para mostrar un resumen general de las compras.
- busquedaField: Campo de texto para ingresar el CompraID a buscar.
- buscarCompraButton: Botón para iniciar la búsqueda por CompraID.
- tablaEspecifica: Tabla para mostrar detalles específicos de una compra.
- confirmacionCompra: Etiqueta para mostrar mensajes de confirmación o error relacionados con las búsquedas.
- busquedafield2: Campo de texto para ingresar el UserID a buscar.
- buscarButton2: Botón para iniciar la búsqueda por UserID.

Variables de Conexión a MongoDB:
- comprasCollection: Colección de MongoDB que contiene los documentos de compras.

 Constructor (AdminVerVentasVendedores)

Inicialización de MongoDB:
- Se establece una conexión con MongoDB utilizando MongoClients.create("mongodb://localhost:27017").
- Se accede a la base de datos proyectoIsaacQuinapallo y a la colección compras.

Configuración de Tablas:
- Se llama al método configurarTablas() para configurar las tablas tablaGeneral y tablaEspecifica con los encabezados apropiados.

Carga de Datos Iniciales:
- Se llama al método cargarDatosTablaGeneral() para cargar los datos iniciales en la tabla general.

Configuración de Listeners y Acciones:
- Se añaden ActionListener a los botones de búsqueda para manejar las acciones cuando el usuario realiza una búsqueda por CompraID o UserID.

 Métodos

configurarTablas()
- Configura los encabezados de las tablas tablaGeneral y tablaEspecifica.
  - tablaGeneral: Encabezados incluyen CompraID, UserID, ClienteID, Cantidad Total, Total Precio, Fecha Compra.
  - tablaEspecifica: Encabezados incluyen CompraID, UserID, ClienteID, ProductID, Cantidad, Total, FechaCompra.

cargarDatosTablaGeneral()
- Recupera todos los documentos de la colección compras.
- Procesa los documentos para acumular datos por CompraID, sumando las cantidades y los totales.
- Llena la tabla tablaGeneral con los datos procesados.

buscarYMostrarCompra()
- Obtiene el CompraID ingresado y verifica si es un número entero.
- Busca documentos en la colección compras con el CompraID especificado.
- Llena la tabla tablaEspecifica con los detalles de la compra encontrada.
- Muestra un mensaje en confirmacionCompra indicando si se encontró la compra o no.

buscarYMostrarPorUserID()
- Obtiene el UserID ingresado.
- Busca documentos en la colección compras con el UserID especificado.
- Llena la tabla tablaEspecifica con los detalles de las compras encontradas.
- Muestra un mensaje en confirmacionCompra indicando si se encontraron compras para el UserID o no.

 Manejo de Errores
- Si el campo CompraID o UserID está vacío, se muestra un mensaje de error en confirmacionCompra.
- Si el CompraID ingresado no es un número entero, se muestra un mensaje de error correspondiente.
- En caso de que no se encuentren compras según los criterios de búsqueda, se muestra un mensaje de confirmación o error en confirmacionCompra.

![image](https://github.com/user-attachments/assets/b7144ac5-63de-4f2b-8d2b-30cc46910027)

![image](https://github.com/user-attachments/assets/dece7be0-62c0-479b-ba22-13f036a06858)

![image](https://github.com/user-attachments/assets/04a69d12-e04f-4f9c-bc06-1055c8fa9f79)

![AdminVerVentasVendedoresClass](https://github.com/user-attachments/assets/47a5a1cc-56c8-48b7-9346-138d57b16463)





11.-Bases de Datos

Base de datos

 Colecciones Permanentes

1. clientes
- Propósito: Almacena la información de los clientes registrados en el sistema. Esto puede incluir datos como el nombre del cliente, cédula, dirección, y contacto.
- Uso en la Aplicación: Se utiliza para identificar y gestionar a los clientes durante las transacciones y para generar informes relacionados con las compras de los clientes.

2. compras
- Propósito: Guarda los registros de las compras realizadas en la tienda. Incluye detalles como el CompraID, UserID (identificador del cajero), ClienteID, ProductID, cantidad comprada, total de la compra, y la fecha de la compra.
- Uso en la Aplicación: Facilita la generación de informes sobre las ventas realizadas, permite la visualización de datos de compras específicas, y ayuda en la revisión y análisis de ventas por parte de los administradores.

3. productos
- Propósito: Contiene la información de los productos disponibles en la tienda. Incluye detalles como el ProductID, nombre, marca, categoría, tamaño, color, precio, inventario, descripción, URL de la imagen, y calificación.
- Uso en la Aplicación: Se utiliza para mostrar información sobre los productos disponibles en la tienda, manejar el inventario, y facilitar la gestión de productos en la interfaz de usuario para tanto administradores como cajeros.

4. userslogin
- Propósito: Almacena los datos de los usuarios que tienen acceso al sistema, incluyendo tanto administradores como cajeros. Los datos pueden incluir el UserID, contraseñas (encriptadas), y roles de usuario.
- Uso en la Aplicación: Se utiliza para gestionar el acceso al sistema, autenticar a los usuarios durante el login, y asegurar que los diferentes usuarios tengan los permisos adecuados según su rol.

 Colecciones Temporales

1. respaldo
- Propósito: Sirve como una copia de respaldo de la colección productos. Esta colección se actualiza periódicamente para mantener una copia de seguridad con la fecha actual añadida a cada documento.
- Uso en la Aplicación: Garantiza la integridad y disponibilidad de los datos en caso de fallos o pérdida de datos en la colección principal productos. Se actualiza automáticamente cada 24 horas si es necesario.

2. carrito
- Propósito: Almacena temporalmente los productos que los clientes añaden a su carrito de compras antes de realizar la compra final.
- Uso en la Aplicación: Facilita la gestión del proceso de compra, permitiendo a los clientes revisar y modificar su selección de productos antes de proceder a la compra. Los datos en esta colección se utilizan para calcular el total de la compra y para mostrar el contenido del carrito al cliente.


![image](https://github.com/user-attachments/assets/8acfb1cc-2454-41a6-9285-7c9aaf9dfe1d)





