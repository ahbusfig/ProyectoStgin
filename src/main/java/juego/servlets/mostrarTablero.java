package juego.servlets;
import juego.Conecta4;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
public class mostrarTablero extends HttpServlet {
    public  void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection con;
        try {
            // Obtener la conexión a la base de datos (ajusta la URL, usuario y contraseña según tu configuración)
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/conecta4", "root", "");
            // Crear un objeto de la clase Conecta4 para acceder a los metodos de la clase
            Conecta4 juego = new Conecta4(con);
            // Obtener los parámetros de la petición
            int idTablero = Integer.parseInt(request.getParameter("idTablero"));
            // obtener el idPartida de la database
            String sqlIdPartida = "SELECT IdPartida FROM tablero WHERE IdTablero = ?";
            PreparedStatement psIdPartida = con.prepareStatement(sqlIdPartida);
            psIdPartida.setInt(1, idTablero);
            ResultSet rsIdPartida = psIdPartida.executeQuery();
            rsIdPartida.next();
            int idPartida = rsIdPartida.getInt(1);
            System.out.println("idPartida: " + idPartida);
            //String idJugadorParam = request.getParameter("idJugador");
            int columna = Integer.parseInt(request.getParameter("columna"));
            boolean esTurnoJugador1 = Boolean.parseBoolean(request.getParameter("Turno"));
            // Pasar string a int del idJugador
            int idJugador =  Integer.parseInt(request.getParameter("idJugador"));
            // usar el método insertarFicha de la clase Conecta4
            int fila = juego.insertarFicha(idTablero, columna, esTurnoJugador1);
            // Cambiar el valor de Turno en la database
            String sql = "UPDATE partidas SET Turno = ? WHERE IdPartida = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBoolean(1, !esTurnoJugador1);
            ps.setInt(2, idPartida);
            ps.executeUpdate();
            // Enviar a otra página y mandar los datos necesarios
            // response.sendRedirect("mostrarTablero.jsp "); // + fila + "&columna=" + columna + "&idJugador=" + idJugador + "&codigoPartida=" + codigoPartida);
            // Cerrar la conexión
            con.close();

            // Generar el HTML
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Conecta4 - Juego</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div id='contenedor-juego'>");
            out.println("<h1>Conecta4 - Juego</h1>");
            out.println("<div id='tablero' data-idTablero='" + idTablero + "' data-jugador='" + idJugador + "'>");
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    out.println("<div class='celda' id='celda_" + i + "_" + j + "' data-fila='" + i + "' data-columna='" + j + "' data-idTablero='" + idTablero + "' data-jugador='" + idJugador + "'></div>");
                }
            }
            out.println("</div>");
            out.println("<p id='turnoJugador'>Turno del Jugador: " + (esTurnoJugador1 ? '1' : '2') + "</p>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}