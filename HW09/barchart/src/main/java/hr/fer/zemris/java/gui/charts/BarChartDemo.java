package hr.fer.zemris.java.gui.charts;

import com.sun.org.apache.xpath.internal.SourceTree;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Application for demonstration of usage of {@link BarChartComponent}. Application expects single command-line
 * argument - path to file containing data used to plot bar chart. The file should be formatted in 6 lines:
 * <p>x-axis label</p>
 * <p>y-axis label</p>
 * <p>x1,y1 x2,y2 x3,y3 ...</p>
 * <p>minimum y value</p>
 * <p>maximum y value</p>
 * <p>spacing between ticks on the y-axis.</p>
 * @author ilijan
 */
public class BarChartDemo extends JFrame {
    /** Data used for drawing chart. */
    private BarChart chart;
    /** Path to file containing data. */
    private Path filePath;

    /**
     * Constructor initializes application using path to filename containing data (only used for printing filename on screen)
     * and reference to {@link BarChart} object containing data used for actual drawing.
     * @param filePath  file containing data.
     * @param chart     {@link BarChart} object containing data.
     */
    public BarChartDemo(Path filePath, BarChart chart) {
        this.setTitle("BarChart demo");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(600, 400));
        this.chart = chart;
        this.filePath = filePath;

        initGUI();
    }

    /**
     * Initializes graphical user interface by setting up layout and adding components.
     */
    private void initGUI() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(Color.LIGHT_GRAY);
        JLabel fileName = new JLabel(filePath.toString());
        fileName.setHorizontalAlignment(SwingUtilities.CENTER);
        getContentPane().add(fileName, BorderLayout.PAGE_START);
        getContentPane().add(new BarChartComponent(chart), BorderLayout.CENTER);
    }

    /**
     * The main method for the application {@link BarChartDemo}.
     * @param args  command-line arguments. Single argument is expected - path to file containing data.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No chart data file.");
            System.exit(0);
        }
        Path filePath = Paths.get(args[0]);
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {

            String xLabel = reader.readLine();
            String ylabel = reader.readLine();

            String[] data = reader.readLine().split(" ");
            List<XYValue> lista = new ArrayList<>(data.length);
            for (int i = 0; i < data.length; i++) {
                String[] xAndYValues = data[i].split(",");
                int x = Integer.parseInt(xAndYValues[0]);
                int y = Integer.parseInt(xAndYValues[1]);
                lista.add(new XYValue(x, y));
            }

            int yMin = Integer.parseInt(reader.readLine());
            int yMax = Integer.parseInt(reader.readLine());
            int tickSpacing = Integer.parseInt(reader.readLine());

            SwingUtilities.invokeLater(() -> {
                JFrame frame = new BarChartDemo(filePath, new BarChart(lista, xLabel, ylabel, yMin, yMax, tickSpacing));
                frame.setVisible(true);
            });
        } catch (IOException ex) {
            System.out.println("Unable to open file.");
        } catch (Exception ex) {
            System.out.println("Could not parse data from given file.");
        }
    }
}
