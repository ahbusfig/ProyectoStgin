package juego;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.Random;

public class Conecta4 {

    Connection con;
    Statement st;
    ResultSet rs;
    int numeroTablero = 0;
    int numeroPartidas = 0;
    Random random = new Random();
    String SQL;

    private int idTablero;

    public Conecta4(Connection con) { // con ya esta conectado a la base de datos (conectaServlet.java)
        try {
            this.con = con;
            this.st = con.createStatement();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void crearTablero(int idPartida) throws SQLException {
        // Comenzar una transacción
        con.setAutoCommit(false);

        try {
            // Comprobar si ya existe un tablero para la idPartida
            String checkSQL = "SELECT IdTablero FROM tablero WHERE IdPartida = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkSQL);
            checkStmt.setInt(1, idPartida);
            ResultSet rs = checkStmt.executeQuery();

            // Si no existe, insertar un nuevo tablero
            if(!rs.next()) {
                String insertSQL = "INSERT INTO tablero (Columnas, Filas, IdPartida) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                insertStmt.setInt(1, 6); // Suponiendo que siempre son 6 columnas
                insertStmt.setInt(2, 6); // Suponiendo que siempre son 6 filas
                insertStmt.setInt(3, idPartida);
                insertStmt.executeUpdate();

                // Obtener el IdTablero generado
                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Obtener el IdTablero generado
                    int newIdTablero = generatedKeys.getInt(1);

                    // Insertar detalles del tablero (detallestablero) con el nuevo IdTablero
                    for (int i = 0; i < 6; i++) { // para filas
                        for (int j = 0; j < 6; j++) { // para columnas
                            String detailsSQL = "INSERT INTO detallestablero (IdTablero, OcupadoJugador1, OcupadoJugador2, Fila, Columna) VALUES (?, 0, 0, ?, ?);";
                            PreparedStatement detailsStmt = con.prepareStatement(detailsSQL);
                            detailsStmt.setInt(1, newIdTablero);
                            detailsStmt.setInt(2, i);
                            detailsStmt.setInt(3, j);
                            detailsStmt.executeUpdate();
                        }
                    }
                    // Actualizar el idTablero
                    this.idTablero = newIdTablero;
                }

            }

            // Si todo ha ido bien, hacer commit de la transacción
            con.commit();
        } catch (SQLException e) {
            // Si hay un error, hacer rollback de la transacción
            con.rollback();
            throw e; // Lanzar la excepción para manejarla en un nivel superior
        } finally {
            // Restablecer el modo de auto commit
            con.setAutoCommit(true);
        }
    }





//    public int IdTableroActual(){ // para conseguir una ID libre para crear una nueva partida
//        try {
//            SQL = "SELECT IdTablero FROM tablero ORDER BY IdTablero DESC LIMIT 1";
//            rs = st.executeQuery(SQL);
//            if(rs.next()){
//                return rs.getInt("IdTablero") + 1;
//            }
//            return 0;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public int IdPartidaActual(){ // para conseguir una ID libre para crear una nueva partida
//        try {
//            SQL = "SELECT IdPartida FROM partidas ORDER BY IdPartida DESC LIMIT 1";
//            rs = st.executeQuery(SQL);
//            if(rs.next()){
//                return rs.getInt("IdPartida") + 1;
//            }
//            return 0;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    private void crearPartida(int jugador1, int jugador2) {
//        try {
//            con.setAutoCommit(false);
//            String SQL = "INSERT INTO partidas VALUES (?, ?, ?, ?);";
//            try (PreparedStatement preparedStatement = con.prepareStatement(SQL)) {
//                preparedStatement.setInt(1, numeroPartidas);
//                preparedStatement.setInt(2, jugador1);
//                preparedStatement.setInt(3, jugador2);
//                preparedStatement.setInt(4, random.nextInt(2));
//                preparedStatement.executeUpdate();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//            // Para crear partidas
//            crearTablero(numeroPartidas);
//
//            //con.commit();
//            con.setAutoCommit(true);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }


//    public int crearPartidaEsperar(int IdJugador1) throws SQLException {
//
//        con.setAutoCommit(false);
//        boolean vacio = true;
//        ResultSet rs2 = null;
//        numeroPartidas = IdPartidaActual();
//        numeroTablero = IdTableroActual();
//        System.out.println(numeroPartidas);
//        System.out.println(numeroTablero);
//
//        while (vacio) {
//            SQL = "SELECT * FROM esperandoconexion WHERE IdPartida = ? ";
//            try (PreparedStatement preparedStatement = con.prepareStatement(SQL)) {
//                preparedStatement.setInt(1, numeroPartidas);
//                rs2 = preparedStatement.executeQuery();
//
//                // Mueve el cursor a la primera fila
//                if (rs2.next()) {
//                    vacio = false;
//
//                    int jugador2 = rs2.getInt("IdJugador");
//                    crearPartida(IdJugador1, jugador2);
//                    SQL = "DELETE FROM esperandoconexion WHERE IdPartida = ?;";
//                    PreparedStatement preparedStatement2 = con.prepareStatement(SQL);
//                    preparedStatement2.setInt(1, numeroPartidas);
//                    preparedStatement2.executeUpdate();
//                    break;
//                }
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//            con.setAutoCommit(true);
//        }
//        return numeroPartidas;
//    }

//    public void unirsePartida(int IdPartida, int IdJugador2){ // para que el jugador2 se una a la partida creada
//
//        try {
//            con.setAutoCommit(false);
//            String SQL = "INSERT INTO esperandoconexion VALUES (?, ?);";
//            try (PreparedStatement preparedStatement = con.prepareStatement(SQL)) {
//                preparedStatement.setInt(1, IdJugador2);
//                preparedStatement.setInt(2, IdPartida);
//                preparedStatement.executeUpdate();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//            //con.commit();
//            con.setAutoCommit(true);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public int[] consultarColumna(int idTablero, int columna) {
        int[] estadoColumna = new int[6];
        for (int i = 0; i < 6; i++) {
            SQL = "SELECT OcupadoJugador1, OcupadoJugador2 FROM detallestablero WHERE IdTablero=? AND Fila=? AND Columna=?";
            try (PreparedStatement preparedStatement = con.prepareStatement(SQL)) {
                preparedStatement.setInt(1, idTablero);
                preparedStatement.setInt(2, i);
                preparedStatement.setInt(3, columna);

                rs = preparedStatement.executeQuery();

                // Mueve el cursor a la primera fila
                if (rs.next()) {
                    int ocupado1 = rs.getInt("OcupadoJugador1");
                    int ocupado2 = rs.getInt("OcupadoJugador2");

                    if (!(ocupado1 == 1 && ocupado2 == 1)) {// si no hay fichas de los dos jugadores
                        if (ocupado1 == 1) {// si hay ficha del jugador 1
                            estadoColumna[i] = 1;// se pone un 1
                        } else if (ocupado2 == 1) {
                            estadoColumna[i] = 2;
                        } else {
                            estadoColumna[i] = 0;
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return estadoColumna;
    }

    public int consultarFicha(int idTablero, int fila, int columna) {
        int[] estadoColumna = consultarColumna(idTablero, columna);
        return estadoColumna[fila];
    }

    public int insertarFicha(int idTablero, int columna, boolean TurnoJugador2, int idJugador1, int idJugador) {
        try {
            con.setAutoCommit(false);

            // Calcular la fila donde se insertará la ficha
            int fila = -1;
            for (int i = 5; i >= 0; i--) {
                if (consultarFicha(idTablero, i, columna) == 0) {
                    fila = i;
                    break;
                }
            }

            // Si la columna está llena, no hacer nada
            if (fila == -1) {
                return fila;
            }

            // Insertar la ficha
            String updateSQL;
            if (!TurnoJugador2 && idJugador == idJugador1 ) {
                updateSQL = "UPDATE detallestablero SET OcupadoJugador1 = 1 WHERE IdTablero = ? AND Fila = ? AND Columna = ?;";
                System.out.println("Ha tirado el jugador 1" + fila + " " + columna);
            } else {
                updateSQL = "UPDATE detallestablero SET OcupadoJugador2 = 1 WHERE IdTablero = ? AND Fila = ? AND Columna = ?;";
                System.out.println("Ha tirado el jugador 2" + fila + " " + columna);
            }

            try (PreparedStatement preparedStatement = con.prepareStatement(updateSQL)) {
                preparedStatement.setInt(1, idTablero);
                preparedStatement.setInt(2, fila);
                preparedStatement.setInt(3, columna);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            con.commit();
            con.setAutoCommit(true);

            // Imprimir la información de la ficha modificada
            System.out.println("Ficha modificada: Jugador " + (TurnoJugador2 ? "1" : "2") + ", Fila " + fila + ", Columna " + columna);

            // Devolver la fila donde se insertó la ficha
            return fila;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
        public int[][] estadoTraerTablero(int idTablero){ // metodo para recorrer el tablero entero y reutilizar en los siguientes metodos
        int[][] tablero = new int[6][6];
        for (int i = 0; i < 6; i++) { // para recorrer las filas
            for (int j = 0; j < 6; j++) { // para recorrer las columnas
                tablero[i][j] = consultarFicha(idTablero,i,j);
            }
        }
        return tablero;
    }


    public LinkedList<int[]>[] sumarPuntosAux(int[] posicion, int[][] tablero, int direccion, int jugador){ //para mirar el estado del tablero cada vez que se introduzca una ficha
        int x,y, x2, y2, x3, x4, y3, y4;
        x = posicion[0]; // fila
        x2 = posicion[0];
        x3 = posicion[0];
        x4 = posicion[0];
        y = posicion[1]; // columna
        y2 = posicion[1];
        y3 = posicion[1];
        y4 = posicion[1];
        jugador++; // para que coincidan los valores de tablero (1 y 2) y los valores de jugador (0 y 1), si lo incrementamos coinciden
        LinkedList<int[]> solucion = new LinkedList<>();
        LinkedList<int[]> solucion2 = new LinkedList<>();
        LinkedList<int[]> solucion3 = new LinkedList<>();
        LinkedList<int[]> solucion4 = new LinkedList<>();

        switch (direccion) {
            case 0: // horizontal
                // Miramos la parte izquierda del tablero haciendo -1
                while (x > 0 && tablero[x - 1][y] == jugador) { //si las fichas son de un color (de un jugador) seguirá y parará por el 0

                    solucion.add(new int[]{x - 1, y}); // si la poicion equivale a la ficha de un color (del jugador) lo metemos
                    x = x - 1; // para recorrer la parte izquierda
                }

                solucion.add(new int[]{x, y}); // para añadir la posicion que hemos insertado
                // Miramos la parte derecha del tablero haciendo +1
                while (x2 < 5 && tablero[x2 + 1][y] == jugador) { //si las fichas son de un color (de un jugador) seguirá y parará por el 5

                    solucion2.add(new int[]{x2 + 1, y}); // si la poicion equivale a la ficha de un color (del jugador) lo metemos
                    x2 = x2 + 1; // para recorrer la parte derecha
                }
                solucion.addAll(solucion2); // concatenamos las 2 listas
                return new LinkedList[]{solucion,null}; // para pasar las dos soluciones a la diagonal
            // Lo mismo que antes pero en lugar de filas con columnas (y,y2) y en direccion arriba y abajo
            case 1: // vertical
                // Miramos la parte izquierda del tablero haciendo -1
                while (y > 0 && tablero[x][y - 1] == jugador) { //si las fichas son de un color (de un jugador) seguirá y parará por el 0

                    solucion.add(new int[]{x, y - 1}); // si la poicion equivale a la ficha de un color (del jugador) lo metemos
                    y = y - 1; // para recorrer la parte de arriba
                }

                solucion.add(new int[]{x, y}); // para añadir la posicion que hemos insertado
                // Miramos la parte derecha del tablero haciendo +1
                while (y2 < 5 && tablero[x][y2 + 1] == jugador) { //si las fichas son de un color (de un jugador) seguirá y parará por el 5

                    solucion2.add(new int[]{x, y2 + 1}); // si la poicion equivale a la ficha de un color (del jugador) lo metemos
                    y2 = y2 + 1; // para recorrer la parte de abajo
                }
                solucion.addAll(solucion2); // concatenamos las 2 listas
                return new LinkedList[]{solucion,null}; // para pasar las dos soluciones a la diagonal

            // En diagonal como hay 4 direcciones habrán 4 while
            case 2: // diagonal
                while (x > 0 && y > 0 && tablero[x - 1][y - 1] == jugador) { //si las fichas son de un color (de un jugador) seguirá y parará por el 0 (izquierda y arriba)

                    solucion.add(new int[]{x - 1, y - 1}); // si la poicion equivale a la ficha de un color (del jugador) lo metemos
                    x = x - 1; // para recorrer la parte izquierda
                    y = y - 1; // para recorrer la parte de arriba
                }

                // Miramos la parte derecha del tablero haciendo +1
                while (x2 < 5 && y2 > 0 && tablero[x2 + 1][y2 - 1] == jugador) { //si las fichas son de un color (de un jugador) seguirá y parará por el 5

                    solucion2.add(new int[]{x2 + 1, y2 - 1}); // si la poicion equivale a la ficha de un color (del jugador) lo metemos
                    x2 = x2 + 1; // para recorrer la parte derecha
                    y2 = y2 - 1; // para recorrer la parte de arriba
                }

                solucion.add(new int[]{x, y}); // para añadir la posicion que hemos insertado
                solucion2.add(new int[]{x, y}); // para añadir la posicion que hemos insertado

                while (y3 < 5 && x3 > 0 && tablero[x3 - 1][y3 + 1] == jugador) { //si las fichas son de un color (de un jugador) seguirá y parará por el 0

                    solucion3.add(new int[]{x3 - 1, y3 + 1}); // si la poicion equivale a la ficha de un color (del jugador) lo metemos
                    x3 = x3 - 1; // para recorrer la parte izquierda
                    y3 = y3 + 1; // para recorrer la parte de abajo
                }

                // Miramos la parte derecha del tablero haciendo +1
                while (y4 < 5 && x4 < 5 && tablero[x4 + 1][y4 + 1] == jugador) { //si las fichas son de un color (de un jugador) seguirá y parará por el 5

                    solucion4.add(new int[]{x4 + 1, y4 + 1}); // si la poicion equivale a la ficha de un color (del jugador) lo metemos
                    x4 = x4 + 1; // para recorrer la parte derecha
                    y4 = y4 + 1; // para recorrer la parte de abajo
                }
                solucion.addAll(solucion4); // concateno las direcciones opuestas
                solucion2.addAll(solucion3); // concateno las direcciones opuestas

                return new LinkedList[]{solucion,solucion2}; // devuelvo todas las direcciones
        }
        return new LinkedList[0];
    }

    public int sumarPuntos (int idTablero, int jugador, int[] posicion){
        int[][] tablero = estadoTraerTablero(idTablero);
        LinkedList<LinkedList<int[]>> solucion = new LinkedList<>();
        solucion.add(sumarPuntosAux(posicion,tablero,0,jugador)[0]);
        solucion.add(sumarPuntosAux(posicion,tablero,1,jugador)[0]);
        LinkedList<int[]>[] diagonal;
        diagonal = sumarPuntosAux(posicion,tablero,2,jugador);
        solucion.add(diagonal[0]);
        solucion.add(diagonal[1]);

        int puntuacion = 0;
        for (int i = 0; i < 4; i++) {
            if(solucion.get(i).size() == 4){
                puntuacion++; // si conectamos 4 sumamos un punto
            }
            else if(solucion.get(i).size() == 5){
                puntuacion = puntuacion + 2; // si conectamos 5 sumamos dos puntos
            }
            else if(solucion.get(i).size() == 6){
                puntuacion = puntuacion + 3; // si conectamos 6 sumamos tres punto
            }
        }
        return puntuacion;
    }
    public int getIdTablero() {

        return this.idTablero;
    }
}