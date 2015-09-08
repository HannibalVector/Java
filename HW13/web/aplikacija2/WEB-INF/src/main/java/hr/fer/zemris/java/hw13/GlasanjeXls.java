package hr.fer.zemris.java.hw13;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates Excel spreadsheet containing poll results.
 * @author ilijan
 */
@WebServlet(urlPatterns={"/glasanje-xls"})
public class GlasanjeXls extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        createExcelFile(req, resp);
    }

    /**
     * Generates Excel spreadsheet containing poll results and sends it to client.
     * @param req               request used to resolve absolute path to the file containing poll results.
     * @param resp              response used to send spreadsheet to the client.
     * @throws ServletException in case of error in communication with client.
     * @throws IOException      if reading from file is not possible.
     */
    private void createExcelFile(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Rezultati glasanja");
            HSSFRow heading = sheet.createRow(0);
            heading.createCell(0).setCellValue("Bend");
            heading.createCell(1).setCellValue("Broj glasova");

            List<Artists.ArtistEntry> artists = new ArrayList<>(Artists.getList(req.getServletContext()));
            artists.sort((a1, a2) ->
                    -1 * Integer.compare(a1.getVotes(), a2.getVotes()));
            req.setAttribute("artists", artists);

            int rowNumber = 1;
            for (Artists.ArtistEntry artist : artists) {
                HSSFRow row = sheet.createRow(rowNumber);
                row.createCell(0).setCellValue(artist.getName());
                row.createCell(1).setCellValue(artist.getVotes());
                rowNumber++;
            }

            OutputStream out = resp.getOutputStream();
            workbook.write(out);
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
