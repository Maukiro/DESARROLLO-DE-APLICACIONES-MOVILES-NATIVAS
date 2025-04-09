📍 OpenStreetMapApp
📝 Descripción del Proyecto
OpenStreetMapApp es una aplicación móvil nativa para Android que muestra la ubicación actual del usuario en un mapa de OpenStreetMap (OSM) y permite explorar zonas predefinidas.
Además, incluye una versión alternativa con Google Maps para comparar rendimiento entre diferentes proveedores de mapas.

El sistema de exploración urbana muestra puntos de interés que se desbloquean al visitarlos físicamente y una barra de progreso indica el porcentaje de zonas exploradas.

🚀 Instrucciones para Configurar y Ejecutar la Aplicación
Clonar el repositorio:

bash
Copiar
Editar
git clone https://github.com/tu-usuario/OpenStreetMapApp.git
Abrir el proyecto en Android Studio.

Sincronizar Gradle para descargar las dependencias.

Configurar permisos:

Asegúrese de otorgar permisos de ubicación cuando la aplicación lo solicite.

Ejecutar la aplicación en un dispositivo físico o emulador con servicios de ubicación habilitados.

🏛️ Arquitectura de la Aplicación
La aplicación sigue una arquitectura sencilla basada en Activities:

MainActivity:
Muestra el mapa de OpenStreetMap en un WebView, gestiona la ubicación y zonas explorables.

GoogleMapsActivity:
Carga Google Maps web en un WebView utilizando la ubicación actual.

Diagrama Simplificado:
scss
Copiar
Editar
[ MainActivity ]
   ↳ WebView (OSM + Leaflet.js)
   ↳ Botón → [ GoogleMapsActivity ]
                ↳ WebView (Google Maps)
Comunicación entre Android y WebView para mostrar mapas.

Lógica de detección de zonas explorables basada en coordenadas y radio.

🖼️ Capturas de Pantalla
OpenStreetMap con zonas explorables	Exploración Progreso	Google Maps WebView
![image](https://github.com/user-attachments/assets/6b636767-f377-4691-b042-478b085dd324)
![image](https://github.com/user-attachments/assets/2b58c2bc-c9bd-4f81-8d76-45eea1b0fbc6)


🛠️ Desafíos Encontrados y Soluciones
Desafío	Solución
Integrar mapas OSM en Android sin SDK nativo	Uso de WebView y Leaflet.js
Detectar zonas explorables en tiempo real	Cálculo de distancia entre coordenadas usando Location.distanceBetween
Diferenciar zonas descubiertas/no descubiertas	Cambio dinámico de color en los círculos de Leaflet
Comparar rendimientos entre mapas	Medir tiempos de carga y respuesta al toque
Mostrar progresos visuales	Implementación de ProgressBar en tiempo real
📦 Dependencias Utilizadas
Dependencia	Propósito
com.google.android.gms:play-services-location:21.0.1	Obtener la ubicación actual del usuario de forma precisa
Leaflet.js (v1.9.4)	Biblioteca JavaScript para renderizar mapas de OpenStreetMap en WebView
WebView	Componente de Android para mostrar contenido HTML/JS
ConstraintLayout	Layout flexible y responsivo para las vistas de la app
Kotlin Standard Library	Lógica principal de la app
