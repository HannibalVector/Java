package hr.fer.zemris.java.hw14;

import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.Poll;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Lists all available polls in the application.
 * @author ilijan
 */
@WebServlet(urlPatterns={"/index.html"})
public class PollsIndex extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Poll> polls = DAOProvider.getDao().getBasicEntryList();
        req.setAttribute("polls", polls);
        req.getRequestDispatcher("/WEB-INF/pages/pollsIndex.jsp").forward(req, resp);
    }
}
