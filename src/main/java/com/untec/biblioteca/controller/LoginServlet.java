package com.untec.biblioteca.controller;

import com.untec.biblioteca.dao.UsuarioDAO;
import com.untec.biblioteca.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet controlador del Login.
 * MVC: actúa como Controller, redirige a vistas JSP (View) y usa DAO (Model).
 *
 * URL mapeada: /login
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * GET /login  → Muestra el formulario de login.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Si ya hay sesión activa, redirigir al dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    /**
     * POST /login → Procesa las credenciales del formulario.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email    = request.getParameter("email");
        String password = request.getParameter("password");

        // Validación básica de campos vacíos
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("error", "Por favor ingresa email y contraseña.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        try {
            Usuario usuario = usuarioDAO.autenticar(email.trim(), password.trim());

            if (usuario != null) {
                // Crear sesión y guardar usuario autenticado
                HttpSession session = request.getSession(true);
                session.setAttribute("usuarioLogueado", usuario);
                session.setMaxInactiveInterval(30 * 60); // 30 minutos

                // Redirigir al dashboard
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                // Credenciales incorrectas
                request.setAttribute("error", "Email o contraseña incorrectos.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            request.setAttribute("error", "Error de servidor. Intenta nuevamente.");
            e.printStackTrace();
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
