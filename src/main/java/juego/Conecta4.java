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

    public Conecta4(Connection con) { // con ya esta conectado a la base de datos (conectaServlet.java)
        try {
            this.con = con;
            this.st = con.createStatement();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void crearTablero(int partida) {
        try {
            con.setAutoCommit(false);
            String SQL = "INSERT INTO tablero VALUES (?, 6, 6, ?);";
            try (PreparedStatement preparedStatement = con.prepareStatement(SQL)) {
                preparedStatement.setInt(1, numeroTablero);
                preparedStatement.setInt(2, partida);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            for (int i = 0; i < 6; i++) { // para filas
                for (int j = 0; j < 6; j++) { // para columnas

                    SQL = "INSERT INTO detallestablero VALUES (" + numeroTablero + ",0,0," + i + "," + j + ");"; // pongo 0 para poner que no está ocupado (ver tabla)
                    st.executeUpdate(SQL);
                }
            }
            numeroTablero++;

            con.commit();
            con.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void crearPartida(int jugador1, int jugador2) {
        try {
            con.setAutoCommit(false);
            String SQL = "INSERT INTO partidas VALUES (?, ?, ?, ?);";
            try (PreparedStatement preparedStatement = con.prepareStatement(SQL)) {
                preparedStatement.setInt(1, numeroPartidas);
                preparedStatement.setInt(2, jugador1);
                preparedStatement.setInt(3, jugador2);
                preparedStatement.setInt(4, random.nextInt(2));
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Para crear partidas
            crearTablero(numeroPartidas);
            numeroPartidas++;

            //con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void crearPartidaEsperar(int IdJugador1) throws SQLException {

        con.setAutoCommit(false);
        boolean vacio = true;
        ResultSet rs2 = null;
            while (vacio) {
                SQL = "SELECT * FROM esperandoconexion WHERE IdPartida = ? ";
                try (PreparedStatement preparedStatement = con.prepareStatement(SQL)) {
                    preparedStatement.setInt(1, numeroPartidas);
                    rs2 = preparedStatement.executeQuery();

                    // Mueve el cursor a la primera fila
                    if (rs2.next()) {
                        vacio = false;

                        int jugador2 = rs2.getInt("IdJugador");
                        crearPartida(IdJugador1, jugador2);
                        SQL = "DELETE FROM esperandoconexion WHERE IdPartida = ?;";
                        PreparedStatement preparedStatement2 = con.prepareStatement(SQL);
                        preparedStatement2.setInt(1, numeroPartidas);
                        preparedStatement2.executeUpdate();
                        break;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                con.setAutoCommit(true);
            }

    }

    public void unirsePartida(int IdPartida, int IdJugador2){ // para que el jugador2 se una a la partida creada

        try {
            con.setAutoCommit(false);
            String SQL = "INSERT INTO esperandoconexion VALUES (?, ?);";
            try (PreparedStatement preparedStatement = con.prepareStatement(SQL)) {
                preparedStatement.setInt(1, IdJugador2);
                preparedStatement.setInt(2, IdPartida);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            //con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int consultarFicha(int idTablero, int fila, int columna) {

            SQL = "SELECT OcupadoJugador1, OcupadoJugador2 FROM detallestablero WHERE IdTablero=? AND Fila=? AND Columna=?";
            try (PreparedStatement preparedStatement = con.prepareStatement(SQL)) {
                preparedStatement.setInt(1, idTablero);
                preparedStatement.setInt(2, fila);
                preparedStatement.setInt(3, columna);

                rs = preparedStatement.executeQuery();

                // Mueve el cursor a la primera fila
                if (rs.next()) {
                    int ocupado1 = rs.getInt("OcupadoJugador1");
                    int ocupado2 = rs.getInt("OcupadoJugador2");

                    if (!(ocupado1 == 1 && ocupado2 == 1)) {
                        if (ocupado1 == 1) {
                            return 1;
                        } else if (ocupado2 == 1) {
                            return 2;
                        } else {
                            return 0;
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return -1;

    }


    public void insertarFicha(int idTablero, int fila, int columna, int jugador) {
        try {
            con.setAutoCommit(false);

            if (consultarFicha(idTablero, fila, columna) == 0) {
                for (int i = fila; i < 6; i++) { // para que las fichas empiecen desde la última fila
                    if (consultarFicha(idTablero, i, columna) == 0 ) {
                            fila = i; // comprobamos si esta ocupado, sino está ocupado se asigna esa fila
                    }

                    else{
                        break; // si esta ocupado se salta el break y se queda en la fila donde no esta ocupada (fila anterior)
                    }
                }
                System.out.println(fila);

                if (consultarFicha(idTablero, fila, columna) == 0) { //para actualizar el valor y el estado en la base de datos
                    String updateSQL;
                    if (jugador == 0) {
                        updateSQL = "UPDATE detallestablero SET OcupadoJugador1 = 1 WHERE IdTablero = ? AND Fila = ? AND Columna = ?;";
                    } else if (jugador == 1) {
                        updateSQL = "UPDATE detallestablero SET OcupadoJugador2 = 1 WHERE IdTablero = ? AND Fila = ? AND Columna = ?;";
                    } else {
                        throw new IllegalArgumentException("El jugador debe ser 0 o 1.");
                    }

                    try (PreparedStatement preparedStatement = con.prepareStatement(updateSQL)) {
                        preparedStatement.setInt(1, idTablero);
                        preparedStatement.setInt(2, fila);
                        preparedStatement.setInt(3, columna);
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
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


