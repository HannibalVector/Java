package hr.fer.zemris.java.hw13;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Used to update votes in a poll and to redirect results to a script generating report.
 * @author ilijan
 */
@WebServlet(urlPatterns={"/glasanje-glasaj"})
public class GlasanjeGlasajServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int index = Integer.parseInt(req.getParameter("id")) - 1;
        Artists.getList(req.getServletContext()).get(index).vote();

        resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
    }
}
