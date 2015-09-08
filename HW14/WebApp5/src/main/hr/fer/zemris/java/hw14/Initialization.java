package hr.fer.zemris.java.hw14;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import hr.fer.zemris.java.hw14.model.PollOption;
import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.dao.sql.SQLConnectionProvider;
import hr.fer.zemris.java.hw14.model.Poll;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Initializes web-application used to conduct simple polls. Initializer sets up database data,
 * initializes data source (i.e. connection pool), creates tables in database if they don't already
 * exist and fills them with polls provided in txt files from 'polls' directory.
 */
@WebListener
public class Initialization implements ServletContextListener {
	private static final String POLLS_DIRECTORY = "/WEB-INF/polls";

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		Properties config = new Properties();
		try {
			config.load(Files.newBufferedReader(
                    Paths.get(sce.getServletContext().getRealPath("/WEB-INF/database.properties"))
            ));
		} catch (IOException e) {
			e.printStackTrace();
		}

		String connectionURL =
				"jdbc:derby://"
				+ config.getProperty("host") + ":" +
						config.getProperty("port") + "/" +
						config.getProperty("name") + ";user=" +
						config.getProperty("user") + ";password=" +
						config.getProperty("password");

		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		try {
			dataSource.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Error while initializing pool.", e1);
		}
		dataSource.setJdbcUrl(connectionURL);
		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", dataSource);

		try {
			SQLConnectionProvider.setConnection(dataSource.getConnection());
			boolean tablesExist = DAOProvider.getDao().createTablesIfNotExists();
			if(!tablesExist) {
				String pollsPath = sce.getServletContext().getRealPath(POLLS_DIRECTORY);
				DAOProvider.getDao().addPolls(loadPolls(pollsPath));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads polls from 'polls' directory.
	 * @param pollsPath			absolute path to directory containing polls.
	 * @return					list of {@link Poll} objects.
	 */
	public static List<Poll> loadPolls(String pollsPath) {
		List<Poll> polls = new ArrayList<>();

		File pollsDir = new File(pollsPath);
		for (File file : pollsDir.listFiles()) {
			try {
				List<String> fileLines = Files.readAllLines(file.toPath());
				Poll poll = new Poll();
				poll.setTitle(fileLines.get(0));
				poll.setMessage(fileLines.get(1));
				for (int i = 2; i < fileLines.size(); i++) {
					String[] optionStrings = fileLines.get(i).split("\\t");
					PollOption option = new PollOption(0, optionStrings[0], optionStrings[1], 0);
					poll.getPollOptions().add(option);
				}
				polls.add(poll);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return polls;
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource dataSource = (ComboPooledDataSource)sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if(dataSource!=null) {
			try {
				DataSources.destroy(dataSource);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
