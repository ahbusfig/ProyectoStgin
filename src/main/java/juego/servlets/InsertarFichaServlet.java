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
            int idPartida = Integer.parseInt(request.getParameter("idPartida"));
            String sql = "SELECT Jugador1, Jugador2 FROM partidas WHERE IdPartida = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, String.valueOf(idPartida));
            ResultSet rs = ps.executeQuery();
            rs.next();
            int jugador1 = rs.getInt("Jugador1");
            int jugador2 = rs.getInt("Jugador2");

            Conecta4 juego = Conecta4.getInstance(con, idPartida, jugador1, jugador2);
            // Obtener los parámetros de la petición
            int idTablero = Integer.parseInt(request.getParameter("idTablero"));

            // obtener el idPartida de la database

            //String idJugadorParam = request.getParameter("idJugador");
            int columna = Integer.parseInt(request.getParameter("columna"));
            // Pasar string a int del idJugador
            int idJugador =  Integer.parseInt(request.getParameter("idJugador"));

            juego.insertarFicha(columna, idJugador);

            // obtener el id del otro jugador
            sql = "SELECT Jugador1, Jugador2 FROM partidas WHERE IdPartida = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPartida);
            rs = ps.executeQuery();
            int other = -1;
            while (rs.next()) {
                if (rs.getInt("Jugador1") == idJugador) {
                    other = rs.getInt("Jugador2");
                } else {
                    other = rs.getInt("Jugador1");
                }
            }

            // usar el método insertarFicha de la clase Conecta4
            // int fila = juego.insertarFicha(idTablero, columna, esTurnoJugador1);
            // Cambiar el valor de Turno en la database
            sql = "INSERT INTO TURNO (PARTIDA_ID, JUGADOR_ID, TIMESTAMP) VALUES ('" + idPartida + "', '" + other + "', CURRENT_TIMESTAMP)";
            ps = con.prepareStatement(sql);
            ps.executeUpdate();

            con.close();
            // Enviar a otra página y mandar los datos necesarios
            response.sendRedirect("conecta4Juego.jsp?IdPartida="+ idPartida); // + fila + "&columna=" + columna + "&idJugador=" + idJugador + "&codigoPartida=" + codigoPartida);

            // Cerrar la conexión

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doPost(req, res);
    }
}