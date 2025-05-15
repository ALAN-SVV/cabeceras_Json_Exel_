package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Producto;
import service.ProductoService;
import service.ProductoServiceImplement;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// Servlet para generar y descargar un listado de productos en formato Excel o HTML
@WebServlet({"/productos.xls", "/productos.html"}) // Mapea las URLs "/productos.xls" y "/productos.html" a este servlet
public class ProductoXlsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Crear una instancia del servicio de productos
        ProductoService service = new ProductoServiceImplement();
        // Obtener la lista de productos del servicio
        List<Producto> productos = service.listar();

        // Establecer el tipo de contenido inicial como HTML
        resp.setContentType("text/html;charset=UTF-8");
        String servletPath = req.getServletPath(); // Obtener la ruta del servlet
        boolean esXls = servletPath.endsWith(".xls"); // Verificar si se solicita un archivo Excel

        //  Exel
        if (esXls) {
            resp.setContentType("application/vnd.ms-excel"); // Establecer el tipo de contenido para Excel
            resp.setHeader("Content-Disposition", "attachment; filename=productos.xls"); // Indicar que es un archivo para descargar
        } else {
            resp.setContentType("text/html;charset=UTF-8"); // Mantener el tipo de contenido como HTML
        }

        // html o xls
        try (PrintWriter out = resp.getWriter()) { // Usar PrintWriter para escribir la respuesta
            if (!esXls) { // Si no es un archivo Excel, generar HTML
                out.print("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta charset=\"utf-8\">");
                out.println("<title>Listado de Productos</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Listado de productos</h1>");
                out.println("<p><a href=\"" + req.getContextPath() + "/productos.xls\">Exportar a Excel</a></p>");
                out.println("<p><a href=\"" + req.getContextPath() + "/producto.json\">Mostrar JSON</a></p>");
            }

            // Generar la tabla de productos
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>id</th>");
            out.println("<th>nombre</th>");
            out.println("<th>tipo</th>");
            out.println("<th>precio</th>");
            out.println("</tr>");

            // Iterar sobre la lista de productos y crear filas en la tabla
            productos.forEach(p -> {
                out.println("<tr>");
                out.println("<td>" + p.getId() + "</td>"); // ID del producto
                out.println("<td>" + p.getNombre() + "</td>"); // Nombre del producto
                out.println("<td>" + p.getTipo() + "</td>"); // Tipo del producto
                out.println("<td>" + p.getPrecio() + "</td>"); // Precio del producto
                out.println("</tr>");
            });

            out.println("</table>");

            if (!esXls) { // Si no es un archivo Excel, cerrar el cuerpo y el HTML
                out.println("</body>");
                out.println("</html>");
            }
        }
    }
}
