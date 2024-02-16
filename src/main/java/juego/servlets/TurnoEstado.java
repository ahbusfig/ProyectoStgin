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
        // Obtener el id del jugador de la sesión
        int idJugador = (int) req.getSession().getAttribute("idJugador");
        // Obtener el id de la partida de la sesión
        int idPartida = (int) req.getSession().getAttribute("idPartida");
        // Obtener el turno de la sesión
        int Turno = (int) req.getSession().getAttribute("Turno");
        // Obtener el id del jugador 1 de la sesión
        int idJugador1 = (int) req.getSession().getAttribute("idJugador1");
        // Obtener el id del jugador 2 de la sesión
        int idJugador2 = (int) req.getSession().getAttribute("idJugador2");
        // Si el id del jugador es igual al id del jugador 1
        if (idJugador == idJugador1) {
            // Si el turno es 0
            if (Turno == 0) {
                // Redirigir al usuario a la página de juego
                res.sendRedirect("mostrarTablero.jsp");
            } else {
                // Redirigir al usuario a la página de juego
                res.sendRedirect("mostrarTablero.jsp");
            }
        } else {
            // Si el id del jugador es igual al id del jugador 2
            if (idJugador == idJugador2) {
                // Si el turno es 1
                if (Turno == 1) {
                    // Redirigir al usuario a la página de juego
                    res.sendRedirect("mostrarTablero.jsp");
                } else {
                    // Redirigir al usuario a la página de juego
                    res.sendRedirect("mostrarTablero.jsp");
                }
            }
        }
    }
}
