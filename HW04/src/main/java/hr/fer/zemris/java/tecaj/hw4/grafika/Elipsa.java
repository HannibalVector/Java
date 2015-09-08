package hr.fer.zemris.java.tecaj.hw4.grafika;

import java.util.Scanner;

/**
 * Predstavlja geometrijski lik elipsu.
 * @author ilijan
 */
public class Elipsa extends GeometrijskiLik {

    /**
     * Statička instanca klase {@link StvarateljElipse} koja implementira sučelje
     * {@link StvarateljLika} koje sadrži metodu - tvornicu elipsi iz danog stringa.
     */
    public static final StvarateljLika STVARATELJ = new StvarateljElipse();

    /** Centar elipse. */
    private Tocka centar;

    /** Vodoravni radijus elipse. */
    private int vodoravniRadijus;

    /** Okomiti radijus elipse. */
    private int okomitiRadijus;

    /**
     * Konstruktor prima točku koja predstavlja centar elipse te njen vodoravni i
     * okomiti radijus.
     * @param centar            točka koja predstavlja centar elipse.
     * @param vodoravniRadijus  vodoravni radijus elipse.
     * @param okomitiRadijus    okomiti radijus elipse.
     */
    public Elipsa(Tocka centar, int vodoravniRadijus, int okomitiRadijus) {
        this.centar = centar;
        provjeriPozitivnuVelicinu(vodoravniRadijus, "Vodoravni radijus");
        provjeriPozitivnuVelicinu(okomitiRadijus, "Okomiti radijus");
        this.vodoravniRadijus = vodoravniRadijus;
        this.okomitiRadijus = okomitiRadijus;
    }

    private void provjeriPozitivnuVelicinu(int velicina, String nazivVelicine) {
        if (velicina < 0) {
            throw new IllegalArgumentException(nazivVelicine + " mora biti nenegativan, a iznosi " + velicina + ".");
        }
    }

    @Override
    public boolean sadrziTocku(int x, int y) {
        double dx = Math.abs(x - centar.getX());
        double dy = Math.abs(y - centar.getY());

        double dxSkalirano = dx / vodoravniRadijus;
        double dySkalirano = dy / okomitiRadijus;

        double dSkalirano = Math.pow(dxSkalirano, 2) + Math.pow(dySkalirano, 2);

        return dSkalirano < 1;
    }

    /**
     * Klasa implementira sučelje {@link StvarateljLika} koje sadrži
     * metodu - tvornicu elipsi iz danog stringa.
     */
    private static class StvarateljElipse implements StvarateljLika {
        @Override
        public String nazivLika() {
            return "ELIPSA";
        }

        @Override
        public GeometrijskiLik stvoriIzStringa(String parametri) {
            try {
                Scanner scanner = new Scanner(parametri);
                Tocka centar = new Tocka(scanner.nextInt(), scanner.nextInt());
                int vodoracniRadijus = scanner.nextInt();
                int okomitiRadijus = scanner.nextInt();
                return new Elipsa(centar, vodoracniRadijus, okomitiRadijus);

            } catch (Exception e) {
                throw new IllegalArgumentException("Problem prilikom citanja prametara.");
            }
        }
    }
}
