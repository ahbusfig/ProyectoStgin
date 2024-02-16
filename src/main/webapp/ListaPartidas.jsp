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
    // String para almacenar el nombre del jugador que ha iniciado sesión
    String nombre = (String) session.getAttribute("usuario");

    // Variable para almacenar el id del jugador que ha iniciado sesión
    int jugador = (int) session.getAttribute("idJugador");


    // Variables para almacenar el resultado de la consulta
    ArrayList<String[]> partidas = new ArrayList<>();
    ArrayList<String[]> partidas2 = new ArrayList<>();
    try {
        // Conexión a la base de datos
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, usuario, password);
        Statement st = con.createStatement();
        ResultSet rs;

        // Consulta SQL para obtener las partidas en las que es el turno del jugador
        String consultaTeToca = "SELECT partidas.IdPartida, jugadores.idJugador, partidas.Turno FROM partidas  inner join jugadores on partidas.jugador1 = jugadores.idJugador WHERE partidas.Turno = 0 ORDER BY IdPartida asc";
        rs = st.executeQuery(consultaTeToca);




        String consultaNoTeToca = "SELECT partidas.IdPartida, jugadores.idJugador, partidas.Turno FROM partidas  inner join jugadores on partidas.jugador1 = jugadores.idJugador where jugadores.idJugador = ? and partidas.Turno = 1";
        PreparedStatement pstmt2 = con.prepareStatement(consultaNoTeToca);
        pstmt2.setInt(1, jugador);
        ResultSet rs2 = pstmt2.executeQuery(); // Modificado aquí


    // Verificar si la consulta devuelve algo
        if (!rs.next()) {
            out.println("La consulta 'consultaTeToca' no devolvió ningún resultado.");
            // Enviar al inicio

        } else {
            // La consulta devolvió al menos un resultado, puedes procesarlo aquí.
            do {
                String[] partida = new String[1];
                partida[0] = rs.getString("IdPartida");
                partidas.add(partida);
                out.println("Resultado de consultaTeToca: " + rs.getString("IdPartida")); // Agregado aquí
            } while (rs.next());
        }
        // Almacenar el resultado de la consulta 2 en el ArrayList
        while (rs2.next()) {
            String[] partida = new String[1];
            partida[0] = rs2.getString("IdPartida");
            partidas2.add(partida);
            out.println("Resultado de consultaNoTeToca: " + rs2.getString("IdPartida")); // Agregado aquí
        }

        //pstmt.close();
        rs.close();
        con.close();
        out.println(" "+nombre+" "+jugador+" ");
        out.println(Arrays.deepToString(partidas.toArray()));

    } catch (Exception e) {
        e.printStackTrace();
    }
%>
<p>Bienvenido</p>
<p>Te toca:</p>
<table border="1">
    <tr>
        <th>IdPartida</th>
        <th>Link</th>
    </tr>
    <% for (String[] partida : partidas) { %>
    <tr>
        <td><%= partida[0] %></td>
        <td><a href="InsertarFichaServlet?IdPartida=<%= partida[0] %>">Insertar Ficha</a></td>
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
        <td><a href="InsertarFichaServlet?IdPartida=<%= partida[0] %>">Ver tablero</a></td>
    </tr>
    <% } %>
</table>
</body>
</html>