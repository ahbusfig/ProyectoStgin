package juego.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
//Hemos cambiado el jakarta por el javax porque no nos funcionaba
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class TurnoEstado extends HttpServlet {
    // Aqui se procesa -> si es el turno de un jugador
    // Si es el turno de ese jugador -> se redirige a InsertarFichaServlet
    // Y finalmente se redirige a mostrarTablero.jsp
    // Por otro lado si no es tu turno -> se redirige a mostrarTablero.jsp
    // Los botonoes de insertar ficha estan deshabilitados en mostrarTablero.jsp pero si en insertarFichaServlet
    // Tenemos que saber -> como sacar el turno partido de la base de datos
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Connection con ;
        PreparedStatement st;
        ResultSet rs;
        try {
            // Obtener la conexión a la base de datos (ajusta la URL, usuario y contraseña según tu configuración)
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/conecta4", "root", "");
            String codigoPartida = (String) req.getSession().getAttribute("codigoPartida");
            String SQL = "SELECT IdPartida FROM partidas WHERE CodigoPartida = '" + codigoPartida + "'";
            st = con.prepareStatement(SQL);
            rs = st.executeQuery();
            rs.next();
            String idPartida = rs.getString("IdPartida");
            // Sacar idJugador de la sesion
            int idJugador = (int) req.getSession().getAttribute("idJugador");
            // Sacar idJugador1 de la sesion
            SQL = "SELECT Jugador1 FROM partidas WHERE IdPartida = ?";
            PreparedStatement ps1 = con.prepareStatement(SQL);
            ps1.setString(1, (String) idPartida);
            ResultSet rs1 = ps1.executeQuery();
            rs1.next();
            int idJugador1 = rs1.getInt("Jugador1");
            // Sacar idJugador2 de la sesion
            SQL = "SELECT Jugador2 FROM partidas WHERE IdPartida = ?";
            ps1 = con.prepareStatement(SQL);
            ps1.setString(1, (String) idPartida);
            rs1 = ps1.executeQuery();
            rs1.next();
            int idJugador2 = rs1.getInt("Jugador2");


            // Declarar variable Turno -> Sacar de la base de datos
            String sql = "SELECT JUGADOR_ID FROM TURNO WHERE PARTIDA_ID = ? ORDER BY TIMESTAMP DESC LIMIT 1";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idPartida);
            rs = ps.executeQuery();
            rs.next();
            int Turno = rs.getInt("JUGADOR_ID");
            if (Turno == 0){
                //redirect to insertarFicha.servlet
                if (idJugador == idJugador1)
                    res.sendRedirect("InsertarFichaServlet");
                else
                    res.sendRedirect("mostrarTablero");
            }
            else if (Turno == 1){
                //redirect to insertarFicha.servlet
                if (idJugador == idJugador2)
                    res.sendRedirect("InsertarFichaServlet");
                else
                    res.sendRedirect("mostrarTablero");
            }
            else {
                //redirect to mostrarTablero.jsp
                System.out.println("Error en el turno");
            }

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doPost(req, res);
    }
}
