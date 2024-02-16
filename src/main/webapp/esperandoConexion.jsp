<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Esperando Jugador - Conecta4</title>

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
    <script>
        // Función para sondear el estado cada 3 segundos
        function verificarSegundoJugador() {
            fetch('esperarConexionServlet') // Url del servlet
                .then(response => response.json())
                .then(data => {
                    if (data.status === 'found') {
                        // Si se encuentra el segundo jugador, redirige al servlet TurnoEstado
                        window.location.href = 'TurnoEstado';
                    } else if (data.status === 'error') {
                        // Manejar el error como prefieras
                        alert('Ocurrió un error al verificar el estado del juego.');
                    }
                    // Si el estado es 'waiting', no hace nada y esperará a la próxima verificación
                })
                .catch(error => {
                    console.error('Error al realizar la solicitud', error);
                });
        }

        // Iniciar el sondeo cuando la página cargue
        window.onload = function() {
            setInterval(verificarSegundoJugador, 3000); // Verifica el estado cada 3 segundos
        };
    </script>
</head>
<body>
<h1>Esperando al segundo jugador...</h1>
<%
    String codigoPartida = (String) session.getAttribute("codigoPartida");
    if (codigoPartida != null) {
        out.println("<p>Código de Partida: <span id='codigoPartida'>" + codigoPartida + "</span></p>");
%>
<button onclick="copiarCodigo()">Copiar Código</button>
<script> // Función para copiar el código de partida al portapapeles
    function copiarCodigo() {
        var codigo = document.getElementById('codigoPartida').innerText;
        navigator.clipboard.writeText(codigo).then(function() {
            alert('Código copiado al portapapeles: ' + codigo);
        }, function(err) {
            alert('No se pudo copiar el código: ', err);
        });
    }
</script>
<%
    } else {
        out.println("<p>Error: No se encontró el código de partida.</p>");
    }
%>
<a href="index.html" class="button">Volver a Inicio</a>
</body>
</html>
