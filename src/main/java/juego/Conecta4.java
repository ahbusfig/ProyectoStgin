package juego;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;
import java.util.Random;

public class Conecta4 {

    Connection con;
    Statement st;
    ResultSet rs;
    int numeroTablero = 0;
    int numeroPartidas = 0;
    Random random = new Random();
    String SQL;
    public Conecta4 (Connection con){ // con ya esta conectado a la base de datos (conectaServlet.java)
        try {
            this.con = con;
            this.st = con.createStatement();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void crearTablero(int partida) {
        try {
            con.setAutoCommit(false);
            SQL = "INSERT INTO tablero VALUES (" + numeroTablero + "6,6," + partida + ")";
            rs = st.executeQuery(SQL);

            for (int i = 0; i < 6; i++) { // para filas
                for (int j = 0; j < 6; j++) { // para columnas

                    SQL = "INSERT INTO detallestablero VALUES (" + numeroTablero + ",0,0," + i + "," + j + ")"; // pongo 0 para poner que no está ocupado (ver tabla)
                    rs = st.executeQuery(SQL);
                }
            }
            numeroTablero++;

            con.commit();
            con.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void crearPartida (int jugador1, int jugador2) {
        try {
            con.setAutoCommit(false);
            SQL = "INSERT INTO partidas VALUES (" + numeroPartidas + "," + jugador1 + "," + jugador2 + "," + random.nextInt(2) + ")"; // el random es para que el turno se asigne aleatoriamente al inicio
            rs = st.executeQuery(SQL);

            // Para crear partidas
            crearTablero(numeroPartidas);
            numeroPartidas++;

            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int consultarFicha(int idTablero, int fila, int columna){ // si no esta ocupado devuelve 0
        try{
            SQL = "SELECT OcupadoJugador1, OcupadoJugador2 FROM detallestablero WHERE IdTablero=" +idTablero+" AND Fila=" +fila+" AND Columna=" +columna;
            rs = st.executeQuery(SQL);
            int ocupado1;
            ocupado1 = rs.getInt("OcupadoJugador1"); // para saber la casilla ocupada por jugador1 si devuelve un 0 no esta ocupado
            int ocupado2;
            ocupado2 = rs.getInt("OcupadoJugador2");

            if(!(ocupado1 == 1 && ocupado2 == 1)){ // condición de que ambos jugadores no ocupen la misma casilla
                if(ocupado1 == 1){
                    return 1;
                }
                else if (ocupado2 == 1){
                    return 2;
                }
                else{
                    return 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1; // por si la consulta ha salido mal
    }
    public void insertarFicha(int idTablero, int fila, int columna, int jugador) { // cuando jugador es 0 es jugador1 y si es 1 será jugador2
        try{
            con.setAutoCommit(false);

            if(consultarFicha(idTablero,fila,columna) == 0) {

                for (int i = fila - 1; i >= 0; i--) { // para que las fichas empiecen desde la ultima fila
                    if (consultarFicha(idTablero, i, columna) == 0) {
                        fila = i; // si no está ocupado
                    }
                }

                if (consultarFicha(idTablero, fila, columna) == 0) {
                    if (jugador == 0) {
                        SQL = "UPDATE detallestablero SET OcupadoJugador1 = 1";
                        rs = st.executeQuery(SQL);
                    } else if (jugador == 1) {
                        SQL = "UPDATE detallestablero SET OcupadoJugador2 = 1";
                        rs = st.executeQuery(SQL);
                    }
                }
            }
            con.commit();
            con.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}

