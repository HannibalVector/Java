package hr.fer.zemris.java.hw13;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Reads poll results, sorts them, and redirects to the script used to generate report.
 * @author ilijan
 */
@WebServlet(urlPatterns={"/glasanje-rezultati"})
public class GlasanjeRezultatiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Artists.ArtistEntry> artists = new ArrayList<>(Artists.getList(req.getServletContext()));
        artists.sort((a1, a2) ->
                -1 * Integer.compare(a1.getVotes(), a2.getVotes()));
        req.setAttribute("artists", artists);

        req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
    }
}
