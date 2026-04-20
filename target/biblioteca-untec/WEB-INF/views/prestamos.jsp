<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Préstamos – Biblioteca Digital UNTEC</title>
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
    <div class="page-header">
        <h2>🔖 Gestión de Préstamos</h2>
        <a href="${pageContext.request.contextPath}/prestamos?accion=nuevo" class="btn btn-primary">+ Nuevo préstamo</a>
    </div>

    <c:if test="${param.msg == 'prestado'}">
        <div class="alert alert-success">✅ Préstamo registrado correctamente.</div>
    </c:if>
    <c:if test="${param.msg == 'devuelto'}">
        <div class="alert alert-info">📦 Devolución registrada correctamente.</div>
    </c:if>

    <c:choose>
        <c:when test="${empty prestamos}">
            <p class="empty-msg">No hay préstamos registrados aún.</p>
        </c:when>
        <c:otherwise>
            <table class="table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Libro</th>
                        <th>Usuario</th>
                        <th>Fecha préstamo</th>
                        <th>Fecha devolución</th>
                        <th>Estado</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${prestamos}" varStatus="st">
                        <tr>
                            <td><c:out value="${st.count}" /></td>
                            <td><c:out value="${p.tituloLibro}" /></td>
                            <td><c:out value="${p.nombreUsuario}" /></td>
                            <td>
                                <fmt:formatDate value="${p.fechaPrestamo}" pattern="dd/MM/yyyy" />
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty p.fechaDevolucion}">
                                        <fmt:formatDate value="${p.fechaDevolucion}" pattern="dd/MM/yyyy" />
                                    </c:when>
                                    <c:otherwise>–</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${p.estado == 'ACTIVO'}">
                                        <span class="badge badge-orange">Activo</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-green">Devuelto</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:if test="${p.estado == 'ACTIVO'}">
                                    <a href="${pageContext.request.contextPath}/prestamos?accion=devolver&id=${p.id}"
                                       class="btn btn-sm btn-primary"
                                       onclick="return confirm('¿Registrar devolución?')">Devolver</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</main>

</body>
</html>
