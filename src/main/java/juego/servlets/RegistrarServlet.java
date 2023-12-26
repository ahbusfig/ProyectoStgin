package juego.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class RegistrarServlet extends HttpServlet {
    // El método doPost recibe los datos del formulario y los guarda en la base de datos
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Connection con = null ;
        PreparedStatement pst = null;
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        // Capturar los datos del formulario
        String username = req.getParameter("Nombre");
        String email = req.getParameter("email");
        int age = Integer.parseInt(req.getParameter("edad"));
        String password = req.getParameter("password"); // En un caso real, deberías cifrar esto antes de guardarlo

        try {
            // Cargar el driver y conectarse a la base de datos
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/conecta4", "root", "");

            // Crear la sentencia SQL para insertar el nuevo usuario
            String sql = "INSERT INTO usuarios (username, email, age, password) VALUES (?, ?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, email);
            pst.setInt(3, age);
            pst.setString(4, password);

            // Ejecutar la inserción
            int rowCount = pst.executeUpdate();
            if (rowCount > 0) {
                out.println("<p>Registro exitoso!</p>");
                // Redirigir o enviar a una página de éxito
            } else {
                out.println("<p>Error al registrar usuario.</p>");
                // Manejar el error adecuadamente
            }
        } catch (ClassNotFoundException | SQLException e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
            // Manejar la excepción adecuadamente
        } finally {
            // Cerrar recursos
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
                if (out != null) out.close();
            } catch (SQLException ex) {
                out.println("<p>Error al cerrar recursos: " + ex.getMessage() + "</p>");
            }
        }
    }
}
