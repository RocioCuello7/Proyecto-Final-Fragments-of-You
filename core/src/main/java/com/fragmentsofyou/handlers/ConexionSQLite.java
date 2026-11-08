package com.fragmentsofyou.handlers;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class ConexionSQLite {

    private Connection conexion;

    public Connection conectar() throws SQLException {

        File archivo = new File("../BDDFOY.db");

        System.out.println("Ruta de la base de datos: " + archivo.getAbsolutePath());

        if (!archivo.exists()) {
            throw new SQLException("No se encontró FragmentsOfYou.db en esa ubicación.");
        }

        String url = "jdbc:sqlite:" + archivo.getAbsolutePath();

        conexion = DriverManager.getConnection(url);

        try (Statement sentencia = conexion.createStatement()) {
            sentencia.execute("PRAGMA foreign_keys = ON");
        }

        System.out.println("¡Conexión con SQLite establecida!");

        return conexion;
    }

    public void consultarJugadores() throws SQLException {

        String sql = "SELECT * FROM jugador";

        try (PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {

                int id = resultado.getInt("id_jugador");
                String nombre = resultado.getString("nombre");

                System.out.println("ID: " + id + " | Nombre: " + nombre);
            }
        }
    }

    public ArrayList<String> consultarRanking() throws SQLException {

        ArrayList<String> ranking = new ArrayList<>();

        String sql = "SELECT nombre, mejor_tiempo FROM vista_ranking";

        try (PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {

                String nombre = resultado.getString("nombre");
                int tiempo = resultado.getInt("mejor_tiempo");

                int minutos = tiempo / 60;
                int segundos = tiempo % 60;

                String registro = nombre + " - " +
                    String.format("%02d:%02d", minutos, segundos);

                ranking.add(registro);
            }
        }

        return ranking;
    }

    public void guardarPartida(String nombre, int tiempoSegundos)
        throws SQLException {

        nombre = nombre.trim();

        if (nombre.isEmpty() || tiempoSegundos <= 0) {
            throw new SQLException("Nombre o tiempo inválido.");
        }

        conexion.setAutoCommit(false);

        try {
            int idJugador;

            String buscar =
                "SELECT id_jugador FROM jugador WHERE nombre = ? LIMIT 1";

            try (PreparedStatement consulta = conexion.prepareStatement(buscar)) {

                consulta.setString(1, nombre);

                try (ResultSet resultado = consulta.executeQuery()) {

                    if (resultado.next()) {
                        idJugador = resultado.getInt("id_jugador");

                    } else {
                        String insertarJugador =
                            "INSERT INTO jugador (nombre) VALUES (?)";

                        try (PreparedStatement insertar =
                                 conexion.prepareStatement(insertarJugador)) {

                            insertar.setString(1, nombre);
                            insertar.executeUpdate();
                        }

                        try (Statement sentencia = conexion.createStatement();
                             ResultSet resultadoId =
                                 sentencia.executeQuery("SELECT last_insert_rowid()")) {

                            resultadoId.next();
                            idJugador = resultadoId.getInt(1);
                        }
                    }
                }
            }

            String insertarPartida =
                "INSERT INTO partida (id_jugador, tiempo_segundos) VALUES (?, ?)";

            try (PreparedStatement insertar =
                     conexion.prepareStatement(insertarPartida)) {

                insertar.setInt(1, idJugador);
                insertar.setInt(2, tiempoSegundos);
                insertar.executeUpdate();
            }

            conexion.commit();

            System.out.println("¡Partida guardada correctamente!");

        } catch (SQLException e) {

            conexion.rollback();
            throw e;

        } finally {
            conexion.setAutoCommit(true);
        }
    }


    public void cerrarConexion() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
            System.out.println("Conexión con SQLite cerrada.");
        }
    }
}
