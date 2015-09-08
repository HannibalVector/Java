package hr.fer.zemris.java.student0177035204.hw15.web.servlets;


import hr.fer.zemris.java.student0177035204.hw15.dao.jpa.JPAEMProvider;
import hr.fer.zemris.java.student0177035204.hw15.model.BlogEntry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Updates database with edited data for existing blog entry.
 * @author ilijan
 */
@WebServlet("/servleti/edit-post")
public class EditPost extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long id = Long.parseLong(req.getParameter("id"));
        BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
        blogEntry.setTitle(req.getParameter("title"));
        blogEntry.setText(req.getParameter("text"));

        blogEntry.setLastModifiedAt(new Date());

        resp.sendRedirect("author/" + blogEntry.getCreator().getNick());
    }
}
