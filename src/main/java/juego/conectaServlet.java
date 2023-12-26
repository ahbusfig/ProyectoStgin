package juego;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;


@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class conectaServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        Connection con;
        Statement st;
        ResultSet rs, rs2;
        String SQL;
        PrintWriter out;

        try{
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception e){
            System.out.println(e);
            return;
        }

        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/conecta4", "root" , "");
            st = con.createStatement();



        }

        catch(Exception e){
            System.out.println(e);
            return;
        }
    }
}
