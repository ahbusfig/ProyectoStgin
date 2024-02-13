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
        String password = " ";

        // Variable para almacenar el nombre del jugador que ha iniciado sesión
        String jugador = (String) session.getAttribute("usuario");

        // Si no hay un jugador en sesión, redirigir al inicio de sesión
        if (jugador == null) {
            response.sendRedirect("index.html");
        }

        // Variables para almacenar el resultado de la consulta
        ArrayList<String[]> partidas = new ArrayList<>();

    try {
        // Conexión a la base de datos
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, usuario, password);

        // Determinar el turno del jugador -> a partir del valor de sesion de turno
        int turno = (int) session.getAttribute("Turno");
        // Consulta SQL para obtener las partidas en las que es el turno del jugador
        String consulta = "SELECT IdPartida, Jugador1, Jugador2, Turno FROM partidas WHERE (Jugador1 = ? OR Jugador2 = ?) AND Turno = ?";
        PreparedStatement pstmt = con.prepareStatement(consulta);
        pstmt.setString(1, jugador);
        pstmt.setString(2, jugador);
        pstmt.setInt(3, turno);

        ResultSet rs = pstmt.executeQuery();
        // Almacenar el resultado de la consulta en el ArrayList
        while (rs.next()) {
            String[] partida = new String[4];
            partida[0] = rs.getString("IdPartida");
            partida[1] = rs.getString("Jugador1");
            partida[2] = rs.getString("Jugador2");
            partida[3] = rs.getString("Turno");
            partidas.add(partida);
        }
        pstmt.close();
        con.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
<p>Bienvenido, <%= jugador %>!</p>

<table border="1">
    <tr>
        <th>IdPartida</th>
        <th>Jugador1</th>
        <th>Jugador2</th>
        <th>Turno</th>
        <th>Acción</th>
    </tr>
    <% for (String[] partida : partidas) { %>
    <tr>
        <td><%= partida[0] %></td>
        <td><%= partida[1] %></td>
        <td><%= partida[2] %></td>
        <td><%= partida[3] %></td>
        <td><a href="InsertarFichaServlet?IdPartida=<%= partida[0] %>">Insertar Ficha</a></td>
    </tr>
    <% } %>
</table>

</body>
</html>
