package com.untec.biblioteca.dao;

import com.untec.biblioteca.model.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD sobre la entidad Libro.
 */
public class LibroDAO {

    private Connection conexion;

    public LibroDAO() {
        this.conexion = ConexionDB.getInstancia().getConexion();
    }

    // ─── CREATE ─────────────────────────────────────────────────────────────

    /**
     * Inserta un nuevo libro en la base de datos.
     */
    public boolean insertar(Libro libro) throws SQLException {
        String sql = "INSERT INTO libros (titulo, autor, isbn, anio, disponible) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setInt(4, libro.getAnio());
            ps.setBoolean(5, libro.isDisponible());
            return ps.executeUpdate() > 0;
        }
    }

    // ─── READ ────────────────────────────────────────────────────────────────

    /**
     * Retorna todos los libros de la biblioteca.
     */
    public List<Libro> listarTodos() throws SQLException {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros ORDER BY titulo";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                libros.add(mapear(rs));
            }
        }
        return libros;
    }

    /**
     * Retorna solo los libros disponibles para préstamo.
     */
    public List<Libro> listarDisponibles() throws SQLException {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE disponible = TRUE ORDER BY titulo";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                libros.add(mapear(rs));
            }
        }
        return libros;
    }

    /**
     * Busca un libro por su ID.
     */
    public Libro buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM libros WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    /**
     * Busca libros por título (búsqueda parcial).
     */
    public List<Libro> buscarPorTitulo(String titulo) throws SQLException {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE LOWER(titulo) LIKE LOWER(?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, "%" + titulo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) libros.add(mapear(rs));
            }
        }
        return libros;
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────

    /**
     * Actualiza los datos de un libro existente.
     */
    public boolean actualizar(Libro libro) throws SQLException {
        String sql = "UPDATE libros SET titulo=?, autor=?, isbn=?, anio=?, disponible=? WHERE id=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setInt(4, libro.getAnio());
            ps.setBoolean(5, libro.isDisponible());
            ps.setInt(6, libro.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cambia la disponibilidad de un libro (útil al prestar/devolver).
     */
    public boolean cambiarDisponibilidad(int libroId, boolean disponible) throws SQLException {
        String sql = "UPDATE libros SET disponible=? WHERE id=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setBoolean(1, disponible);
            ps.setInt(2, libroId);
            return ps.executeUpdate() > 0;
        }
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────

    /**
     * Elimina un libro por su ID.
     */
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM libros WHERE id=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ─── MAPEO ───────────────────────────────────────────────────────────────

    /**
     * Convierte un ResultSet en un objeto Libro.
     */
    private Libro mapear(ResultSet rs) throws SQLException {
        return new Libro(
            rs.getInt("id"),
            rs.getString("titulo"),
            rs.getString("autor"),
            rs.getString("isbn"),
            rs.getInt("anio"),
            rs.getBoolean("disponible")
        );
    }
}
