<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Página raíz: redirige automáticamente al login --%>
<% response.sendRedirect(request.getContextPath() + "/login"); %>
