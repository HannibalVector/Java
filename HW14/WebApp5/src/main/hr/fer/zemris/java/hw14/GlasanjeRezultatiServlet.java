package hr.fer.zemris.java.hw14;

import hr.fer.zemris.java.hw14.model.Poll;
import hr.fer.zemris.java.hw14.model.PollOption;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads poll results, sorts them, and redirects to the script used to generate report.
 * @author ilijan
 */
@WebServlet(urlPatterns={"/glasanje-rezultati"})
public class GlasanjeRezultatiServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Poll poll = (Poll)req.getAttribute("selectedPoll");

        List<PollOption> results = new ArrayList<>();
        results.addAll(poll.getPollOptions());
        results.sort((o1, o2) ->
                -1 * Integer.compare(o1.getVotes(), o2.getVotes()));

        req.setAttribute("results", results);
        req.setAttribute("selectedPoll", poll);

        req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
    }
}
