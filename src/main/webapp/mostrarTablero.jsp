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

    //Consulta para obtener el id de la partida
    String sql = "SELECT idPartida FROM partidas WHERE codigoPartida = ?";
    PreparedStatement ps = con.prepareStatement(sql);
    ps.setString(1, (String) request.getSession().getAttribute("codigoPartida"));
    ResultSet rs = ps.executeQuery();
    rs.next();
    int idPartida = rs.getInt("idPartida");


    // Obtener el id del jugador de la sesión
    int idJugador = (int) request.getSession().getAttribute("idJugador");

    // Instanciar el juego y crear el tablero
    Conecta4 juego = new Conecta4(con);
    juego.crearTablero(idPartida);
    // Guardar el id del tablero en la sesión
    int idTablero = juego.getIdTablero();

    // El turno lo miramos por la database
    int Turno = (int) request.getSession().getAttribute("Turno");

%>
<div id="contenedor-juego">
    <h1>Conecta4 - Juego</h1>

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
<%
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
    }
%>
</body>
</html>