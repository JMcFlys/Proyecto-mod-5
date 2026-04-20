<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Libros – Biblioteca Digital UNTEC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%-- Navbar --%>
<nav class="navbar">
    <div class="navbar-brand">📚 Biblioteca UNTEC</div>
    <ul class="navbar-links">
        <li><a href="${pageContext.request.contextPath}/dashboard">Inicio</a></li>
        <li><a href="${pageContext.request.contextPath}/libros?accion=listar" class="active">Libros</a></li>
        <li><a href="${pageContext.request.contextPath}/prestamos?accion=listar">Préstamos</a></li>
        <li><a href="${pageContext.request.contextPath}/logout">Salir</a></li>
    </ul>
    <span class="navbar-user">👤 <c:out value="${sessionScope.usuarioLogueado.nombre}" /></span>
</nav>

<main class="main-content">
    <div class="page-header">
        <h2>📖 Catálogo de Libros</h2>
        <c:if test="${sessionScope.usuarioLogueado.rol == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/libros?accion=nuevo" class="btn btn-primary">+ Agregar libro</a>
        </c:if>
    </div>

    <%-- Mensajes de confirmación --%>
    <c:if test="${param.msg == 'creado'}">
        <div class="alert alert-success">✅ Libro agregado correctamente.</div>
    </c:if>
    <c:if test="${param.msg == 'actualizado'}">
        <div class="alert alert-success">✅ Libro actualizado correctamente.</div>
    </c:if>
    <c:if test="${param.msg == 'eliminado'}">
        <div class="alert alert-info">🗑️ Libro eliminado.</div>
    </c:if>

    <%-- Tabla de libros --%>
    <c:choose>
        <c:when test="${empty libros}">
            <p class="empty-msg">No hay libros registrados aún.</p>
        </c:when>
        <c:otherwise>
            <table class="table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Título</th>
                        <th>Autor</th>
                        <th>ISBN</th>
                        <th>Año</th>
                        <th>Estado</th>
                        <c:if test="${sessionScope.usuarioLogueado.rol == 'ADMIN'}">
                            <th>Acciones</th>
                        </c:if>
                    </tr>
                </thead>
                <tbody>
                    <%-- c:forEach itera la lista de libros recibida desde el Servlet --%>
                    <c:forEach var="libro" items="${libros}" varStatus="status">
                        <tr>
                            <td><c:out value="${status.count}" /></td>
                            <td><c:out value="${libro.titulo}" /></td>
                            <td><c:out value="${libro.autor}" /></td>
                            <td><c:out value="${libro.isbn}" /></td>
                            <td><c:out value="${libro.anio}" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${libro.disponible}">
                                        <span class="badge badge-green">Disponible</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-red">Prestado</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <c:if test="${sessionScope.usuarioLogueado.rol == 'ADMIN'}">
                                <td>
                                    <a href="${pageContext.request.contextPath}/libros?accion=editar&id=${libro.id}"
                                       class="btn btn-sm btn-outline">Editar</a>
                                    <a href="${pageContext.request.contextPath}/libros?accion=eliminar&id=${libro.id}"
                                       class="btn btn-sm btn-danger"
                                       onclick="return confirm('¿Eliminar este libro?')">Eliminar</a>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</main>

</body>
</html>
