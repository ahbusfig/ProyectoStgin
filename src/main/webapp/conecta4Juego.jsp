<% String idPartida = null; %>
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


<%-- Obtener el id de la partida --%>
<% try {
    Class.forName("org.mariadb.jdbc.Driver");
    Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/conecta4", "root", "");

    //Consulta para obtener el id de la partida

    idPartida = request.getQueryString().split("=")[1];


    // Obtener el id del jugador de la sesión
    int idJugador = (int) request.getSession().getAttribute("idJugador");


    String sql = "SELECT Jugador1, Jugador2 FROM partidas WHERE IdPartida = ?";
    PreparedStatement ps = con.prepareStatement(sql);
    ps.setString(1, idPartida);
    ResultSet rs = ps.executeQuery();
    rs.next();
    int jugador1 = rs.getInt("Jugador1");
    int jugador2 = rs.getInt("Jugador2");

    // Instanciar el juego y crear el tablero
    Conecta4 juego = Conecta4.getInstance(con, Integer.parseInt(idPartida), jugador1, jugador2);

    // si está lleno el tablero
    if (juego.estaLleno()) {
        response.sendRedirect("FinPartidaServlet?idPartida=" + idPartida);
    }


    juego.crearTablero(Integer.parseInt(idPartida));
    // Guardar el id del tablero en la sesión
    int idTablero = juego.getIdTablero();
    sql = "SELECT JUGADOR_ID FROM TURNO WHERE PARTIDA_ID = ? ORDER BY TIMESTAMP DESC LIMIT 1";
    ps = con.prepareStatement(sql);
    ps.setString(1, idPartida);
    rs = ps.executeQuery();
    rs.next();

    // El turno lo miramos por la database
    int Turno = rs.getInt("JUGADOR_ID");

%>


<div id="contenedor-juego">
        <% if (idJugador == Turno) { %>
    <h1>Conecta4 - Juego</h1>
        <%
                    for (int j = 0; j < 6; j++) {
                        %>
    <form action="InsertarFichaServlet" method="post">
        <input type="hidden" name="idJugador" value="<%=idJugador%>">
        <input type="hidden" name="columna" value="<%=j%>">
        <input type="submit" value="Columna <%=j + 1%>">
        <input type="hidden" value="<%=idTablero%>" name="idTablero">
        <input type="hidden" value="<%=idPartida%>" name="idPartida">
    </form>
        <%
                    }
                    %>
    <div id="tablero" data-idTablero="<%=idTablero%>" data-jugador="<%=idJugador%>">
        <% for (int i = 0; i < 6; i++) { %>
        <% for (int j = 0; j < 6; j++) { %>
        <% int c = juego.getCelda(i, j);
            String str = c == 0 ? "#53616d" : c == jugador1 ? "#CD5C5C" : "#6495ED";%>
        <div style="background-color: <%=str%>" class="celda" id="celda<%=i%><%=j%>"
             data-fila="<%=i%>"
             data-columna="<%=j%>"
             data-idTablero="<%=idTablero%>"
             data-jugador="<%=idJugador%>"></div>
        <% } %>
        <% } %>
        <% } else { %> <!--- polling -->
        <div id="tablero" data-idTablero="<%=idTablero%>" data-jugador="<%=idJugador%>">
            <% for (int i = 0; i < 6; i++) { %>
            <% for (int j = 0; j < 6; j++) { %>
            <% int c = juego.getCelda(i, j);
                String str = c == 0 ? "#53616d" : c == jugador1 ? "#CD5C5C" : "#6495ED";%>
            <div style="background-color: <%=str%>" class="celda" id="celda<%=i%><%=j%>"
                 data-fila="<%=i%>"
                 data-columna="<%=j%>"
                 data-idTablero="<%=idTablero%>"
                 data-jugador="<%=idJugador%>"></div>
            <% } %>
            <% } %>
        </div>
        <script>
            var idPartida = '<%=idPartida%>';
            var idJugador = '<%=idJugador%>';

            function verificarCambioDeTurno() {
                fetch('ObtenerUltimoTurnoServlet?idPartida=' + idPartida)
                    .then(response => response.json())
                    .then(data => {
                        var n = "turno";
                        var ultimoTurno = data[n];
                        if (ultimoTurno == idJugador) {
                            location.reload();
                        }
                    });
            }


            // Realiza la verificación cada 5 segundos
            setInterval(verificarCambioDeTurno, 1000);
        </script>
        <% } %>

        <p id="turnoJugador"><%=Turno == idJugador ? "Es tu turno" : "No es tu turno"%>
        </p>
    </div>
        <%
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
                %>
</body>
</html>