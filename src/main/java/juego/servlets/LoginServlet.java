package juego.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
public class LoginServlet extends HttpServlet {
    public  void  doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //declarar variables
        Connection con;
        Statement st;
        ResultSet rs;
        String SQL, nombre, password;
        PrintWriter out = response.getWriter(); // para escribir la respuesta

        try{
            // Recoger los datos del formulario
            nombre = request.getParameter("usuario");
            password = request.getParameter("password");

            //Cargar el driver y establecer la conexión
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/conecta4", "root", "");

            //Crear la consulta SQL
            SQL = "SELECT * FROM jugadores WHERE Nombre = '" + nombre + "' AND password = '" + password + "'";
            st = con.createStatement();

            //Ejecutar la consulta
            rs = st.executeQuery(SQL);

            //Comprobar si el usuario existe y la contraseña es correcta
            if(rs.next()){
                //El usuario existe y la contraseña es correcta
                HttpSession session = request.getSession(); // crea una sesión
                session.setAttribute("usuario", nombre); // guarda el nombre del usuario en la sesión
                session.setAttribute("idJugador", rs.getInt("IdJugador")); // guarda el id del usuario en la sesión
                st.close();
                con.close();
                response.sendRedirect("JuegoOpciones.html");
            }
            else{
                //El usuario no existe o la contraseña es incorrecta
                response.sendRedirect("login.html");
            }
        }
        catch(Exception e){
            e.printStackTrace();
            out.println("Error en el login: " + e.getMessage());
        }

        out.close();

    }
}