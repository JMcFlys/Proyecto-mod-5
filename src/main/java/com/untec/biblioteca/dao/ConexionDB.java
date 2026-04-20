package com.untec.biblioteca.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase Singleton para gestionar la conexión a la base de datos.
 * Usa H2 en modo embebido (educativo). Para MySQL, cambiar la URL y driver.
 */
public class ConexionDB {

    // ─── Configuración H2 (modo embebido) ───────────────────────────────────
    private static final String URL      = "jdbc:h2:~/biblioteca_untec;AUTO_SERVER=TRUE";
    private static final String USUARIO  = "sa";
    private static final String PASSWORD = "";
    private static final String DRIVER   = "org.h2.Driver";

    // ─── Configuración MySQL (comentar H2 y descomentar esto) ───────────────
    // private static final String URL     = "jdbc:mysql://localhost:3306/biblioteca_untec";
    // private static final String USUARIO = "root";
    // private static final String PASSWORD = "tu_password";
    // private static final String DRIVER  = "com.mysql.cj.jdbc.Driver";

    // Instancia única (Singleton)
    private static ConexionDB instancia;
    private Connection conexion;

    // Constructor privado
    private ConexionDB() {
        try {
            Class.forName(DRIVER);
            this.conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("✅ Conexión a base de datos establecida.");
            inicializarTablas();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ Driver no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error conectando a BD: " + e.getMessage());
        }
    }

    /**
     * Retorna la única instancia de ConexionDB (Singleton).
     */
    public static synchronized ConexionDB getInstancia() {
        if (instancia == null || estaConexionCerrada()) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    /**
     * Retorna el objeto Connection listo para usarse en los DAO.
     */
    public Connection getConexion() {
        return conexion;
    }

    /**
     * Verifica si la conexión está cerrada o es nula.
     */
    private static boolean estaConexionCerrada() {
        try {
            return instancia.conexion == null || instancia.conexion.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    /**
     * Crea las tablas si no existen y carga datos de ejemplo.
     */
    private void inicializarTablas() throws SQLException {
        Statement stmt = conexion.createStatement();

        // Tabla usuarios
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS usuarios (
                id       INT AUTO_INCREMENT PRIMARY KEY,
                nombre   VARCHAR(100) NOT NULL,
                email    VARCHAR(100) UNIQUE NOT NULL,
                password VARCHAR(100) NOT NULL,
                rol      VARCHAR(20)  DEFAULT 'ESTUDIANTE'
            )
        """);

        // Tabla libros
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS libros (
                id          INT AUTO_INCREMENT PRIMARY KEY,
                titulo      VARCHAR(200) NOT NULL,
                autor       VARCHAR(100) NOT NULL,
                isbn        VARCHAR(20)  UNIQUE,
                anio        INT,
                disponible  BOOLEAN DEFAULT TRUE
            )
        """);

        // Tabla prestamos
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS prestamos (
                id               INT AUTO_INCREMENT PRIMARY KEY,
                libro_id         INT NOT NULL,
                usuario_id       INT NOT NULL,
                fecha_prestamo   DATE NOT NULL,
                fecha_devolucion DATE,
                estado           VARCHAR(20) DEFAULT 'ACTIVO',
                FOREIGN KEY (libro_id)   REFERENCES libros(id),
                FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
            )
        """);

        // Datos de ejemplo (solo si la tabla está vacía)
        stmt.execute("""
            MERGE INTO usuarios (nombre, email, password, rol)
            KEY(email)
            VALUES ('Administrador', 'admin@untec.cl', 'admin123', 'ADMIN')
        """);

        stmt.execute("""
            MERGE INTO usuarios (nombre, email, password, rol)
            KEY(email)
            VALUES ('Juan Pérez', 'juan@untec.cl', '1234', 'ESTUDIANTE')
        """);

        stmt.execute("""
            MERGE INTO libros (titulo, autor, isbn, anio, disponible)
            KEY(isbn)
            VALUES ('Clean Code', 'Robert C. Martin', '978-0132350884', 2008, TRUE)
        """);

        stmt.execute("""
            MERGE INTO libros (titulo, autor, isbn, anio, disponible)
            KEY(isbn)
            VALUES ('Head First Java', 'Kathy Sierra', '978-0596009205', 2005, TRUE)
        """);

        stmt.execute("""
            MERGE INTO libros (titulo, autor, isbn, anio, disponible)
            KEY(isbn)
            VALUES ('Design Patterns', 'Gang of Four', '978-0201633610', 1994, TRUE)
        """);

        stmt.close();
        System.out.println("✅ Tablas inicializadas correctamente.");
    }
}
