package hr.fer.zemris.java.student0177035204.hw15.web.servlets;



import hr.fer.zemris.java.student0177035204.hw15.dao.jpa.JPAEMProvider;
import hr.fer.zemris.java.student0177035204.hw15.model.BlogComment;
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
 * Adds comment to the blog entry whose id is provided through post request. If user is not logged in, commenting
 * is not possible, and user is redirected to blog entry.
 *
 * @author ilijan
 */
@WebServlet("/servleti/add-comment")
public class AddComment extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long id = Long.parseLong(req.getParameter("id"));
        BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
        BlogUser author = blogEntry.getCreator();

        String currentUser = (String) req.getSession().getAttribute("current.user.nick");
        if (currentUser == null) {
            req.setAttribute("title", "Not Allowed");
            req.setAttribute("message", "Commenting is only allowed to registered users.");
            req.setAttribute("returnPoint", "/servleti/author/" + author.getNick() + "/" + blogEntry.getId());

            req.getRequestDispatcher("/WEB-INF/pages/message.jsp").forward(req, resp);
        }

        BlogUser user = (BlogUser) JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.findByNick")
                .setParameter("nick", currentUser)
                .getSingleResult();


        BlogComment newComment = new BlogComment();

        newComment.setMessage(req.getParameter("message"));
        newComment.setBlogEntry(blogEntry);
        newComment.setAuthor(user);
        newComment.setPostedOn(new Date());

        JPAEMProvider.getEntityManager().persist(newComment);

        blogEntry.getComments().add(newComment);

        resp.sendRedirect("author/" + author.getNick() + "/" + blogEntry.getId());
    }
}
