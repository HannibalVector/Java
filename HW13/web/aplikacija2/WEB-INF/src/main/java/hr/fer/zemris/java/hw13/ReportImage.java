package hr.fer.zemris.java.hw13;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import javax.imageio.ImageIO;
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
 * Generates pie chart for hardcoded data about distribution of different OS users.
 * @author ilijan
 */
@WebServlet(urlPatterns={"/reportImage"})
public class ReportImage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PieDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset, "Which operating system are you using?");
        BufferedImage chartImage = chart.createBufferedImage(800, 400);
        resp.setContentType("image/png");
        OutputStream out = resp.getOutputStream();
        ImageIO.write(chartImage, "png", out);
        out.close();
    }

    /**
     * Generates hardcoded pie chart data for demonstration purposes.
     * @return              pie chart data.
     */
    private  PieDataset createDataset() {
        DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("Linux", 29);
        result.setValue("Mac", 20);
        result.setValue("Windows", 51);
        return result;
    }


    /**
     * Creates pie chart using provided data set and title.
     * @param dataset   data used to generate pie chart.
     * @param title     chart title.
     * @return          new pie chart.
     */
    private JFreeChart createChart(PieDataset dataset, String title) {

        JFreeChart chart = ChartFactory.createPieChart3D(title,
                dataset,
                true,
                true,
                false);

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
    }
}
