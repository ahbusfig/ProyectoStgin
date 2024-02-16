package juego.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;

public class UnirsePartidaServlet extends HttpServlet{
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // Declaramos las variables
        Connection con; // para conectarse a la base de datos
        Statement st; // para ejecutar las consultas
        ResultSet rs; // para guardar el resultado de las consultas
        String SQL,codigoPartida; // para las consultas
        PrintWriter out = res.getWriter(); // para escribir la respuesta

        try {
            // Obtener la conexión a la base de datos
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/conecta4", "root", "");
            st = con.createStatement();

            // Obtener el código de partida desde los parámetros de la solicitud
            codigoPartida = req.getParameter("codigoPartida");
            // Obtener el id del usuario de la sesión
            int usuario2 = (int) req.getSession().getAttribute("idJugador");
            // guarda el idJugador 2 en la sesión
            req.getSession().setAttribute("idJugador2", usuario2);
            // Lógica para insertar jugador2 en la base de datos con el código de partida obtenido
            SQL = "UPDATE partidas SET Jugador2 = '" + usuario2 + "' WHERE CodigoPartida = '" + codigoPartida + "'";
            st.executeUpdate(SQL);

            // redirigir al usuario al servlet al juego
            res.sendRedirect("ListaPartidas.jsp");

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Error en el servidor.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            out.close();
        }

    }
}
