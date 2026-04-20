package com.untec.biblioteca.controller;

import com.untec.biblioteca.dao.LibroDAO;
import com.untec.biblioteca.dao.PrestamoDAO;
import com.untec.biblioteca.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet del Dashboard principal (página de inicio tras login).
 * URL mapeada: /dashboard
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private LibroDAO    libroDAO;
    private PrestamoDAO prestamoDAO;

    @Override
    public void init() {
        libroDAO    = new LibroDAO();
        prestamoDAO = new PrestamoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Pasar datos de resumen a la vista
            request.setAttribute("totalLibros",    libroDAO.listarTodos().size());
            request.setAttribute("librosDisponibles", libroDAO.listarDisponibles().size());
            request.setAttribute("totalPrestamos", prestamoDAO.listarTodos().size());

            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
