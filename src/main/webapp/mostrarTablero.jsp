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

        #contenedor-juego form {
            display: inline-block; /* Hace que los formularios se muestren en línea */
        }

        #tablero {
            display: grid;
            grid-template-columns: repeat(6, 70px);
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

    // Instanciar el juego y crear el tablero
    Conecta4 juego = new Conecta4(con);
    juego.crearTablero(idPartida);
    // Guardar el id del tablero en la sesión
    request.getSession().setAttribute("idTablero", juego.getIdTablero());

} catch (ClassNotFoundException | SQLException e) {
    e.printStackTrace();
}
%>
<%
    int idTablero = (int) request.getSession().getAttribute("idTablero");
    int idJugador = (int) request.getSession().getAttribute("idJugador");
    int idJugador1 = (int) request.getSession().getAttribute("idJugador1");
    int idJugador2 = (int) request.getSession().getAttribute("idJugador2");
    int Turno = (int) request.getSession().getAttribute("Turno");

%>
<div id="contenedor-juego">
    <h1>Conecta4 - Juego</h1>
    <%
        if (idJugador == 0  ) { // Si es el turno del jugador 1
            for (int j = 0; j < 6; j++) {
    %>
    <form action="InsertarFichaServlet" method="post">
        <input type="hidden" name="idJugador" value="<%= idJugador %>">
        <input type="hidden" name="columna" value="<%= j %>">
        <input type="submit" value="Columna <%=j+1%>">
    </form>
    <%
        }
    }
    else if (Turno == 1) { // Si es el turno del jugador 2
        for (int j = 0; j < 6; j++) {
    %>

    <form action="InsertarFichaServlet" method="post">
        <input type="hidden" name="idJugador" value="<%= idJugador %>">
        <input type="hidden" name="columna" value="<%= j %>">
        <input type="submit" value="Columna <%=j+1%>">
    </form>

    <%
            }
        }
        else {
            out.println("No es tu turno");
        }
    %>

    <div id="tablero" data-idTablero="<%=idTablero%>" data-jugador="<%=idJugador%>">
        <% for (int i = 0; i < 6; i++) { %>
        <% for (int j = 0; j < 6; j++) { %>
        <div class="celda" id="celda_<%=i%>_<%=j%>"
             data-fila="<%=i%>"
             data-columna="<%=j%>"
             data-idTablero="<%=idTablero%>"
             data-jugador="<%=idJugador%>"></div>
        <% } %>
        <% } %>
    </div>

    <p id="turnoJugador">Turno del Jugador: <%= (int) request.getSession().getAttribute("Turno") == 0 ? '1' : '2' %></p>
</div>
<script>
    // Obtén los parámetros de la URL
    const urlParams = new URLSearchParams(window.location.search);
    const fila = urlParams.get('fila');
    const columna = urlParams.get('columna');
    const idJugador = urlParams.get('idJugador');

    // Selecciona la celda correspondiente en el tablero
    const celda = document.querySelector(`#celda_${fila}_${columna}`);

    // Cambia el color de la celda en función del jugador
    if (idJugador === '1') {
        celda.style.backgroundColor = 'red';
    } else if (idJugador === '2') {
        celda.style.backgroundColor = 'blue';
    }
</script>

</body>
</html>