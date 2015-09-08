package hr.fer.zemris.java.hw14;

import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.Poll;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet providing simple poll for given poll ID. Poll data is read from the database.
 * @author ilijan
 */
@WebServlet(urlPatterns={"/glasanje"})
public class GlasanjeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long pollID = Long.parseLong(req.getParameter("pollID"));
        Poll poll = DAOProvider.getDao().getEntry(pollID);

        req.setAttribute("selectedPoll", poll);
        req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
    }
}
