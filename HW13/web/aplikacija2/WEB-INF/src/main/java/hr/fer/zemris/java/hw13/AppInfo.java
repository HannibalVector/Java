package hr.fer.zemris.java.hw13;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Context Listener which remembers system time when application is started. Used to determine
 * running time of application on user's request.
 * @author ilijan
 */
@WebListener
public class AppInfo implements ServletContextListener {
    private static long time;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("startTime", System.currentTimeMillis());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
