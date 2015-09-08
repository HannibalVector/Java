package hr.fer.zemris.java.student0177035204.hw15.web.servlets;


import hr.fer.zemris.java.student0177035204.hw15.dao.jpa.JPAEMProvider;
import hr.fer.zemris.java.student0177035204.hw15.model.BlogUser;
import hr.fer.zemris.java.student0177035204.hw15.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Performs registration of new user by adding new user to the database.
 * @author ilijan
 */
@WebServlet("/servleti/complete-registration")
public class CompleteRegistration extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        BlogUser blogUser = new BlogUser();
        blogUser.setFirstName(req.getParameter("firstName"));
        blogUser.setLastName(req.getParameter("lastName"));
        blogUser.setEmail(req.getParameter("email"));
        blogUser.setNick(req.getParameter("nick"));

        req.getSession().setAttribute("attemptedFirstName", blogUser.getFirstName());
        req.getSession().setAttribute("attemptedLastName", blogUser.getLastName());
        req.getSession().setAttribute("attemptedEmail", blogUser.getEmail());
        req.getSession().setAttribute("attemptedNick", blogUser.getNick());


        if (blogUser.getFirstName().isEmpty() ||
                blogUser.getLastName().isEmpty() ||
                blogUser.getEmail().isEmpty() ||
                blogUser.getNick().isEmpty()) {
            req.setAttribute("title", "Error");
            req.setAttribute("message", "All fields are required.");
            req.setAttribute("returnPoint", "/servleti/register");

            req.getRequestDispatcher("/WEB-INF/pages/message.jsp").forward(req, resp);
            return;
        }

        if (!JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.findByNick")
                .setParameter("nick", blogUser.getNick()).getResultList().isEmpty()) {
            req.getSession().removeAttribute("attemptedNick");

            req.setAttribute("title", "Nickname already exists");
            req.setAttribute("message", "Please choose another nickname.");

            req.getRequestDispatcher("/WEB-INF/pages/message.jsp").forward(req, resp);
            return;
        }

        String passwordHash = Util.sha1Hex(req.getParameter("password"));
        blogUser.setPasswordHash(passwordHash);

        JPAEMProvider.getEntityManager().persist(blogUser);

        req.setAttribute("title", "Registration successful");
        req.setAttribute("message", "Registration completed successfully.");

        req.getRequestDispatcher("/WEB-INF/pages/message.jsp").forward(req, resp);
    }
}
