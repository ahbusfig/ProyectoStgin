package juego.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class RegistrarServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html"); // nos dice que la respuesta va a ser en HTML
        PrintWriter out = response.getWriter(); // para escribir la respuesta

        // Recoger los datos del formulario
        String nombre = request.getParameter("usuario");
        String password = request.getParameter("password");
        int edad = Integer.parseInt(request.getParameter("edad")); // el parseInt es para convertir el int a String
        String email = request.getParameter("email");

        try {
            // Cargar el driver JDBC y establecer conexión
            Class.forName("org.mariadb.jdbc.Driver"); // el driver es para conectarse a la base de datos (en este caso MariaDB)
            Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/conecta4", "root", "");


            // Crear la consulta SQL
            String query = "INSERT INTO jugadores (Nombre, Email, Edad, Password) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query); // el prepareStatement es para preparar la consulta

            // Establecer los parámetros y ejecutar la actualización
            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setInt(3, edad);
            ps.setString(4, password);
            int i = ps.executeUpdate();

            if(i > 0) {
                out.println("Registro exitoso!");
            }

        } catch(Exception e) {
            e.printStackTrace();
            out.println("Error en el registro: " + e.getMessage());
        } finally {
            // Cerrar el flujo de salida de datos hacia el cliente
            out.close();
        }
    }

}
