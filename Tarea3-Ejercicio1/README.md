# 📌 Proyecto: Consumo de API Pública - Open Library API

## 📖 Descripción del Proyecto
Esta aplicación móvil para Android consume la **API pública de Open Library**, permitiendo buscar libros por título y visualizar información relevante como el autor, el año de publicación y la portada del libro.

Se ha utilizado **Retrofit** para realizar peticiones HTTP y **RecyclerView** con **Glide** para mostrar los resultados en una interfaz moderna y atractiva.

---

## 🚀 Configuración y Ejecución

### **1️⃣ Requisitos Previos**
- Tener instalado **Android Studio** (última versión recomendada).
- Un emulador configurado o un dispositivo físico con modo desarrollador activado.

### **2️⃣ Clonar el Repositorio**
```sh
 https://github.com/Maukiro/DESARROLLO-DE-APLICACIONES-MOVILES-NATIVAS/new/main/Tarea3-Ejercicio1
```

### **3️⃣ Abrir el Proyecto en Android Studio**
1. Abre **Android Studio** y selecciona "Open an Existing Project".
2. Navega hasta la carpeta del proyecto clonado y ábrelo.
3. Espera a que Android Studio cargue y sincronice las dependencias.

### **4️⃣ Verificar Permisos en `AndroidManifest.xml`**
Asegúrate de que el permiso de acceso a internet esté habilitado:
```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

### **5️⃣ Ejecutar la Aplicación**
- **Emulador:** Presiona ▶ en Android Studio.
- **Dispositivo Físico:** Conéctalo por USB y habilita la depuración USB.

---

## 📌 Arquitectura de la Aplicación

### **Diagrama de Arquitectura**
```
+----------------------+      HTTP Request (GET)      +--------------------+
|   Aplicación Android | --------------------------> |   Open Library API |
|   (Retrofit + Glide) | <-------------------------- | (Datos JSON)       |
+----------------------+       JSON Response        +--------------------+
```

**Explicación:**
1. El usuario ingresa un título en la aplicación.
2. Retrofit realiza una solicitud `GET` a la API de Open Library.
3. La API responde con datos en formato JSON.
4. Los datos se procesan y se muestran en una lista (`RecyclerView`).

---

## 📸 Capturas de Pantalla

### 🔹 **Búsqueda de libros en la app**
![Pantalla de búsqueda](ruta/captura_busqueda.png)

### 🔹 **Resultados de libros con imágenes y detalles**
![Lista de libros](ruta/captura_lista.png)

---

## 🛠️ Desafíos y Soluciones

### **1️⃣ Manejo de Errores de Conectividad**
- **Problema:** Si el usuario no tiene conexión, la app se quedaba en carga infinita.
- **Solución:** Se implementó un manejo de errores en Retrofit con `onFailure()` mostrando un mensaje `Toast` al usuario.

### **2️⃣ Mostrar Portadas de Libros**
- **Problema:** Open Library no proporciona URLs directas de imágenes.
- **Solución:** Se usó **Glide** para construir las URLs dinámicamente y cargar las imágenes correctamente.

### **3️⃣ Diseño Mejorado**
- **Problema:** La interfaz inicial era muy básica.
- **Solución:** Se usó **RecyclerView con CardView** para una mejor presentación.

---

## 📦 Dependencias Utilizadas

| Dependencia  | Propósito |
|-------------|----------|
| `Retrofit`  | Manejo de peticiones HTTP para consumir la API |
| `Gson`      | Conversión automática de JSON a objetos Kotlin |
| `Glide`     | Carga y visualización de imágenes de portadas |
| `RecyclerView` | Mostrar la lista de libros de manera eficiente |
| `CardView`  | Mejora la presentación de los libros |

---

## 📌 Conclusión
Esta aplicación demuestra cómo integrar una **API pública en Android** utilizando Retrofit y Glide. Se logró un diseño atractivo y funcional que permite a los usuarios buscar libros de manera eficiente. 🚀📚



