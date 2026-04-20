package com.untec.biblioteca.controller;

import com.untec.biblioteca.dao.LibroDAO;
import com.untec.biblioteca.dao.PrestamoDAO;
import com.untec.biblioteca.model.Libro;
import com.untec.biblioteca.model.Prestamo;
import com.untec.biblioteca.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet controlador para la gestión de Préstamos.
 *
 * URL mapeada: /prestamos
 * Parámetro "accion":
 *   - listar   (GET)  → lista todos los préstamos
 *   - nuevo    (GET)  → formulario para nuevo préstamo
 *   - prestar  (POST) → registra un préstamo
 *   - devolver (GET)  → registra una devolución
 */
@WebServlet("/prestamos")
public class PrestamoServlet extends HttpServlet {

    private PrestamoDAO prestamoDAO;
    private LibroDAO    libroDAO;

    @Override
    public void init() {
        prestamoDAO = new PrestamoDAO();
        libroDAO    = new LibroDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!sesionValida(request, response)) return;

        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        try {
            switch (accion) {
                case "listar"   -> listar(request, response);
                case "nuevo"    -> mostrarFormulario(request, response);
                case "devolver" -> devolver(request, response);
                default         -> listar(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en base de datos: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/prestamos.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!sesionValida(request, response)) return;

        try {
            prestar(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al registrar préstamo: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/prestamo_form.jsp").forward(request, response);
        }
    }

    // ─── Acciones privadas ───────────────────────────────────────────────────

    private void listar(HttpServletRequest req, HttpServletResponse res)
            throws SQLException, ServletException, IOException {
        List<Prestamo> prestamos = prestamoDAO.listarTodos();
        req.setAttribute("prestamos", prestamos);
        req.getRequestDispatcher("/WEB-INF/views/prestamos.jsp").forward(req, res);
    }

    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse res)
            throws SQLException, ServletException, IOException {
        List<Libro> librosDisponibles = libroDAO.listarDisponibles();
        req.setAttribute("librosDisponibles", librosDisponibles);
        req.getRequestDispatcher("/WEB-INF/views/prestamo_form.jsp").forward(req, res);
    }

    private void prestar(HttpServletRequest req, HttpServletResponse res)
            throws SQLException, IOException {
        int libroId   = Integer.parseInt(req.getParameter("libroId"));
        HttpSession session = req.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        prestamoDAO.registrarPrestamo(libroId, usuario.getId());
        res.sendRedirect(req.getContextPath() + "/prestamos?accion=listar&msg=prestado");
    }

    private void devolver(HttpServletRequest req, HttpServletResponse res)
            throws SQLException, IOException {
        int prestamoId = Integer.parseInt(req.getParameter("id"));
        prestamoDAO.registrarDevolucion(prestamoId);
        res.sendRedirect(req.getContextPath() + "/prestamos?accion=listar&msg=devuelto");
    }

    // ─── Helper ──────────────────────────────────────────────────────────────

    private boolean sesionValida(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        return true;
    }
}
