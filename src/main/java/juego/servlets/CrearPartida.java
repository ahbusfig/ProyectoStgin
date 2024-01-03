package juego.servlets;

import juego.Conecta4;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
public class CrearPartida extends HttpServlet {
    public  void  doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //declarar variables
        Connection con;
        Statement st;
        ResultSet rs;
        String SQL, nombre, password;
        PrintWriter out = response.getWriter(); // para escribir la respuesta

        try {
            // Recoger los datos del formulario
            nombre = request.getParameter("usuario");
            password = request.getParameter("password");

            //Cargar el driver y establecer la conexión
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/conecta4", "root", "");

            Conecta4 crearPartida = new Conecta4(con);

            HttpSession session = request.getSession(); // crea una sesión
            int idPartida = crearPartida.crearPartidaEsperar((Integer) session.getAttribute("idJugador"));
            st = con.createStatement();
            session.setAttribute("idPartida", idPartida);

            st.close();
            con.close();
            response.sendRedirect("esperandoConexion.jsp");
        }
        catch(Exception e){
            e.printStackTrace();
            out.println("Error en el login: " + e.getMessage());
        }

        out.close();

    }
}