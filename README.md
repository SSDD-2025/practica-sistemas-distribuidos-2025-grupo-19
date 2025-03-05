# GymBros

## 📋 Integrantes del equipo de desarrollo

| Nombre        | Apellidos           | Correo Uni  | Cuenta Github|
| ------------- |:-------------:| :---------:|:---------:|
| Víctor | Candel Casado     |   v.candel.2020@alumnos.urjc.es  | victorcc02 |

## 	:cyclone: Entidades
Las principales entidades de la aplicación son:
- **Usuario**: Clientes del gimnasio que pueden acceder a planes de entrenamientos y de dietas.
- **Nutrición**: Planes alimenticios personalizados según los objetivos del usuario.
- **Entrenamiento**: Rutinas de ejercicios personalizadas.
- **Comida**: Comidas que tienen las nutriciones (dietas)
![image](https://github.com/user-attachments/assets/ebb39070-2ee8-44f3-8278-6b10c66448fd)

## 🖼️ Imágenes
- **Usuario**: Los usuarios podrán tener foto de perfil.

## 🖼️ Capturas de pantalla de las diferentes pantallas de la aplicación 
- **Pantalla de inicio**:
  ![image](https://github.com/user-attachments/assets/37fda688-74ce-4682-9218-71e62af2e7c4)
  En esta pantalla los usuarios deben poner el usuario y la contraseña correspondiente a sus cuentas, una vez rellenados el usuario clickará en el botón "Iniciar"
  
- **Pantalla general tras el inicio de sesión**:
  ![localhost_8080_FrontPage_userId=2](https://github.com/user-attachments/assets/1961a3c9-2398-493f-bfeb-491b506e74b5)
  En esta pantalla los usuarios tras iniciar sesión podran ver el menu general, en el que podran acceder presionando los respectivos botones a las diferentes areas de la aplicacion (Menu de entrenamiento, diestas...)
  
- **Pantalla de los detalles de la cuenta del usuario**:
  ![image](https://github.com/user-attachments/assets/03a82dac-3032-4af1-8f84-13146053f846)
  En esta pantalla el usuario podra tener acceso a sus datos, de igual forma podra eliminar su cuenta o editarla clickando en los respectivos botones.
  
- **Pantalla de edición de los atributos de usuario**:
  ![localhost_8080_users_edit_2](https://github.com/user-attachments/assets/6b3f8db1-63c5-4bd0-adc9-a84bfce67d50)
  En esta pantalla el usuario podra editar sus datos, de igual forma podra guardar dichos cambios de su cuenta o cancelarlos clickando en los respectivos botones.
  
- **Pantalla del menu de los entrenamientos**:
  ![image](https://github.com/user-attachments/assets/2cfe81d9-7a74-40af-b944-30ff11a69a0f)
  En esta pantalla se encuentra en un desplegable los posibles entrenamientos a los que puede acceder el usuario, si presiona seleccionar entrara en los detalles de la rutina seleccionada (pecho en este caso), de igual forma podra crear una rutina mediante el boton, y con el botón de la esquina superior izquierda podra volver al menu general de la aplicación.
  
- **Pantalla de los detalles de un entrenamiento**:
  ![image](https://github.com/user-attachments/assets/a2ebe9bd-6074-480a-a893-7218d79eecdb)
  En esta pantalla el usuario podra ver los detalles de un entrenamiento, también podrá mediante los botones superiores editarla, borrarla o volver al menu de los entrenamientos.

- **Pantalla de creación y edición de un entrenamiento**:
  ![image](https://github.com/user-attachments/assets/7cd5b18a-bc8f-4f23-81c4-06fa69f120df)
  En esta pantalla se tiene un formulario para rellenar los datos de un nuevo entrenamiento, de igual forma podra guardar dichos datos de la rutina o cancelar la creación clickando en los respectivos botones.
  La plantilla de esta pantalla será exactamente igual para la de edición de una rutina.
   
- **Pantalla del menu de las dietas**:
  ![image](https://github.com/user-attachments/assets/a852f4b9-57fc-40fc-b7be-220e49694cf5)
  Esta pantalla aparecerá de primeras al acceder a nutriciones desde la pantalla general, en esta aparecen los botones de lista de nutriciones (que llevará a la pantalla de lista de dietas), crear una nutrición (para la creación de una dieta) y atras, que servirá para volver al menú principal. Tras darle a lista de nutriciones se redigira a la lista de dietas:
  ![image](https://github.com/user-attachments/assets/cb0717ba-24d8-4468-a9e5-59beba9816d0)
  De igual forma, en esta pantalla se podrá volver tanto a la pantalla anterior como a la pantalla general de la web.

- **Pantalla de los detalles de una dieta**:
  ![image](https://github.com/user-attachments/assets/f27fbda2-4e3f-4b4a-9b8f-563543ad9d33)
  Esta pantalla muestra lo detalles de la dieta, de igual forma un botón para volver, otro para eliminar esta dieta, y luego podriamos editar la dieta entera o por el contrario editar solo los alimentos de esta.

- **Pantalla de creación de una dieta**:
  ![image](https://github.com/user-attachments/assets/5bd489ea-6b03-4bf1-842a-964c9a8e5378)
  En esta primera pantalla de creación de una dieta se podrá volver atrás (al menu principal de dietas), he introducir los campos nombre y tipo de la nueva dieta, para seguir con su creación se clickará en agregar alimentos lo cual redrigirá la pagina a la siguiente interfaz:
  ![image](https://github.com/user-attachments/assets/3505fc96-7a2f-4a51-b931-0402c8304897)
 En esta pantalla se podrán meter a la dieta los alimentos que se deseen (así como crear nuevos), y al darle a terminar nutrición se creará y la página se redigirá al menu principal de dietas.

- **Pantalla de error 500**:
   ![image](https://github.com/user-attachments/assets/78a27607-496c-4eaf-8c0d-739402ca4f4b)
  Esta pantalla se muestra si se ha producido un error 500 (error server)
- **Pantalla de error 404**:
  ![image](https://github.com/user-attachments/assets/8117b15e-067e-4802-8a09-ff4466e60b8f)
  Esta pantalla se muestra si se ha producido un error 404 (url not found)

## Diagrama de navegación
- Nota: desde cualquier página se puede llegar a la página de error 404 o error 500
![image](https://github.com/user-attachments/assets/736d6cc7-bdc9-4202-aa2c-819747b63d02)


## Diagrama con las entidades de la base de datos
![image](https://github.com/user-attachments/assets/d629588d-1e9f-4cad-ade0-2abfac4d74f3)

## Diagrama de clases y templates

![image](https://github.com/user-attachments/assets/5fd266c5-22e6-4dbc-aab8-048e6e1a14e9)

## Instrucciones para ejecución
Descargar Intellij en jetBrains: https://www.jetbrains.com/es-es/idea/download/?section=windows
En file->settings->pluggins descargar las siguientes extensiones
![image](https://github.com/user-attachments/assets/068c12fc-23b5-46e4-97c0-ac50facf5aa5)
Presionar con click derecho el pom.xml y darle a "Add as a maven project". El proyecto ya estara listo para ejecutarse, sin embargo hay que lanzar primero la base de datos en MySqlWorkbech (descargar en https://dev.mysql.com/downloads/workbench/), es esta se lanzara en el puerto 3036 en localhost con el nombre gymbrosdb (creando un nuevo schema con dicho nombre). y tras ejecutarse el programa ya se podra observar la base de datos MySql.







