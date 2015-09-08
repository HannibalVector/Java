package hr.fer.zemris.java.tecaj.hw4.grafika;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

import java.util.Scanner;

/**
 * Predstavlja geometrijski lik pravokutnik.
 * @author ilijan
 */
public class Pravokutnik extends GeometrijskiLik {

    /**
     * Statička instanca klase {@link StvarateljPravokutnika} koja implementira sučelje
     * {@link StvarateljLika} koje sadrži metodu - tvornicu pravokutnika iz danog stringa.
     */
    public static final StvarateljLika STVARATELJ = new StvarateljPravokutnika();

    /** Točka koja predstavlja lijevi gornji ugao pravokutnika. */
    private Tocka lijeviGornjiUgao;

    /** Širina pravokutnika. */
    private int sirina;

    /** Visina pravokutnika. */
    private int visina;

    /**
     * Konstruktor prima točku koja predstavlja lijevi gornji ugao pravokutnika te
     * širinu i visinu.
     * @param lijeviGornjiUgao  {@link Tocka} koja je lijevi gornji ugao pravokutnika.
     * @param sirina            širina pravokutnika.
     * @param visina            visina pravokutnika.
     * @throws IllegalArgumentException ako je proslijeđena širina ili visina negativna.
     */
    public Pravokutnik(Tocka lijeviGornjiUgao, int sirina, int visina) {
        this.lijeviGornjiUgao = lijeviGornjiUgao;

        provjeriPozitivnuVelicinu(sirina, "Sirina");
        provjeriPozitivnuVelicinu(visina, "Visina");
        this.sirina = sirina;
        this.visina = visina;
    }

    private void provjeriPozitivnuVelicinu(int velicina, String nazivVelicine) {
        if (velicina < 0) {
            throw new IllegalArgumentException(nazivVelicine + " mora biti nenegativna, a iznosi " + velicina + ".");
        }
    }

    @Override
    public boolean sadrziTocku(int x, int y) {
        if (x < lijeviGornjiUgao.getX() || x >= lijeviGornjiUgao.getX() + sirina) {
            return false;
        }
        if (y < lijeviGornjiUgao.getY() || y > lijeviGornjiUgao.getY() + visina) {
            return false;
        }
        return true;
    }

    @Override
    public void popuniLik(Slika slika) {
        int minX = Math.max(lijeviGornjiUgao.getX(), 0);
        int minY = Math.max(lijeviGornjiUgao.getY(), 0);
        int maxX = Math.min(lijeviGornjiUgao.getX() + sirina, slika.getSirina());
        int maxY = Math.min(lijeviGornjiUgao.getY() + visina, slika.getVisina());
        for (int y = minY; y < maxY; y++) {
            for(int x = minX; x < maxX; x++) {
                slika.upaliTocku(x, y);
            }
        }
    }

    /**
     * Klasa implementira sučelje {@link StvarateljLika} koje sadrži
     * metodu - tvornicu pravokutnika iz danog stringa.
     */
    private static class StvarateljPravokutnika implements StvarateljLika {
        @Override
        public String nazivLika() {
            return "PRAVOKUTNIK";
        }

        @Override
        public GeometrijskiLik stvoriIzStringa(String parametri) {
            try {
                Scanner scanner = new Scanner(parametri);
                Tocka lijeviGornjiUgao = new Tocka(scanner.nextInt(), scanner.nextInt());
                int sirina = scanner.nextInt();
                int visina = scanner.nextInt();
                return new Pravokutnik(lijeviGornjiUgao, sirina, visina);
            } catch (Exception e) {
                throw new IllegalArgumentException("Problem prilikom citanja prametara. " + e);
            }
        }
    }
}