package hr.fer.zemris.java.student0177035204.hw15.web.servlets;


import hr.fer.zemris.java.student0177035204.hw15.dao.jpa.JPAEMProvider;
import hr.fer.zemris.java.student0177035204.hw15.model.BlogEntry;
import hr.fer.zemris.java.student0177035204.hw15.model.BlogUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Adds new blog entry associated with user whose nick is provided through post request.
 *
 * @author ilijan
 */
@WebServlet("/servleti/add-post")
public class AddPost extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlogEntry blogEntry = new BlogEntry();
        blogEntry.setTitle(req.getParameter("title"));
        blogEntry.setText(req.getParameter("text"));

        String nick = req.getParameter("nick");

        BlogUser author = (BlogUser)  JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.findByNick")
                .setParameter("nick", nick)
                .getSingleResult();

        blogEntry.setCreatedAt(new Date());
        blogEntry.setCreator(author);

        JPAEMProvider.getEntityManager().persist(blogEntry);

        author.getBlogEntries().add(blogEntry);

        resp.sendRedirect("author/" + nick);
    }
}
