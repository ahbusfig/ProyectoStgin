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
            int idPartida = Integer.parseInt(request.getParameter("idPartida"));
            int idJugador = Integer.parseInt(request.getParameter("idJugador"));
            int columna = Integer.parseInt(request.getParameter("columna"));
            int fila = Integer.parseInt(request.getParameter("fila"));
            // usar el método insertarFicha de la clase Conecta4
            juego.insertarFicha(idPartida, idJugador, columna, fila);

            // Cambiar el turno del jugador
            String SQL = "UPDATE partida SET turno = ? WHERE idPartida = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(SQL)) {
                // Si el turno actual es del jugador 1, cambia a 2, y viceversa
                int nuevoTurno = (idJugador == 1) ? 2 : 1;
                preparedStatement.setInt(1, nuevoTurno);
                preparedStatement.setInt(2, idPartida);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // crear objeto json para la respuesta
            String color = idJugador == 0 ? "red" : "yellow";
            JsonObject jsonResponse = Json.createObjectBuilder()
                    .add("color", color)
                    .build();
            // Enviar la respuesta
            response.setContentType("application/json");
            response.getWriter().println(jsonResponse.toString());

            // Cerrar la conexión
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


