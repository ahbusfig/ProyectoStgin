package juego.servlets;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
public class TurnoEstado extends HttpServlet {
    // Aqui se procesa -> si es el turno de un jugador
    // Si es el turno de ese jugador -> se redirige a InsertarFichaServlet
    // Y finalmente se redirige a mostrarTablero.jsp
    // Por otro lado si no es tu turno -> se redirige a mostrarTablero.jsp
    // Los botonoes de insertar ficha estan deshabilitados en mostrarTablero.jsp pero si en insertarFichaServlet
    // Tenemos que saber -> como sacar el turno partido de la base de datos
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Connection con ;
        try {
            // Obtener la conexión a la base de datos (ajusta la URL, usuario y contraseña según tu configuración)
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/conecta4", "root", "");
            // Sacar idJugador de la sesion
            int idJugador = (int) req.getSession().getAttribute("idJugador");
            // Sacar idJugador1 de la sesion
            int idJugador1 = (int) req.getSession().getAttribute("idJugador1");
            // Sacar idJugador2 de la sesion
            int idJugador2 = (int) req.getSession().getAttribute("idJugador2");
            // Sacar codigoPartida de la sesion
            String codigoPartida = (String) req.getSession().getAttribute("codigoPartida");
            // Declarar variable Turno -> Sacar de la base de datos
            String sql = "SELECT Turno FROM partidas WHERE codigoPartida = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, codigoPartida);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int Turno = rs.getInt(1);
            // Logica para saber a que jugador le toca el turno
            if(Turno == 0 && idJugador == idJugador1){
                //redirect to insertarFicha.servlet
                res.sendRedirect("InsertarFichaServlet");
            }
            else if (Turno == 1 && idJugador == idJugador2){
                //redirect to insertarFicha.servlet
                res.sendRedirect("InsertarFichaServlet");
            }
            else if (Turno == 1 && idJugador == idJugador1){
                //redirect to mostrarTablero
                res.sendRedirect("mostrarTablero.jsp");

            }else if (Turno == 0 && idJugador == idJugador2){
               // redirect to mostrarTablero.
                res.sendRedirect("mostrarTablero.jsp");
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
}
