package juego.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;

public class CrearPartidaServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // Establecer la conexión a la base de datos
        Connection con;
        Statement st;
        ResultSet rs;
        String SQL, codigoPartida;
        PrintWriter out = res.getWriter(); // para escribir la respuesta

        try {
            // Obtener la conexión a la base de datos (ajusta la URL, usuario y contraseña según tu configuración)
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/conecta4", "root", "");
            st = con.createStatement();

            // Generar codigo de 6 digitos aleatorio en variable codigoPartida
            // GENERAR CODIGO DE PARTIDA
            codigoPartida = String.valueOf((int) (Math.random() * 900000) + 100000);
            // Almacena el código de partida en la sesión
            req.getSession().setAttribute("codigoPartida", codigoPartida);
            // Obtener el id del usuario de la sesión

            int usuario1 =  (int) req.getSession().getAttribute("idJugador");
            //Guardar el id del jugador 1 en la sesión
            req.getSession().setAttribute("idJugador1", usuario1);
            // Aleatorizar el turno para que bien empieze el jugador 1 o el jugador 2
            // Lógica para insertar el código de partida, jugador1 y turno en la base de datos
            // Generar un número aleatorio (0 o 1) para representar el turno inicial

            // si es 0 el turno es del jugador 1 si es 1 el turno es del jugador 2
            // Guardar el turno en la sesión



            // Lógica para insertar el código de partida,jugador1 y turno en la base de datos
            SQL = "INSERT INTO partidas (Jugador1, Jugador2, CodigoPartida) VALUES ('" + usuario1 + "', NULL, " + codigoPartida + ")";

            st.executeUpdate(SQL);


            // Almacenar el id de la partida en la sesión
            // Suponiendo que CodigoPartida es único y acabas de insertar la partida
            SQL = "SELECT IdPartida FROM partidas WHERE CodigoPartida = '" + codigoPartida + "'";
            rs = st.executeQuery(SQL);
            if(rs.next()) {
                int idPartida = rs.getInt("IdPartida");
                req.getSession().setAttribute("idPartida", idPartida);

            }
            // Redirigir al usuario a la página de esperandoConexion -> para que espere a que se una otro jugador
            res.sendRedirect("esperandoConexion.jsp");



        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Error con el SQL en el servidor.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            out.close();
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }
}


