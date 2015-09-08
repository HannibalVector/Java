package hr.fer.zemris.java.hw13;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates Excel spreadsheet containing requested integer powers in range [1, n] for requested
 * range of integers [a, b], where a and b are in range [-100, 100], and n in range [1, 5].
 * @author ilijan
 *
 */
@WebServlet(urlPatterns={"/powers"})
public class Powers extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Integer a = Integer.valueOf(req.getParameter("a"));
            Integer b = Integer.valueOf(req.getParameter("b"));
            Integer n = Integer.valueOf(req.getParameter("n"));

            if (a < -100 || a > 100) {
                sendError("Parameter <b>a</b> must be in range [-100, 100].", req, resp);
            }
            if (b < -100 || b > 100) {
                sendError("Parameter <b>b</b> must be in range [-100, 100].", req, resp);
            }
            if (n < 1 || n > 5) {
                sendError("Parameter <b>n</b> must be in range [1, 5].", req, resp);
            }
            createExcelFile(a, b, n, req, resp);

        } catch(Exception e) {
            sendError("Invalid parameter format.", req, resp);
        }
    }

    /**
     * Generates Excel spreadsheet containing requested powers of integers in requested range.
     * @param a     Starting integer in sequence.
     * @param b     Ending integer in sequence.
     * @param n     Highest power to be calculated (method generates all powers in range [1, n]).
     * @param req               request used to resolve absolute path to the file containing poll results.
     * @param resp              response used to send spreadsheet to the client.
     * @throws ServletException in case of error in communication with client.
     * @throws IOException      if reading from file is not possible.
     */
    private void createExcelFile(Integer a, Integer b, Integer n, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try{
            HSSFWorkbook workbook = new HSSFWorkbook();
            for (int power = 1; power <= n; power++) {
                HSSFSheet sheet = workbook.createSheet("x^" + power);
                HSSFRow heading = sheet.createRow(0);
                heading.createCell(0).setCellValue("x");
                heading.createCell(1).setCellValue("x^" + power);

                for (int number = a; number <= b; number++) {
                    HSSFRow row = sheet.createRow(number - a + 1);
                    row.createCell(0).setCellValue(number);
                    row.createCell(1).setCellValue(Math.pow(number, power));
                }
            }

            OutputStream out = resp.getOutputStream();
            workbook.write(out);
            out.close();

        } catch (Exception ex) {
            sendError(ex.getMessage(), req, resp);
        }

    }

    /**
     * In case of error generates error using error template and sends it to the client.
     * @param message               error message.
     * @param req               HTTP request forwarded to error generating script.
     * @param resp              HTTP response forwarded to error generating script.
     * @throws ServletException in case of error in communication with client.
     * @throws IOException      if reading from file containing error template is not possible.
     */
    private void sendError(String message, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("errorMessage", message);
        req.getRequestDispatcher("/WEB-INF/pages/error.jsp")
                .forward(req, resp);

    }
}
