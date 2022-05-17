### Sobre el ejercicio:

Task 1 → Que se conecte al puerto: 3030 -> Ok

Task 2 → Las variables de entorno no se deben subir a github, 
            pero tiene que haber un template que permita conocer cuales son las necesarias.

Task 3 → Las rutas de auth tiene que permitir registrar y logear usuarios: 
            Una ruta para el registro /signup, Una ruta para el login /login

Task 4 → Tanto la ruta de login como la de registro deben enviar el token para poder entrar en las demás rutas.

Task 5 → Además, se necesita una ruta que devuela toda la lista de users que haya 
        para que desde el backoffice de JOBX puedan controlar el crecimiento. 
        Esta ruta tiene que estar protegida y solo se dará acceso a un usuario con rol de admin y con los credenciales 
        que se encuentran encriptados en el documento: ruta: /users

Task 6 → Un user tiene como parámetros obligatorios: username (tiene que ser único), email (tiene que seguir el 
            patrón de correo y ser único), password (tiene que tener mínimo de 8 carácteres, incluir una mayúscula y un número).
