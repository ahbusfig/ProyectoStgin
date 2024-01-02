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

            // Lógica para insertar el código de partida y jugador2 en la base de datos

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
