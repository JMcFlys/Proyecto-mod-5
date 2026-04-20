package com.untec.biblioteca.dao;

import com.untec.biblioteca.model.Prestamo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD sobre la entidad Préstamo.
 */
public class PrestamoDAO {

    private Connection conexion;

    public PrestamoDAO() {
        this.conexion = ConexionDB.getInstancia().getConexion();
    }

    // ─── CREATE ─────────────────────────────────────────────────────────────

    /**
     * Registra un nuevo préstamo y marca el libro como no disponible.
     */
    public boolean registrarPrestamo(int libroId, int usuarioId) throws SQLException {
        // Iniciar transacción manual
        conexion.setAutoCommit(false);
        try {
            // 1. Insertar préstamo
            String sqlPrestamo = "INSERT INTO prestamos (libro_id, usuario_id, fecha_prestamo, estado) VALUES (?, ?, CURRENT_DATE, 'ACTIVO')";
            try (PreparedStatement ps = conexion.prepareStatement(sqlPrestamo)) {
                ps.setInt(1, libroId);
                ps.setInt(2, usuarioId);
                ps.executeUpdate();
            }

            // 2. Marcar libro como no disponible
            String sqlLibro = "UPDATE libros SET disponible = FALSE WHERE id = ?";
            try (PreparedStatement ps = conexion.prepareStatement(sqlLibro)) {
                ps.setInt(1, libroId);
                ps.executeUpdate();
            }

            conexion.commit();
            return true;
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    // ─── READ ────────────────────────────────────────────────────────────────

    /**
     * Lista todos los préstamos con datos del libro y usuario (JOIN).
     */
    public List<Prestamo> listarTodos() throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = """
            SELECT p.*, l.titulo AS titulo_libro, u.nombre AS nombre_usuario
            FROM prestamos p
            JOIN libros   l ON p.libro_id   = l.id
            JOIN usuarios u ON p.usuario_id = u.id
            ORDER BY p.fecha_prestamo DESC
        """;
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) prestamos.add(mapear(rs));
        }
        return prestamos;
    }

    /**
     * Lista los préstamos activos de un usuario específico.
     */
    public List<Prestamo> listarPorUsuario(int usuarioId) throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = """
            SELECT p.*, l.titulo AS titulo_libro, u.nombre AS nombre_usuario
            FROM prestamos p
            JOIN libros   l ON p.libro_id   = l.id
            JOIN usuarios u ON p.usuario_id = u.id
            WHERE p.usuario_id = ? AND p.estado = 'ACTIVO'
            ORDER BY p.fecha_prestamo DESC
        """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) prestamos.add(mapear(rs));
            }
        }
        return prestamos;
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────

    /**
     * Registra la devolución de un libro: actualiza el préstamo y libera el libro.
     */
    public boolean registrarDevolucion(int prestamoId) throws SQLException {
        conexion.setAutoCommit(false);
        try {
            // 1. Obtener libro_id del préstamo
            int libroId = -1;
            String sqlGet = "SELECT libro_id FROM prestamos WHERE id = ?";
            try (PreparedStatement ps = conexion.prepareStatement(sqlGet)) {
                ps.setInt(1, prestamoId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) libroId = rs.getInt("libro_id");
                }
            }
            if (libroId == -1) return false;

            // 2. Marcar préstamo como DEVUELTO
            String sqlPrestamo = "UPDATE prestamos SET estado='DEVUELTO', fecha_devolucion=CURRENT_DATE WHERE id=?";
            try (PreparedStatement ps = conexion.prepareStatement(sqlPrestamo)) {
                ps.setInt(1, prestamoId);
                ps.executeUpdate();
            }

            // 3. Liberar el libro
            String sqlLibro = "UPDATE libros SET disponible = TRUE WHERE id = ?";
            try (PreparedStatement ps = conexion.prepareStatement(sqlLibro)) {
                ps.setInt(1, libroId);
                ps.executeUpdate();
            }

            conexion.commit();
            return true;
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    // ─── MAPEO ───────────────────────────────────────────────────────────────

    private Prestamo mapear(ResultSet rs) throws SQLException {
        Prestamo p = new Prestamo(
            rs.getInt("id"),
            rs.getInt("libro_id"),
            rs.getInt("usuario_id"),
            rs.getDate("fecha_prestamo"),
            rs.getDate("fecha_devolucion"),
            rs.getString("estado")
        );
        p.setTituloLibro(rs.getString("titulo_libro"));
        p.setNombreUsuario(rs.getString("nombre_usuario"));
        return p;
    }
}
