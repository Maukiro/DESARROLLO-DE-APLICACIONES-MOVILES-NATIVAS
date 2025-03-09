# 📌 Proyecto: Implementación de un Servicio REST con Node.js y Consumo en Android

## 📖 Descripción del Proyecto
Este proyecto consiste en la implementación de un servicio **RESTful** con **Node.js y Express**, el cual proporciona un **mensaje en formato JSON**. Posteriormente, la aplicación Android consume este servicio utilizando **Retrofit** para realizar la petición HTTP y mostrar la información en la interfaz de usuario.

---

## 🚀 Configuración y Ejecución

### **1️⃣ Configuración y Ejecución del Backend (Node.js)**

#### 📌 **Requisitos Previos:**
- Tener instalado **Node.js** (versión 14 o superior).
- Tener instalado **npm** (gestor de paquetes de Node.js).

#### 📌 **Pasos para Ejecutar el Backend:**
1. Clonar el repositorio:
   ```sh
   git clone https://github.com/Maukiro/DESARROLLO-DE-APLICACIONES-MOVILES-NATIVAS/edit/main/Tarea3-Ejercicio1/README.md
   ```
2. Ir al directorio del backend:
   ```sh
   cd nombre_del_repositorio/Backend
   ```
3. Instalar las dependencias necesarias:
   ```sh
   npm install
   ```
4. Ejecutar el servidor:
   ```sh
   node server.js
   ```
5. Verificar que el servidor esté corriendo en:
   ```
   http://localhost:8080/api/mensaje
   ```

---

### **2️⃣ Configuración y Ejecución de la Aplicación Android**

#### 📌 **Requisitos Previos:**
- Tener instalado **Android Studio** (versión más reciente recomendada).
- Dispositivo físico con modo desarrollador habilitado o un emulador configurado.

#### 📌 **Pasos para Ejecutar la Aplicación Android:**
1. Abrir **Android Studio** y seleccionar "Open an existing project".
2. Navegar hasta la carpeta `AndroidApp` dentro del repositorio clonado.
3. Verificar que el siguiente permiso esté en `AndroidManifest.xml`:
   ```xml
   <uses-permission android:name="android.permission.INTERNET"/>
   ```
4. Si se ejecuta en un **emulador**, asegurarse de que `RetrofitClient.kt` usa:
   ```kotlin
   private const val BASE_URL = "http://10.0.2.2:8080/"
   ```
   Si se usa un **dispositivo real**, cambiar `10.0.2.2` por la dirección IP local de la computadora.
5. Ejecutar la aplicación en el emulador o dispositivo físico.

---

## 📌 Arquitectura de la Aplicación

### **Diagrama de Arquitectura:**
```
+---------------------+       HTTP Request (GET)        +---------------------+
|  Aplicación Android | ------------------------------> |    Servidor Node.js  |
|     (Retrofit)      | <------------------------------ | (Express.js API)     |
+---------------------+         JSON Response          +---------------------+
```

**Explicación:**
1. La aplicación Android realiza una petición **GET** al backend usando **Retrofit**.
2. El backend en **Node.js** procesa la solicitud y responde con un **JSON**.
3. La aplicación recibe la respuesta y muestra el mensaje en pantalla.

---

## 🛠️ Desafíos y Soluciones

### **1️⃣ Error de Conexión (`CLEARTEXT communication not permitted`)**
- **Causa:** A partir de Android 9, las aplicaciones bloquean el tráfico HTTP.
- **Solución:** Se agregó la siguiente configuración en `AndroidManifest.xml`:
  ```xml
  <application android:usesCleartextTraffic="true">
  ```

### **2️⃣ No se podía conectar desde el Emulador a `localhost`**
- **Causa:** `localhost` en un emulador no apunta a la máquina host.
- **Solución:** Se usó `10.0.2.2` en lugar de `localhost` en `RetrofitClient.kt`.

### **3️⃣ Error `Failed to Connect` en dispositivo real**
- **Causa:** El Firewall bloqueaba la conexión al puerto 8080.
- **Solución:** Se agregó una excepción al Firewall para permitir tráfico en el puerto 8080:
  ```sh
  netsh advfirewall firewall add rule name="Abrir Puerto 8080" dir=in action=allow protocol=TCP localport=8080
  ```

---

## 📦 Dependencias Utilizadas

### **Backend (Node.js)**
| Dependencia  | Propósito |
|-------------|----------|
| `express`   | Framework para crear el servidor HTTP |
| `cors`      | Permite la comunicación entre el backend y el frontend |

### **Android App**
| Dependencia  | Propósito |
|-------------|----------|
| `Retrofit`  | Realiza peticiones HTTP de manera sencilla |
| `Gson`      | Convierte JSON a objetos Kotlin automáticamente |

---

## 📌 Conclusión
Este proyecto demostró cómo se puede implementar una comunicación entre una aplicación móvil y un backend en **Node.js**, utilizando **Retrofit** para consumir el servicio. Se resolvieron diversos problemas relacionados con la conectividad y compatibilidad, logrando así una aplicación funcional que muestra datos dinámicamente desde un servidor externo.

---

✅ **Autor:** [Tu Nombre]  
📅 **Fecha:** [Fecha de Entrega]  
🚀 **Repositorio:** [URL del Repositorio]

