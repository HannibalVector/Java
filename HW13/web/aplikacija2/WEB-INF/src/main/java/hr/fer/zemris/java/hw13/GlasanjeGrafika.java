package hr.fer.zemris.java.hw13;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Creates pie chart representing poll results.
 * @author ilijan
 */
@WebServlet(urlPatterns={"/glasanje-grafika"})
public class GlasanjeGrafika extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PieDataset dataset = createDataset(req.getServletContext());
        JFreeChart chart = createChart(dataset, "Najpopularniji bend");
        BufferedImage chartImage = chart.createBufferedImage(800, 400);
        resp.setContentType("image/png");
        OutputStream out = resp.getOutputStream();
        ImageIO.write(chartImage, "png", out);
        out.close();
    }

    /**
     * Creates data set for pie chart.
     * @param servletContext    used to resolve absolute path to file containing poll results.
     * @return                  data set for pie chart.
     * @throws IOException      if reading from file is not possible.
     */
    private  PieDataset createDataset(ServletContext servletContext) throws IOException {
        DefaultPieDataset result = new DefaultPieDataset();
        List<Artists.ArtistEntry> artists = Artists.getList(servletContext);

        for (int i = 0; i < artists.size(); i++) {
            result.setValue(artists.get(i).getName(), artists.get(i).getVotes());
        }
        return result;
    }

    /**
     * Creates pie chart using provided data set and title.
     * @param dataset   data used to generate pie chart.
     * @param title     chart title.
     * @return          new pie chart.
     */
    private JFreeChart createChart(PieDataset dataset, String title) {

        JFreeChart chart = ChartFactory.createPieChart3D(title,          // chart title
                dataset,                // data
                true,                   // include legend
                true,
                false);

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
    }
}
