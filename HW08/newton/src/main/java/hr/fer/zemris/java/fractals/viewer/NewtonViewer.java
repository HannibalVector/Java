package hr.fer.zemris.java.fractals.viewer;

import hr.fer.zemris.java.fractals.Newton;
import hr.fer.zemris.java.fractals.mandelbrot.Mandelbrot;

import javax.swing.*;

/**
 * @author ilijan
 */
public class NewtonViewer {
    /**
     * Pokreće prikaz Mandelbrotovog fraktala izračunatog
     * slijedno.
     *
     * @param args argumenti naredbenog retka: ne koriste se.
     */
    public static void main(String[] args) {
        Newton.show();
    }

    /**
     * Metoda pokreće grafički preglednik u kojem će prikazati
     * fraktal čiji je generator predan kao parametar.
     *
     * @param iFractalProducer generator fraktala; ne smije biti <code>null</code>
     */
    public static void show(final IFractalProducer iFractalProducer) {
        if(iFractalProducer==null) {
            throw new IllegalArgumentException("Generator fraktala ne smije biti null.");
        }
        if(SwingUtilities.isEventDispatchThread()) {
            showInEDT(iFractalProducer);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    showInEDT(iFractalProducer);
                }
            });
        }
    }

    /**
     * Pomoćna metoda koja pokreće prikaz uz pretpostavku da
     * je pozvana iz EDT-a.
     *
     * @param iFractalProducer generator fraktala
     */
    private static void showInEDT(IFractalProducer iFractalProducer) {
        FractalViewerFrame fvf = new FractalViewerFrame(iFractalProducer);
        fvf.setVisible(true);
    }
}
