# 📱 Aprende Binario con Diversión

**Una app educativa para niños de primaria** que enseña conceptos básicos de representación binaria y código ASCII a través de juegos interactivos y coloridos.

---

## 🎯 Objetivo

La aplicación busca facilitar el aprendizaje de temas abstractos como el sistema binario y la codificación ASCII mediante una interfaz sencilla, visual y amigable para niños pequeños.

---

## 🧩 Funcionalidades principales

- ✅ **Conversión binaria interactiva:** Usando interruptores, los niños aprenden a representar números decimales en binario.
- ✅ **Juego de código ASCII:** Se presenta un valor ASCII y el usuario debe adivinar la letra correspondiente.
- ✅ **Temas visuales personalizables:**
  - Tema **Guinda**
  - Tema **Azul**
  - Ambos se adaptan automáticamente al modo claro/oscuro del sistema.
- ✅ **Retroalimentación auditiva**: Sonidos de felicitación al responder correctamente.
- ✅ **Navegación intuitiva** con menú inferior.

---

## 🧠 Arquitectura de la aplicación

- **Activities:**
  - `MainActivity`: Navegación principal.
  - `BinarioActivity`: Conversión binaria.
  - `AsciiGameActivity`: Juego de letras y ASCII.

- **Fragments:**
  - `InicioFragment`: Pantalla inicial con accesos.
  - `TemasFragment`: Cambio de tema visual.

- **Módulo adicional:** `ThemeManager.kt` para aplicar temas personalizados dinámicamente.

---

## 💻 Tecnologías utilizadas

- Android Studio + Kotlin
- Material Components
- `SharedPreferences` para persistencia de tema
- `MediaPlayer` para sonidos
- Compatible con Android 6.0+

---

## 📷 Capturas de pantalla 
![Imagen de WhatsApp 2025-03-24 a las 17 02 01_1fe41aad](https://github.com/user-attachments/assets/4122f71f-4859-4396-9d50-07a33d108f8e)
![Imagen de WhatsApp 2025-03-24 a las 17 02 01_4c2b03d0](https://github.com/user-attachments/assets/92bc63d7-241e-4abc-ad41-8913304f3525)
![Imagen de WhatsApp 2025-03-24 a las 17 02 01_ed080c5d](https://github.com/user-attachments/assets/759a8e03-13ca-4838-84d7-6098cf4a4152)
![Imagen de WhatsApp 2025-03-24 a las 17 02 01_101f101c](https://github.com/user-attachments/assets/f485eed6-80fe-4552-a2dd-812415833224)



