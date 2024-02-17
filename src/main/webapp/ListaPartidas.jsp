<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.PrintWriter" %>
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
        String consultaIdPartida = "Select IdPartida from partidas where (Jugador1 =" +jugador+ " OR Jugador2=" + jugador+ ") order by IdPartida desc";
        rs = st.executeQuery(consultaIdPartida);

        // 1. recorrer el ResultSet para obtener el id de las partida
        while (rs.next()){
            //Extraer el id de la partida
            int idPartida = rs.getInt("IdPartida");
            // 2. Consultar si es el turno del jugador usando tabla Turno
            String consultaTeToca = "Select JUGADOR_ID from turno where PARTIDA_ID = " + idPartida + " order by TIMESTAMP desc limit 1";
            // hacer la consulta
            rs = st.executeQuery(consultaTeToca);//Tiene el ultimo turno
            // Guardar en una variable y comparar idJugador para saber que jugador tiene el turno
            int idJugador = rs.getInt("JUGADOR_ID");
            // Comparar si el idJugador es igual al id del jugador que ha iniciado sesión y segun eso ponerla en una lista o otra
            String[] partida = new String[1];
            partida[0] = Integer.toString(idPartida);
            if (idJugador == jugador){
                // Si es igual, añadir a la lista de partidas en las que le toca

                partidas.add(partida);
            } else {
                // Si no es igual, añadir a la lista de partidas en las que no le toca

                partidas2.add(partida);
            }

        }


        rs.close();
        con.close();
        out.println(" "+nombre+" "+jugador+" ");
        out.println(Arrays.deepToString(partidas.toArray()));

    } catch (Exception e) {
        e.printStackTrace();
    }
%>
<p>Bienvenido <%= nombre %> </p>
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