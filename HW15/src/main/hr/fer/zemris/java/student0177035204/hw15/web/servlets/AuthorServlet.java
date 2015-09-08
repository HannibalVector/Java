package hr.fer.zemris.java.student0177035204.hw15.web.servlets;

import hr.fer.zemris.java.student0177035204.hw15.dao.jpa.JPAEMProvider;
import hr.fer.zemris.java.student0177035204.hw15.model.BlogEntry;
import hr.fer.zemris.java.student0177035204.hw15.model.BlogUser;

import javax.persistence.RollbackException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Handles all requests that have path that begins with '/servleti/author'.
 */
@WebServlet("/servleti/author/*")
public class AuthorServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Nickname extracted from the path.
	 */
	String nick;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		processPathParameters(req, resp);
		
		@SuppressWarnings("unchecked")
		List<BlogEntry> posts =
				(List<BlogEntry>) JPAEMProvider.getEntityManager().createNamedQuery("BlogEntry.findByCreator")
				.setParameter("creator", nick)
				.getResultList();

		req.setAttribute("posts", posts);

		req.getRequestDispatcher("/WEB-INF/pages/listPosts.jsp").forward(req, resp);
	}

	/**
	 * Processes parameters sent trough path.
	 * @param req		server request.
	 * @param resp		server response.
	 * @throws ServletException		if error in communicating with client occurs.
	 * @throws IOException			if error in communicating with client occurs.
	 */
	private void processPathParameters(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] pathParams = req.getPathInfo().split("/");

		nick = pathParams[1];

		BlogUser author = (BlogUser) JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.findByNick")
				.setParameter("nick", nick)
				.getSingleResult();

		req.setAttribute("author", author);

		String currentNick = (String) req.getSession().getAttribute("current.user.nick");
		if (currentNick != null && currentNick.equals(nick)) {
			req.setAttribute("editingAllowed", true);
		} else {
			req.setAttribute("editingAllowed", false);
		}

		if (pathParams.length > 2) {
			processSecondParameter(pathParams[2], req, resp);
		}
	}

	/**
	 * Processes second parameter sent through path and interprets it as command to create new blog entry, edit existing
	 * blog entry, or display particular blog entry.
	 *
	 * @param secondParameter	string containing second parameter sent through path.
	 * @param req				server request.
	 * @param resp				server response.
	 * @throws ServletException		if error in communicating with client occurs.
	 * @throws IOException			if error in communicating with client occurs.
	 */
	private void processSecondParameter(String secondParameter, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		switch(secondParameter){
			case "new":
				checkPermissions(req, resp);
				addPost(req, resp);
				break;
			case "edit":
				checkPermissions(req, resp);
				editPost(req, resp);
				break;
			default:
				Long postId =  Long.parseLong(secondParameter);
				BlogEntry post = JPAEMProvider.getEntityManager().find(BlogEntry.class, postId);
				req.setAttribute("post", post);
				req.getRequestDispatcher("/WEB-INF/pages/showPost.jsp").forward(req, resp);
				break;
		}
	}

	/**
	 * Edits post for id provided through request parameters.
	 * @param req		server request.
	 * @param resp		server response.
	 * @throws ServletException		if error in communicating with client occurs.
	 * @throws IOException			if error in communicating with client occurs.
	 */
	private void editPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long id = Long.parseLong(req.getParameter("id"));
		BlogEntry post = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		req.setAttribute("post", post);
		req.getRequestDispatcher("/WEB-INF/pages/editPost.jsp").forward(req, resp);

	}

	/**
	 * Adds posts for user specified in request parameters.
	 * @param req		server request.
	 * @param resp		server response.
	 * @throws ServletException		if error in communicating with client occurs.
	 * @throws IOException			if error in communicating with client occurs.
	 */
	private void addPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/addPost.jsp").forward(req, resp);
	}

	/**
	 * Checks if user has permission to add or edit blog entries.
	 *
	 * @param req		server request.
	 * @param resp		server response.
	 * @throws ServletException		if error in communicating with client occurs.
	 * @throws IOException			if error in communicating with client occurs.
	 */
	private void checkPermissions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if ((Boolean)req.getAttribute("editingAllowed")) {
			return;
		}

		req.setAttribute("title", "Not allowed");
		req.setAttribute("message", "You are not allowed to edit posts for user " + nick + "." );
		req.setAttribute("returnPoint", "/servleti/author/" + nick);

		req.getRequestDispatcher("/WEB-INF/pages/message.jsp").forward(req, resp);
	}

}
