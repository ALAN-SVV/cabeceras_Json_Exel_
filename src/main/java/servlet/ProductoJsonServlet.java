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

// Servlet para generar y descargar un JSON de productos
@WebServlet("/producto.json") // Mapea la URL "/producto.json" a este servlet
public class ProductoJsonServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Crear una instancia del servicio de productos
        ProductoService service = new ProductoServiceImplement();
        // Obtener la lista de productos del servicio
        List<Producto> productos = service.listar();

        // Json
        resp.setContentType("application/json;charset=UTF-8"); // Establecer el tipo de contenido como JSON
        resp.setHeader("Content-Disposition", "attachment; filename=productos.json"); // Indicar que es un archivo para descargar
        try (PrintWriter out = resp.getWriter()) { // Usar PrintWriter para escribir la respuesta
            out.println("["); // Iniciar el array JSON
            for (int i = 0; i < productos.size(); i++) {
                Producto p = productos.get(i); // Obtener el producto actual
                out.println("  {"); // Iniciar el objeto JSON
                out.println("    \"id\": " + p.getId() + ","); // Escribir el ID del producto
                out.println("    \"nombre\": \"" + p.getNombre() + "\","); // Escribir el nombre del producto
                out.println("    \"tipo\": \"" + p.getTipo() + "\","); // Escribir el tipo del producto
                out.println("    \"precio\": " + p.getPrecio()); // Escribir el precio del producto
                out.print("  }"); // Cerrar el objeto JSON
                if (i < productos.size() - 1) { // Si no es el último producto, añadir una coma
                    out.println(",");
                } else {
                    out.println(); // Si es el último, solo cerrar la línea
                }
            }
            out.println("]"); // Cerrar el array JSON
        }
    }
}
