package hr.fer.zemris.java.hw13;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Sets background color for all pages in the application.
 * @author ilijan
 */
@WebServlet(urlPatterns={"/setcolor"})
public class SetColor extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String color = req.getParameter("bg");
        req.getSession().setAttribute("pickedBgCol", color);
        resp.sendRedirect("index.jsp");
    }
}
