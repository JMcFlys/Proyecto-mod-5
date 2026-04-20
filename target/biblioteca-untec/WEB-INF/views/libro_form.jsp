<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>${empty libro ? 'Nuevo Libro' : 'Editar Libro'} – Biblioteca UNTEC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

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
    <div class="form-card">
        <%-- El título cambia dependiendo si es edición o creación --%>
        <h2>
            <c:choose>
                <c:when test="${not empty libro}">✏️ Editar libro</c:when>
                <c:otherwise>➕ Nuevo libro</c:otherwise>
            </c:choose>
        </h2>

        <c:if test="${not empty error}">
            <div class="alert alert-error"><c:out value="${error}" /></div>
        </c:if>

        <%-- El action y el campo "accion" cambian si es editar o crear --%>
        <form action="${pageContext.request.contextPath}/libros" method="post">
            <input type="hidden" name="accion" value="${not empty libro ? 'actualizar' : 'guardar'}">
            <c:if test="${not empty libro}">
                <input type="hidden" name="id" value="${libro.id}">
            </c:if>

            <div class="form-group">
                <label for="titulo">Título *</label>
                <input type="text" id="titulo" name="titulo"
                       value="${libro.titulo}" required placeholder="Ej: Clean Code">
            </div>

            <div class="form-group">
                <label for="autor">Autor *</label>
                <input type="text" id="autor" name="autor"
                       value="${libro.autor}" required placeholder="Ej: Robert C. Martin">
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="isbn">ISBN</label>
                    <input type="text" id="isbn" name="isbn"
                           value="${libro.isbn}" placeholder="978-...">
                </div>
                <div class="form-group">
                    <label for="anio">Año</label>
                    <input type="number" id="anio" name="anio"
                           value="${libro.anio}" min="1900" max="2099" placeholder="2024">
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    <c:choose>
                        <c:when test="${not empty libro}">Guardar cambios</c:when>
                        <c:otherwise>Agregar libro</c:otherwise>
                    </c:choose>
                </button>
                <a href="${pageContext.request.contextPath}/libros?accion=listar" class="btn btn-outline">Cancelar</a>
            </div>
        </form>
    </div>
</main>

</body>
</html>
