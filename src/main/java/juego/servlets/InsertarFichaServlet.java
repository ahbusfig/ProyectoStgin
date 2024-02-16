package juego.servlets;

import juego.Conecta4;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
public class InsertarFichaServlet extends  HttpServlet{

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
            // Pasar string a int del idJugador1 de la sesion
            int idJugador1 =  Integer.parseInt(request.getParameter("idJugador1"));
            // usar el método insertarFicha de la clase Conecta4
            int fila = juego.insertarFicha(idTablero, columna, esTurnoJugador1, idJugador1, idJugador);
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
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}