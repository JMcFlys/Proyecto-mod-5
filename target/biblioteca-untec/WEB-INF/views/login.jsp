<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login – Biblioteca Digital UNTEC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="login-page">

<div class="login-container">
    <div class="login-card">

        <!-- Logo / Encabezado -->
        <div class="login-header">
            <span class="login-icon">📚</span>
            <h1>Biblioteca Digital</h1>
            <p>UNTEC</p>
        </div>

        <!-- Mensaje de logout -->
        <c:if test="${param.msg == 'logout'}">
            <div class="alert alert-info">Sesión cerrada correctamente.</div>
        </c:if>

        <!-- Mensaje de error -->
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                <c:out value="${error}" />
            </div>
        </c:if>

        <!-- Formulario de login -->
        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="email">Correo electrónico</label>
                <input type="email" id="email" name="email"
                       placeholder="usuario@untec.cl"
                       value="${param.email}" required autofocus>
            </div>

            <div class="form-group">
                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password"
                       placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn btn-primary btn-full">
                Iniciar sesión
            </button>
        </form>

        <p class="login-hint">
            <small>Demo: <strong>admin@untec.cl</strong> / <strong>admin123</strong></small>
        </p>
    </div>
</div>

</body>
</html>
