package com.example.openstreetmapapp

import ZonaExplorable
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var progresoExploracion: ProgressBar

// =============== Ingresar zonas explorables al mapa ============================
    private val zonasExplorables = mutableListOf(
        ZonaExplorable("Parque Central", 19.433, -99.133, 100f),
        ZonaExplorable("Museo de Historia", 19.431, -99.130, 150f),
        ZonaExplorable("Zócalo", 19.4326, -99.1332, 120f),
        ZonaExplorable("ESCOM", 19.50456, -99.14647, 100f),
        ZonaExplorable("Casa", 19.5409191228741, -99.22037085598498, 100f),
        ZonaExplorable("Tacos", 19.540595297920678, -99.21979531666871, 100f)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.mapWebView)
        progresoExploracion = findViewById(R.id.progresoExploracion)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            obtenerUbicacion()
        }

        val btnGoogleMaps = findViewById<Button>(R.id.btnGoogleMaps)
        btnGoogleMaps.setOnClickListener {
            startActivity(Intent(this, GoogleMapsActivity::class.java))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            obtenerUbicacion()
        } else {
            Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerUbicacion() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                mostrarMapa(latitude, longitude)
                verificarZonas(latitude, longitude)
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarMapa(lat: Double, lon: Double) {
        val marcadoresJS = StringBuilder()
        for (zona in zonasExplorables) {
            marcadoresJS.append(
                """
        L.circle([${zona.latitud}, ${zona.longitud}], {
            color: '${if (zona.descubierta) "green" else "red"}',
            fillColor: '${if (zona.descubierta) "lightgreen" else "pink"}',
            fillOpacity: 0.5,
            radius: ${zona.radio}
        }).addTo(map).bindPopup("${zona.nombre}");
        
        """
            )
        }
        val html = """
    <!DOCTYPE html>
    <html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
            html, body, #map { height: 100%; margin: 0; padding: 0; }
        </style>
        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
        <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
    </head>
    <body>
        <div id="map"></div>
        <script>
            var map = L.map('map').setView([$lat, $lon], 16);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                maxZoom: 19,
                attribution: '© OpenStreetMap contributors'
            }).addTo(map);
            
            L.marker([$lat, $lon]).addTo(map).bindPopup("Mi ubicación").openPopup();
            
            ${marcadoresJS.toString()}
        </script>
    </body>
    </html>
""".trimIndent()


        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.webViewClient = WebViewClient()
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
    }

    private fun verificarZonas(lat: Double, lon: Double) {
        var zonasDescubiertas = 0

        for (zona in zonasExplorables) {
            val distancia = calcularDistancia(lat, lon, zona.latitud, zona.longitud)
            if (!zona.descubierta && distancia <= zona.radio) {
                zona.descubierta = true
                Toast.makeText(this, "¡Descubriste: ${zona.nombre}!", Toast.LENGTH_SHORT).show()
            }
            if (zona.descubierta) zonasDescubiertas++
        }

        val progreso = (zonasDescubiertas.toFloat() / zonasExplorables.size.toFloat()) * 100
        actualizarProgreso(progreso)
    }

    private fun actualizarProgreso(porcentaje: Float) {
        progresoExploracion.progress = porcentaje.toInt()
    }

    private fun calcularDistancia(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }
}
