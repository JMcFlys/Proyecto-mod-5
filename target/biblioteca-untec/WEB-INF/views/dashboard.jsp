<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Dashboard – Biblioteca Digital UNTEC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%-- ── Navbar ──────────────────────────────────────────────────────────── --%>
<nav class="navbar">
    <div class="navbar-brand">📚 Biblioteca UNTEC</div>
    <ul class="navbar-links">
        <li><a href="${pageContext.request.contextPath}/dashboard" class="active">Inicio</a></li>
        <li><a href="${pageContext.request.contextPath}/libros?accion=listar">Libros</a></li>
        <li><a href="${pageContext.request.contextPath}/prestamos?accion=listar">Préstamos</a></li>
        <li><a href="${pageContext.request.contextPath}/logout">Salir</a></li>
    </ul>
    <span class="navbar-user">
        👤 <c:out value="${sessionScope.usuarioLogueado.nombre}" />
        (<c:out value="${sessionScope.usuarioLogueado.rol}" />)
    </span>
</nav>

<%-- ── Contenido principal ───────────────────────────────────────────── --%>
<main class="main-content">
    <h2>Bienvenido/a, <c:out value="${sessionScope.usuarioLogueado.nombre}" /> 👋</h2>
    <p class="subtitle">Panel de control de la Biblioteca Digital UNTEC</p>

    <%-- Tarjetas de resumen --%>
    <div class="card-grid">

        <div class="card card-blue">
            <div class="card-icon">📖</div>
            <div class="card-info">
                <span class="card-number">${totalLibros}</span>
                <span class="card-label">Libros en catálogo</span>
            </div>
        </div>

        <div class="card card-green">
            <div class="card-icon">✅</div>
            <div class="card-info">
                <span class="card-number">${librosDisponibles}</span>
                <span class="card-label">Libros disponibles</span>
            </div>
        </div>

        <div class="card card-orange">
            <div class="card-icon">🔖</div>
            <div class="card-info">
                <span class="card-number">${totalPrestamos}</span>
                <span class="card-label">Préstamos registrados</span>
            </div>
        </div>

    </div>

    <%-- Accesos rápidos --%>
    <div class="quick-actions">
        <h3>Acciones rápidas</h3>
        <a href="${pageContext.request.contextPath}/libros?accion=listar" class="btn btn-primary">Ver catálogo</a>
        <a href="${pageContext.request.contextPath}/prestamos?accion=nuevo"  class="btn btn-secondary">Registrar préstamo</a>
        <c:if test="${sessionScope.usuarioLogueado.rol == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/libros?accion=nuevo" class="btn btn-outline">Agregar libro</a>
        </c:if>
    </div>
</main>

</body>
</html>
