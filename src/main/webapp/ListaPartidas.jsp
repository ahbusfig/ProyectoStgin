<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Listado de Partidas</title>
</head>
<body>

<h2>Listado de Partidas</h2>

<%
    // Variables de conexión a la base de datos
    String url = "jdbc:mariadb://localhost:3306/conecta4";
    String usuario = "root";
    String password = "";

    // Variable para almacenar el nombre del jugador que ha iniciado sesión
    String jugador = (String) session.getAttribute("usuario");


    // Si no hay un jugador en sesión, redirigir al inicio de sesión
    if (jugador == null) {
        response.sendRedirect("index.html");
    }

    // Variables para almacenar el resultado de la consulta
    ArrayList<String[]> partidas1 = new ArrayList<>();
    ArrayList<String[]> partidas2 = new ArrayList<>();
    ArrayList<String[]> partidas3 = new ArrayList<>();

    int idJugador;
    try {
        // Conexión a la base de datos
        Class.forName("org.mariadb.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, usuario, password);
        String sql = "SELECT IdJugador FROM jugadores WHERE Nombre = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, jugador);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        idJugador = rs.getInt("IdJugador");
        sql = "SELECT IdPartida FROM partidas WHERE Jugador1 = ? OR Jugador2 = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, String.valueOf(idJugador));
        pstmt.setString(2, String.valueOf(idJugador));
        rs = pstmt.executeQuery();
        while (rs.next()) {
            int idPartida = rs.getInt("IdPartida");
            String sql2 = "SELECT JUGADOR_ID FROM TURNO WHERE PARTIDA_ID = ? ORDER BY TIMESTAMP DESC LIMIT 1";
            PreparedStatement pstmt2 = con.prepareStatement(sql2);
            pstmt2.setInt(1, idPartida);
            ResultSet rs2 = pstmt2.executeQuery();
            if(!rs2.next()) {
                partidas3.add(new String[]{Integer.toString(idPartida)});
                continue;
            }
            String jugador1 = rs2.getString("JUGADOR_ID");
            if (Integer.parseInt(jugador1) == idJugador) {
                String[] partida = new String[1];
                partida[0] = Integer.toString(idPartida);
                partidas1.add(partida);
            } else {
                String[] partida = new String[1];
                partida[0] = Integer.toString(idPartida);
                partidas2.add(partida);
            }
        }
        // Almacenar el resultado de la consulta en el ArrayList

        pstmt.close();
        con.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
<p>Bienvenido, <%= jugador %>!</p>

<<p>Te toca:</p>
<table border="1">
    <tr>
        <th>IdPartida</th>
        <th>Link</th>
    </tr>
    <% for (String[] partida : partidas1) { %>
    <tr>
        <td><%= partida[0] %></td>
        <td><a href="conecta4Juego.jsp?IdPartida=<%= partida[0] %>">Insertar Ficha</a></td>
    </tr>
    <% } %>
</table>
<p>No te toca:</p>
<table border="1">
    <tr>
        <th>IdPartida</th>
        <th>Link</th>
    </tr>
    <% for (String[] partida : partidas2) { %>
    <tr>
        <td><%= partida[0] %></td>
        <td><a href="conecta4Juego.jsp?IdPartida=<%= partida[0] %>">Ver tablero</a></td>
    </tr>
    <% } %>
</table>
<p>Partidas sin empezar:</p>
<table border="1">
    <tr>
        <th>IdPartida</th>
    </tr>
    <% for (String[] partida : partidas3) { %>
    <tr>
        <td><%= partida[0] %></td>
    </tr>
    <% } %>
</table>
</body>
</html>
