package juego.servlets;

import juego.Conecta4;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;

public class FinPartidaServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String idPartida = req.getParameter("idPartida");
        Connection con;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/conecta4", "root", "1234");
            String sql = "SELECT Jugador1, Jugador2 FROM partidas WHERE IdPartida = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idPartida);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int jugador1 = rs.getInt("Jugador1");
            int jugador2 = rs.getInt("Jugador2");

            // Instanciar el juego y crear el tablero
            Conecta4 juego = Conecta4.getInstance(con, Integer.parseInt(idPartida), jugador1, jugador2);


            int pJugador1 = 0;
            int pJugador2 = 0;
            for (int i = 0; i < 6; ++i) {
                pJugador1 += juego.sumarPuntosHorizontales(jugador1, i);
                pJugador2 += juego.sumarPuntosHorizontales(jugador2, i);
            }

            for (int i = 0; i < 6; ++i) {
                pJugador1 += juego.sumarPuntosVerticales(jugador1, i);
                pJugador2 += juego.sumarPuntosVerticales(jugador2, i);
            }

            for (int i = 0; i < 6; ++i) {
                for (int j = 0; j < 6; ++j) {
                    pJugador1 += juego.sumarPuntosDiagonales(jugador1, new int[]{i, j});
                    pJugador2 += juego.sumarPuntosDiagonales(jugador2, new int[]{i, j});
                }
            }
            req.getSession().setAttribute("pJugador1", pJugador1);
            req.getSession().setAttribute("pJugador2", pJugador2);

            // Extraer el número de partidas jugadas y el número de victorias
            int partidasJugadas1 = nPartidasJugadas(jugador1);
            int victorias1 = getVictorias(jugador1);

            int partidasJugadas2 = nPartidasJugadas(jugador2);
            int victorias2 = getVictorias(jugador2);

            if (pJugador1 == pJugador2) {
                victorias1++;
                victorias2++;
                partidasJugadas1++;
                partidasJugadas2++;
                sql = "UPDATE estadisticas SET PartidasJugadas = ?, Victorias = ? WHERE IdJugador = ?";
                ps = con.prepareStatement(sql);
                ps.setInt(1, partidasJugadas1);
                ps.setInt(2, victorias1);
                ps.setInt(3, jugador1);
                ps.executeUpdate();
                ps.setInt(1, partidasJugadas2);
                ps.setInt(2, victorias2);
                ps.setInt(3, jugador2);
                ps.executeUpdate();
            } else {
                int ganador = pJugador1 > pJugador2 ? jugador1 : jugador2;
                if (ganador == jugador1) {
                    victorias1++;
                } else {
                    victorias2++;
                }
                partidasJugadas1++;
                partidasJugadas2++;
                sql = "UPDATE estadisticas SET PartidasJugadas = ?, Victorias = ? WHERE IdJugador = ?";
                ps = con.prepareStatement(sql);
                ps.setInt(1, partidasJugadas1);
                ps.setInt(2, victorias1);
                ps.setInt(3, jugador1);
                ps.executeUpdate();
                ps.setInt(1, partidasJugadas2);
                ps.setInt(2, victorias2);
                ps.setInt(3, jugador2);
                ps.executeUpdate();
            }

            sql = "SELECT IdTablero FROM tablero WHERE IdPartida = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, idPartida);
            rs = ps.executeQuery();
            rs.next();
            int idTablero = rs.getInt("IdTablero");
            sql = "DELETE FROM detallestablero WHERE IdTablero = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idTablero);
            ps.executeUpdate();
            sql = "DELETE FROM tablero WHERE IdPartida = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, idPartida);
            ps.executeUpdate();
            sql = "DELETE FROM TURNO WHERE PARTIDA_ID = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, idPartida);
            ps.executeUpdate();
            sql = "DELETE FROM partidas WHERE IdPartida = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, idPartida);
            ps.executeUpdate();


            con.close();
            ps.close();
            rs.close();
            res.sendRedirect("finPartida.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }

    private int nPartidasJugadas(int idUsuario) throws ClassNotFoundException, SQLException {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        Class.forName("org.mariadb.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/conecta4", "root", "1234");
        String sql = "SELECT PartidasJugadas FROM estadisticas WHERE IdJugador = ?";
        ps = con.prepareStatement(sql);
        ps.setInt(1, idUsuario);
        rs = ps.executeQuery();
        if (!rs.next()) {
            sql = "INSERT INTO estadisticas (IdJugador, PartidasJugadas, Victorias) VALUES (?, 0, 0)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            con.close();
            ps.close();
            return 0;
        }
        con.close();
        ps.close();
        return rs.getInt("PartidasJugadas");
    }

    private int getVictorias(int idUsuario) throws ClassNotFoundException, SQLException {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        Class.forName("org.mariadb.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/conecta4", "root", "1234");
        String sql = "SELECT Victorias FROM estadisticas WHERE IdJugador = ?";
        ps = con.prepareStatement(sql);
        ps.setInt(1, idUsuario);
        rs = ps.executeQuery();
        if (!rs.next()) {
            sql = "INSERT INTO estadisticas (IdJugador, PartidasJugadas, Victorias) VALUES (?, 0, 0)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            con.close();
            ps.close();
            return 0;
        }
        con.close();
        ps.close();
        return rs.getInt("Victorias");
    }
}


