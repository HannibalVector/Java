package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Draws circle on screen.
 * @author ilijan
 */
public class CircleWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) {
        BufferedImage bim = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = bim.createGraphics();

        g2d.setBackground(Color.WHITE);
        g2d.setColor(Color.RED);
        g2d.fillOval(0, 0, 199, 199);

        g2d.dispose();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        context.setMimeType("image/png");
        try {
            ImageIO.write(bim, "png", bos);
            context.write(bos.toByteArray());
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
