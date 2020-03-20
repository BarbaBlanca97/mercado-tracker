# Requisitos
## Detalles tecnicos

**Presupuesto:** 0

## Usuarios / Destinatarios

Cualquier persona puede crearse una cuenta y usar la aplicacion

## Descripcion general
Es una aplicacion web para seguir precios de mercadolibre y notificar en caso de que cambien.
Debe permitir la creacion de cuentas de usuario, así como la edición de los datos personales, y la eliminacion de una cuenta.
Un usuario con cuenta creada puede ingresar y ver los productos que sigue, editar sus parametros de configuracion, pausar sus notificaciones y eliminarlos.
Cuando el precio de un determinado producto cambie, se notificara via mail a todos los usuarios siguiendo a ese producto (en base a la configuracion del seguimiento).

## Interfaz

Se cuenta con 3 pantallas:
- Bienvenida
- Inicio
- Configuracion de cuenta
- Verificacion de correo electrónico

### Bienvenida

Se muestra el nombre de la aplicacion y una descripcion breve de lo que hace.
Permite el ingreso a usuarios registrados asi como registro de nuevos usuarios, desplegando el formulario corresponiente.

### Inicio

Se muestran los productos seguidos en forma de tarjetas, con su imagen y nombre correspondiente, con controles para editar el umbral de notificación, pausar el seguimiento y eliminar el producto.
Tambien se muestran controles para seguir un nuevo producto y acceder a la pantalla de gestión de cuenta.

### Configuracion de cuenta

Se muestran los datos actuales del usuario que son:
- Nombre
- Correo electrónico

Se muestran controles para editar los campos anteriormente mencionados sumando unos para cambiar la contraseña y eliminar la cuenta.

## Datos

### Sobre los productos

- Id
- El nombre
- Una imagen
- **Precio actual**
- Variación total
- Fecha de inicio del seguimiento

### Sobre el usuario

- Un nombre
- Correo electrónico
- Clave de acceso (hash)
- Productos que sigue


## Funcionalidades

### Seguimiento

- Seguir un producto introduciendo su link
- Cuando el precio de un producto seguido varía se le avisa al usuario por correo
- Decidir cual es la variacion de precio mínima para ser notificado
- Pausar las notificaciones de una publicación
- Eliminar un producto del seguimiento

### Usuario

- Cambiar todos los datos personales
- Eliminar cuenta