package hr.fer.zemris.java.hw14;

import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.Poll;
import hr.fer.zemris.java.hw14.model.PollOption;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Used to update votes in a poll and to redirect results to a script generating report.
 * @author ilijan
 */
@WebServlet(urlPatterns={"/glasanje-glasaj"})
public class GlasanjeGlasajServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long pollID = Long.parseLong(req.getParameter("pollID"));
        int optionIndex = Integer.parseInt(req.getParameter("optionIndex"));

        Poll selectedPoll = DAOProvider.getDao().getEntry(pollID);
        PollOption selectedOption = selectedPoll.getPollOptions().get(optionIndex);
        selectedOption.vote();
        DAOProvider.getDao().updateVotes(selectedOption);

        req.setAttribute("selectedPoll", selectedPoll);
        req.getRequestDispatcher("glasanje-rezultati").forward(req, resp);
    }
}
