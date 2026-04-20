# 📚 Biblioteca Digital UNTEC
### Evaluación Módulo 5 – Desarrollo de aplicaciones web dinámicas en Java
### 🛠️ Configurado para **IntelliJ IDEA**

---

## 📌 Descripción

Sistema de gestión web para la Biblioteca Digital de la Universidad UNTEC.
Permite gestionar libros, usuarios y préstamos a través de una aplicación Java EE
desplegada en Apache Tomcat.

---

## 🏗️ Arquitectura MVC

```
com.untec.biblioteca/
├── model/          ← Entidades (Libro, Usuario, Prestamo)
├── dao/            ← Acceso a datos con JDBC (patrón DAO + Singleton)
└── controller/     ← Servlets (LoginServlet, LibroServlet, etc.)

webapp/
├── WEB-INF/
│   ├── web.xml
│   └── views/      ← Vistas JSP con JSTL
└── css/style.css
```

**Flujo MVC:**
```
Navegador → Servlet (Controller) → DAO (Model) → BD
                                ↓
                          JSP (View) ← datos
```

---

## ⚙️ Requisitos previos

| Herramienta   | Versión recomendada                         |
|---------------|---------------------------------------------|
| Java JDK      | 17+                                         |
| IntelliJ IDEA | Community o Ultimate (ambas sirven)         |
| Apache Tomcat | **10.x** (no usar Tomcat 9)                |
| Maven         | Incluido en IntelliJ (no necesitas instalarlo) |
| Base de datos | H2 embebida (incluida) o MySQL 8            |

> **Descarga Tomcat 10:** https://tomcat.apache.org/download-10.cgi
> Descarga el ZIP y descomprímelo en una ruta sin espacios (ej: `C:\tomcat10`).

---

## 🚀 Instalación paso a paso en IntelliJ IDEA

### Paso 1 – Abrir el proyecto

1. Abre IntelliJ IDEA.
2. Selecciona **Open** (o `File → Open`).
3. Navega hasta la carpeta `BibliotecaDigitalUNTEC_IJ/` y haz clic en **OK**.
4. IntelliJ detecta el `pom.xml` → clic en **Open as Project**.
5. Espera que Maven descargue las dependencias (barra inferior derecha).

---

### Paso 2 – Configurar el JDK

1. `File → Project Structure → Project`
2. En **SDK** selecciona JDK 17 (o clic en **Add SDK → JDK** si no aparece).
3. En **Language Level** elige **17**.
4. Clic **Apply → OK**.

---

### Paso 3 – Agregar Apache Tomcat a IntelliJ

1. Ve a `File → Settings → Build, Execution, Deployment → Application Servers`
   (en Mac: `IntelliJ IDEA → Preferences → ...`)
2. Clic en **+** → selecciona **Tomcat Server**.
3. En **Tomcat Home** apunta a la carpeta de Tomcat 10
   (ej: `C:\tomcat10` o `/opt/tomcat10`).
4. Clic **OK**.

---

### Paso 4 – Crear configuración de ejecución

1. Clic en el menú desplegable arriba a la derecha → **Edit Configurations...**
2. Clic en **+** → **Tomcat Server → Local**.
3. Completa:

   | Campo              | Valor                                   |
   |--------------------|-----------------------------------------|
   | Name               | `Tomcat Local`                          |
   | Application server | Selecciona el Tomcat que agregaste      |
   | HTTP port          | `8080`                                  |
   | Open browser       | ✅ marcado                              |
   | URL                | `http://localhost:8080/biblioteca-untec/` |

4. Ve a la pestaña **Deployment**:
   - Clic en **+** → **Artifact...**
   - Selecciona `BibliotecaDigitalUNTEC:war exploded`
   - En **Application context** escribe: `/biblioteca-untec`

5. Clic **Apply → OK**.

---

### Paso 5 – Ejecutar la aplicación

1. Clic en el botón ▶️ verde (o `Shift + F10`).
2. IntelliJ compila, despliega en Tomcat y abre el navegador.
3. Accede a: **http://localhost:8080/biblioteca-untec/**

**Credenciales de demo:**

| Rol         | Email              | Contraseña |
|-------------|-------------------|------------|
| ADMIN       | admin@untec.cl    | admin123   |
| ESTUDIANTE  | juan@untec.cl     | 1234       |

---

### Paso 6 – Generar el archivo WAR para entrega

**Opción A – Desde IntelliJ:**
`Build → Build Artifacts → BibliotecaDigitalUNTEC:war → Build`
Queda en: `out/artifacts/`

**Opción B – Terminal Maven integrada:**
Panel **Maven** (lado derecho) → `Lifecycle` → doble clic en **package**
Queda en: `target/biblioteca-untec.war`

---

## 🗂️ Funcionalidades

| Módulo    | Funcionalidad                              | Acceso       |
|-----------|--------------------------------------------|--------------|
| Login     | Autenticación con email y contraseña       | Público      |
| Dashboard | Resumen: libros, disponibles, préstamos    | Autenticado  |
| Libros    | Listar, agregar, editar, eliminar          | Admin        |
| Libros    | Ver catálogo y disponibilidad              | Estudiante   |
| Préstamos | Registrar nuevo préstamo                   | Autenticado  |
| Préstamos | Ver historial                              | Autenticado  |
| Préstamos | Registrar devolución                       | Autenticado  |
| Logout    | Cerrar sesión (invalida HttpSession)       | Autenticado  |

---

## 🗃️ Base de datos H2 (por defecto)

Sin instalación. La BD se crea en `~/biblioteca_untec` al primer arranque.
Las tablas y datos demo se insertan automáticamente desde `ConexionDB.java`.

### Cambiar a MySQL (opcional)

Edita `ConexionDB.java`:
```java
private static final String URL      = "jdbc:mysql://localhost:3306/biblioteca_untec";
private static final String USUARIO  = "root";
private static final String PASSWORD = "tu_password";
private static final String DRIVER   = "com.mysql.cj.jdbc.Driver";
```
Crea la base de datos:
```sql
CREATE DATABASE biblioteca_untec CHARACTER SET utf8mb4;
```
Y descomenta la dependencia MySQL en `pom.xml`.

---

## 📋 Lecciones del módulo

| Lección | Tema               | Archivo clave                         |
|---------|--------------------|---------------------------------------|
| L1      | JEE y su entorno   | `pom.xml`, estructura Maven           |
| L2      | Java Server Pages  | `views/*.jsp` con JSTL                |
| L3      | Servlets           | `controller/*.java` con `@WebServlet` |
| L4      | Capa DAO           | `dao/*.java`, `ConexionDB.java`       |
| L5      | Patrón MVC         | Separación model / dao / controller / views |
| L6      | Despliegue         | `web.xml`, generación `.WAR`          |

---

## ❗ Problemas frecuentes

| Problema | Solución |
|----------|----------|
| `ClassNotFoundException: org.h2.Driver` | Panel Maven → clic en 🔄 Reload |
| Página en blanco | Verifica que Application Context sea `/biblioteca-untec` |
| Error 404 | Verifica que SDK sea Java 17 y packaging sea `war` |
| Tomcat no inicia | Usa Tomcat **10.x**, no Tomcat 9 (incompatible con Jakarta EE 9) |
| Error al compilar con `jakarta.servlet` | El scope en `pom.xml` debe ser `provided` ✅ |

---

## 👤 Autor

Docente:Sabina Romero
Bootcamp Java Trainee 2026 – Módulo 5

# Modulo5_Biblioteca_Digital_UNTEC_JAVA
# Proyecto-mod-5
