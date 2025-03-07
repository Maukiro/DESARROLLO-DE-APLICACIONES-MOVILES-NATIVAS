// Importar Express
const express = require('express');
const app = express();
const PORT = 8080;

// Endpoint que retorna un mensaje en JSON
app.get('/api/mensaje', (req, res) => {
    res.json({ mensaje: 'Hola Mundo desde Node.js' });
});

// Iniciar el servidor
app.listen(PORT, () => {
    console.log(`Servidor corriendo en http://localhost:${PORT}`);
});

/*
    Documentación:
    -----------------
    - Método: GET
    - URL: http://localhost:8080/api/mensaje
    - Respuesta JSON esperada:
      {
          "mensaje": "Hola Mundo desde Node.js con Express"
      }
*/
