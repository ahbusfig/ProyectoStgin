<%--
  Created by IntelliJ IDEA.
  User: alain
  Date: 02/01/2024
  Time: 22:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>conecta4 - juego</title>
</head>
<body>
    <h1>conecta4 - juego</h1>
    <!-- instanciar juego -->
    <%@ page import="java.sql.*" %>
    <%@ page import="juego.Conecta4" %>

    <%
        // conexion a la base de datos
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/conecta4", "root", "");

        // instanciar juego
        Conecta4 juego = new Conecta4(con);
        // partida actual
        int idPartida = Integer.parseInt(request.getParameter("idPartida"));
        // crear tablero
        juego.crearTablero(idPartida);

    %>
</body>
</html>
