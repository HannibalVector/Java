package hr.fer.zemris.java.student0177035204.hw15.web.servlets;

import hr.fer.zemris.java.student0177035204.hw15.dao.jpa.JPAEMProvider;
import hr.fer.zemris.java.student0177035204.hw15.util.Util;
import hr.fer.zemris.java.student0177035204.hw15.model.BlogUser;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Main servlet provides user with login form and list of all available authors.
 */
@WebServlet("/servleti/main")
public class MainServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		@SuppressWarnings("unchecked")
		List<BlogUser> users = (List<BlogUser>) JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.getAll")
				.getResultList();
		req.setAttribute("authors", users);

		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String pressedButton = req.getParameter("action");
		if (pressedButton.equals("Register")) {
			resp.sendRedirect("register");
			return;
		}

		String nick = req.getParameter("nick");
		String passwordHash = Util.sha1Hex(req.getParameter("password"));

		BlogUser user = null;
		try {
			user = (BlogUser) JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.findByNick")
					.setParameter("nick", nick)
					.getSingleResult();
		} catch (NoResultException ex) {
			req.setAttribute("title", "Invalid nickname");
			req.setAttribute("message", "User with nickname " + nick + " has not been found.");

			req.getRequestDispatcher("/WEB-INF/pages/message.jsp").forward(req, resp);
			return;
		}


		if (!user.getPasswordHash().equals(passwordHash)) {

			req.getSession().setAttribute("attemptedNick", user.getNick());
			req.setAttribute("title", "Wrong password");
			req.setAttribute("message", "Wrong password for user " + nick + ".");

			req.getRequestDispatcher("/WEB-INF/pages/message.jsp").forward(req, resp);
			return;
		}

		req.getSession().setAttribute("current.user.id", user.getId());
		req.getSession().setAttribute("current.user.firstName", user.getFirstName());
		req.getSession().setAttribute("current.user.lastName", user.getLastName());
		req.getSession().setAttribute("current.user.nick", user.getNick());

		resp.sendRedirect("main");
	}
	
}
