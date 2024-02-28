package juego.servlets;

import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
// Objetivo del servlet es que se use periodicamente para comprobar si hay un segundo jugador en la partida en esperandoConexion.jsp
public class ObtenerUltimoTurnoServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/conecta4", "root", "");

            String idPartida = req.getParameter("idPartida");

            String sql = "SELECT JUGADOR_ID FROM TURNO WHERE PARTIDA_ID = ? ORDER BY TIMESTAMP DESC LIMIT 1";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idPartida);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int ultimoTurno = rs.getInt("JUGADOR_ID");

            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print("{\"turno\"" + ": " + ultimoTurno + "}");
            con.close();
            out.flush();

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

