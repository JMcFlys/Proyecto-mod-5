package com.untec.biblioteca.controller;

import com.untec.biblioteca.dao.LibroDAO;
import com.untec.biblioteca.model.Libro;
import com.untec.biblioteca.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet controlador para la gestión de Libros.
 *
 * URL mapeada: /libros
 * Parámetro "accion" determina la operación:
 *   - listar   (GET)    → lista todos los libros
 *   - nuevo    (GET)    → formulario de nuevo libro
 *   - guardar  (POST)   → inserta un libro
 *   - editar   (GET)    → formulario de edición
 *   - actualizar (POST) → actualiza un libro
 *   - eliminar (GET)    → elimina un libro
 */
@WebServlet("/libros")
public class LibroServlet extends HttpServlet {

    private LibroDAO libroDAO;

    @Override
    public void init() {
        libroDAO = new LibroDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar sesión activa
        if (!sesionValida(request, response)) return;

        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        try {
            switch (accion) {
                case "listar"  -> listar(request, response);
                case "nuevo"   -> request.getRequestDispatcher("/WEB-INF/views/libro_form.jsp")
                                         .forward(request, response);
                case "editar"  -> editar(request, response);
                case "eliminar"-> eliminar(request, response);
                default        -> listar(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en base de datos: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/libros.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!sesionValida(request, response)) return;

        String accion = request.getParameter("accion");
        if (accion == null) accion = "guardar";

        try {
            switch (accion) {
                case "guardar"    -> guardar(request, response);
                case "actualizar" -> actualizar(request, response);
                default           -> response.sendRedirect(request.getContextPath() + "/libros");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al guardar: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/libro_form.jsp").forward(request, response);
        }
    }

    // ─── Acciones privadas ───────────────────────────────────────────────────

    private void listar(HttpServletRequest req, HttpServletResponse res)
            throws SQLException, ServletException, IOException {
        List<Libro> libros = libroDAO.listarTodos();
        req.setAttribute("libros", libros);
        req.getRequestDispatcher("/WEB-INF/views/libros.jsp").forward(req, res);
    }

    private void editar(HttpServletRequest req, HttpServletResponse res)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Libro libro = libroDAO.buscarPorId(id);
        req.setAttribute("libro", libro);
        req.getRequestDispatcher("/WEB-INF/views/libro_form.jsp").forward(req, res);
    }

    private void guardar(HttpServletRequest req, HttpServletResponse res)
            throws SQLException, IOException {
        Libro libro = construirLibro(req);
        libroDAO.insertar(libro);
        res.sendRedirect(req.getContextPath() + "/libros?accion=listar&msg=creado");
    }

    private void actualizar(HttpServletRequest req, HttpServletResponse res)
            throws SQLException, IOException {
        Libro libro = construirLibro(req);
        libro.setId(Integer.parseInt(req.getParameter("id")));
        libroDAO.actualizar(libro);
        res.sendRedirect(req.getContextPath() + "/libros?accion=listar&msg=actualizado");
    }

    private void eliminar(HttpServletRequest req, HttpServletResponse res)
            throws SQLException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        libroDAO.eliminar(id);
        res.sendRedirect(req.getContextPath() + "/libros?accion=listar&msg=eliminado");
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    /**
     * Construye un objeto Libro a partir de los parámetros del formulario.
     */
    private Libro construirLibro(HttpServletRequest req) {
        Libro libro = new Libro();
        libro.setTitulo(req.getParameter("titulo"));
        libro.setAutor(req.getParameter("autor"));
        libro.setIsbn(req.getParameter("isbn"));
        libro.setAnio(Integer.parseInt(req.getParameter("anio")));
        libro.setDisponible(true);
        return libro;
    }

    /**
     * Verifica que exista una sesión activa. Si no, redirige al login.
     */
    private boolean sesionValida(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        return true;
    }
}
