package hr.fer.zemris.java.hw13;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates list of trigonometric functions (sines and cosines) for integer angles
 * (in degrees) in given range.
 * @author ilijan
 */
@WebServlet(urlPatterns={"/trigonometric"})
public class Trigonometric extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Integer a;
        Integer b;

        try {
            a = Integer.valueOf(req.getParameter("a"));
        } catch (Exception e) {
            a = 0;
        }

        try {
            b = Integer.valueOf(req.getParameter("b"));
        } catch (Exception e) {
            b = 360;
        }

        if (a > b) {
            Integer tmp = a;
            a = b;
            b = tmp;
        }

        if (b > a + 720) {
            b = a + 720;
        }

        List<Entry> results = new ArrayList<>();
        for (int i = a, n = b; i <= n; i++) {
            results.add(new Entry(i));
        }

        req.setAttribute("results", results);

        req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp")
                .forward(req, resp);

    }

    /**
     * Represents single entry in the list of results (used to populate table of values of trigonometric
     * functions).
     */
    public static class Entry {
        /** Angle in degrees. */
        private int angle;
        /** Sine of the given angle. */
        private double sine;
        /** Cosine of the given angle. */
        private double cosine;
        /** Format used for printing double values. */
        private static final String format = "%.4f";

        /**
         * Constructor initializes new entry using provided angle.
         * @param angle     angle in degrees.
         */
        public Entry(int angle) {
            this.angle = angle;
            sine = Math.sin(Math.toRadians(angle));
            cosine = Math.cos(Math.toRadians(angle));
        }

        /**
         * Getter for the angle.
         * @return  angle in the entry.
         */
        public int getAngle() {
            return angle;
        }

        /**
         * Getter for the string containing sine for the given angle.
         * @return  string containing sine for the given angle.
         */
        public String getSine() {
            return String.format(format, sine);
        }

        /**
         * Getter for the string containing cosine for the given angle.
         * @return  string containing cosine for the given angle.
         */
        public String getCosine() {
            return String.format(format, cosine);
        }
    }
}

