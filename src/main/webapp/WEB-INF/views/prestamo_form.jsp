<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Nuevo Préstamo – Biblioteca UNTEC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav class="navbar">
    <div class="navbar-brand">📚 Biblioteca UNTEC</div>
    <ul class="navbar-links">
        <li><a href="${pageContext.request.contextPath}/dashboard">Inicio</a></li>
        <li><a href="${pageContext.request.contextPath}/libros?accion=listar">Libros</a></li>
        <li><a href="${pageContext.request.contextPath}/prestamos?accion=listar" class="active">Préstamos</a></li>
        <li><a href="${pageContext.request.contextPath}/logout">Salir</a></li>
    </ul>
    <span class="navbar-user">👤 <c:out value="${sessionScope.usuarioLogueado.nombre}" /></span>
</nav>

<main class="main-content">
    <div class="form-card">
        <h2>🔖 Registrar nuevo préstamo</h2>

        <c:if test="${not empty error}">
            <div class="alert alert-error"><c:out value="${error}" /></div>
        </c:if>

        <c:choose>
            <c:when test="${empty librosDisponibles}">
                <div class="alert alert-info">
                    No hay libros disponibles para préstamo en este momento.
                </div>
                <a href="${pageContext.request.contextPath}/prestamos?accion=listar" class="btn btn-outline">Volver</a>
            </c:when>
            <c:otherwise>
                <form action="${pageContext.request.contextPath}/prestamos" method="post">
                    <input type="hidden" name="accion" value="prestar">

                    <div class="form-group">
                        <label for="libroId">Seleccionar libro *</label>
                        <select id="libroId" name="libroId" required>
                            <option value="">-- Elige un libro disponible --</option>
                            <%-- c:forEach itera los libros disponibles --%>
                            <c:forEach var="libro" items="${librosDisponibles}">
                                <option value="${libro.id}">
                                    <c:out value="${libro.titulo}" /> – <c:out value="${libro.autor}" />
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <p class="form-note">
                        El préstamo se registrará a nombre de:
                        <strong><c:out value="${sessionScope.usuarioLogueado.nombre}" /></strong>
                    </p>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Registrar préstamo</button>
                        <a href="${pageContext.request.contextPath}/prestamos?accion=listar" class="btn btn-outline">Cancelar</a>
                    </div>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</main>

</body>
</html>
