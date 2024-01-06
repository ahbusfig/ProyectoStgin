<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Esperando Jugador - Conecta4</title>
    <!-- Recarga la página cada 10 segundos para verificar si el segundo jugador se ha unido -->
    <meta http-equiv="refresh" content="10;url=esperarConexionServlet">
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
            color: #1a1919;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }
        .title {
            margin-bottom: 20px;
            color: #61dafb;
        }
        .button {
            background-color: #61dafb;
            color: #282c34;
            padding: 15px 30px;
            margin: 10px;
            border: none;
            border-radius: 25px;
            cursor: pointer;
            font-size: 18px;
            transition: all 0.3s;
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
            text-transform: uppercase;
            font-weight: bold;
        }
        .button:hover {
            background-color: #4fa1c0;
            box-shadow: 0px 6px 12px rgba(0, 0, 0, 0.3);
        }
        .button:focus {
            outline: none;
        }
    </style>
</head>
<body>
<h1>Esperando al segundo jugador...</h1>
<%
    // Recuperar el código de partida de la sesión
    String codigoPartida = (String) session.getAttribute("codigoPartida");
    if (codigoPartida != null) {
        out.println("<p>Código de Partida: <span id='codigoPartida'>" + codigoPartida + "</span></p>");
%>
<button onclick="copiarCodigo()">Copiar Código</button>
<script> // Función para copiar el código de partida al portapapeles
    function copiarCodigo() {
        var codigo = document.getElementById('codigoPartida').innerText; // Obtener el código de partida
        navigator.clipboard.writeText(codigo).then(function() { // Copiar el código al portapapeles
            alert('Código copiado al portapapeles: ' + codigo); // Mostrar mensaje de éxito
        }, function(err) {
            alert('No se pudo copiar el código: ', err); // Mostrar mensaje de error
        });
    }
</script>
<%
    } else {
        out.println("<p>Error: No se encontró el código de partida.</p>");
    }
%>
<!-- Boton para volver a la página de inicio -->
<a href="index.html" class="button">Volver a Inicio</a>
</body>
</html>
