package juego;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.*;

public class Pruebas {


    public static void main(String[] args) {

        Connection con;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println(e);
            return;
        }

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/conecta4", "root", "");

            Conecta4 prueba = new Conecta4(con);
            prueba.crearPartida(0,1);
            System.out.println(prueba.consultarFicha(0,4,3));
            prueba.insertarFicha(0, 0, 5, 1);
            System.out.println(prueba.consultarFicha(0,4,3));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


