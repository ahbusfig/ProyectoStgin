<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="juego.Conecta4" %>

<html>
<head>
    <title>Conecta4 - Juego</title>
    <style>
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }

        #contenedor-juego {
            text-align: center;
            margin: 20px; /* Ajusta el margen del contenedor según tus necesidades */
        }

        #tablero {
            display: grid;
            grid-template-columns: repeat(7, 70px);
            grid-gap: 10px;
        }

        .celda {
            width: 70px;
            height: 70px;
            border: 1px solid #000;
            background-color: #53616d;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
            color: #fff;
            cursor: pointer;
        }
    </style>
</head>
<body>

<%-- Obtener la conexión a la base de datos --%>
<% try {
    Class.forName("org.mariadb.jdbc.Driver");
    Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/conecta4", "root", "");

    // Obtener el id de la partida de la sesión
    int idPartida = (int) request.getSession().getAttribute("idPartida");
    // Obtener el id del jugador de la sesión
    int idJugador = (int) request.getSession().getAttribute("idJugador");
    // Obtener el id del tablero de la sesión
    Integer idTablero = (Integer) request.getSession().getAttribute("idTablero");


    // Instanciar el juego y crear el tablero
    Conecta4 juego = new Conecta4(con);
    juego.crearTablero(idPartida);

} catch (ClassNotFoundException | SQLException e) {
    e.printStackTrace();
}
%>

<%-- Aquí va el código JavaScript para crear el tablero y manejar las fichas --%>

<div id="contenedor-juego">
    <h1>Conecta4 - Juego</h1>
    <div id="tablero">
        <% for (int i = 0; i < 6; i++) { %>
        <% for (int j = 0; j < 7; j++) {
            Integer idTablero = (Integer) request.getSession().getAttribute("idTablero");
            int idJugador = (int) request.getSession().getAttribute("idJugador");
        %>
        <div class="celda" id="celda_<%=i%>_<%=j%>" onclick="insertarFicha(<%=idTablero%>, <%=i%>, <%=j%>, <%=idJugador%>)"></div>
        <% } %>
        <% } %>
    </div>
</div>
<%-- Aquí va el código JavaScript para crear el tablero y manejar las fichas --%>
<script>
    function insertarFicha(idTablero, fila, columna, idJugador) {
        $.ajax({
            type: "POST",
            url: "InsertarFichaServlet",
            data: {
                idTablero: idTablero,
                fila: fila,
                columna: columna,
                idJugador: idJugador
            },
            success: function(response) {
                if (response.success) {
                    // Cambia el color de la casilla según el jugador
                    var color = response.jugador == 1 ? "red" : "yellow";
                    var cell = document.getElementById("celda_" + fila + "_" + columna);
                    cell.style.backgroundColor = color;
                } else {
                    // Registra un error si la inserción de la ficha falló
                    console.error("Error al insertar la ficha.");
                }
            },
            error: function(error) {
                // Registra el error si la petición AJAX falló
                console.error(error);
            }
        });
    }
</script>
</body>
</html>
