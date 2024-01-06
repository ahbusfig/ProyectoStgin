package juego.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
// Objetivo del servlet es que se use periodicamente para comprobar si hay un segundo jugador en la partida en esperandoConexion.jsp
public class esperarConexionServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json"); // Indicamos que la respuesta es JSON
        PrintWriter out = response.getWriter(); // Para escribir la respuesta
        int idPartida = (int) request.getSession().getAttribute("idPartida"); // Obtenemos el id de la partida de la sesión

        try (
            // Obtener la conexión a la base de datos (ajusta la URL, usuario y contraseña según tu configuración)
            Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/conecta4", "root", "")) {
            Statement st = con.createStatement();
            // Lógica para comprobar si hay un segundo jugador en la partida
            String SQL = "SELECT * FROM partidas WHERE IdPartida = " + idPartida;
            ResultSet rs = st.executeQuery(SQL);
            // Si hay un segundo jugador, devolvemos un JSON con status: "found"
            if (rs.next()) {
                int jugador2 = rs.getInt("Jugador2");
                if (jugador2 != 0) {  // Suponiendo que '0' significa que no hay segundo jugador
                    out.println("{\"status\": \"found\"}");

                    // Eliminar la partida de la tabla esperandoconexion (ya no es necesaria)
                    SQL = "DELETE FROM esperandoconexion WHERE IdPartida = " + idPartida;
                    st.executeUpdate(SQL);
                    // Redirigir al usuario a la página de juego
                    response.sendRedirect("conecta4Juego.jsp");

                } else {
                    out.println("{\"status\": \"waiting\"}");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("{\"status\": \"error\"}");
        }
    }
}
