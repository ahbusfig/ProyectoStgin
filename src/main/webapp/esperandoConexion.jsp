<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.*" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
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

</head>
<body>
<h1>Esperando al segundo jugador...</h1>
<%
    String codigoPartida = (String) session.getAttribute("codigoPartida");
    PrintWriter o = response.getWriter();
    int idPartida = 0;
    try {
        Class.forName("org.mariadb.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/conecta4", "root", "");
        String query = "SELECT idPartida FROM partidas WHERE CodigoPartida = " + codigoPartida;

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        idPartida = rs.getInt("idPartida");
        con.close();
        stmt.close();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }


    if (codigoPartida != null) {
        o.println("<p>Código de Partida: <span id='codigoPartida'>" + codigoPartida + "</span></p>");
    } else {
        o.println("<p>Error: No se encontró el código de partida.</p>");
    }
%>

<script>
    // Función para sondear el estado cada 3 segundos
    function verificarSegundoJugador() {
        var idPartida = <%=idPartida %>;
        fetch('esperarConexionServlet') // Url del servlet
            .then(response => response.json())
            .then(data => {
                if (data.status === 'found') {
                    // Si se encuentra el segundo jugador, redirige al servlet TurnoEstado
                    window.location.href = 'conecta4Juego.jsp?idPartida=' + idPartida;
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
<a href="index.html" class="button">Volver a Inicio</a>
</body>
</html>
