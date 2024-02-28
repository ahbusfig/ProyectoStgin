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
            req.getSession().setAttribute("codigoPartida", codigoPartida);
            // Obtener el id del usuario de la sesión
            int usuario2 = (int) req.getSession().getAttribute("idJugador");
            // guarda el idJugador 2 en la sesión
            req.getSession().setAttribute("idJugador2", usuario2);
            // Lógica para insertar jugador2 en la base de datos con el código de partida obtenido
            SQL = "SELECT IdPartida FROM partidas WHERE CodigoPartida = '" + codigoPartida + "'";
            rs = st.executeQuery(SQL);
            rs.next();
            String idPartida = rs.getString("IdPartida");
            System.out.println(idPartida);
            SQL = "UPDATE partidas SET Jugador2 = '" + usuario2 + "' WHERE CodigoPartida = '" + codigoPartida + "'";
            st.executeUpdate(SQL);
            SQL = "SELECT Jugador1 FROM partidas WHERE CodigoPartida = '" + codigoPartida + "'";
            rs = st.executeQuery(SQL);
            rs.next();
            String usuario1 = rs.getString("Jugador1");
            int Turno = (int) (Math.random() * 2);
            if (Turno == 0) {
                SQL = "INSERT INTO TURNO (PARTIDA_ID, JUGADOR_ID, TIMESTAMP) VALUES ('" + idPartida + "', '" + usuario1 + "', CURRENT_TIMESTAMP)";
                st.executeUpdate(SQL);
            } else {
                SQL = "INSERT INTO TURNO (PARTIDA_ID, JUGADOR_ID, TIMESTAMP) VALUES ('" + idPartida + "', '" + usuario2 + "', CURRENT_TIMESTAMP)";
                st.executeUpdate(SQL);
            }

            // redirigir al usuario al servlet TurnoEstado
            res.sendRedirect("JuegoOpciones.html");

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Error en el servidor." + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            out.close();
        }

    }
}


