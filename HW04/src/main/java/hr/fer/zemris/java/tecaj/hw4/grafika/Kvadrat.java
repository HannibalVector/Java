package hr.fer.zemris.java.tecaj.hw4.grafika;

import java.util.Scanner;

/**
 * Predstavlja geometrijski lik kvadrat.
 * @author ilijan
 */
public class Kvadrat extends Pravokutnik {

    /**
     * Statička instanca klase {@link StvarateljKvadrata} koja implementira sučelje
     * {@link StvarateljLika} koje sadrži metodu - tvornicu kvadrata iz danog stringa.
     */
    public static final StvarateljLika STVARATELJ = new StvarateljKvadrata();

    /**
     * Konstruktor prima točku koja predstavlja lijevi gornji ugao kvadrata te
     * duljinu stranice.
     * @param lijeviGornjiUgao  {@link Tocka} koja je lijevi gornji ugao kvadrata.
     * @param duljinaStranice   duljina stranice kvadrata.
     */
    public Kvadrat(Tocka lijeviGornjiUgao, int duljinaStranice) {
        super(lijeviGornjiUgao, duljinaStranice, duljinaStranice);
    }

    /**
     * Klasa implementira sučelje {@link StvarateljLika} koje sadrži
     * metodu - tvornicu kvadrata iz danog stringa.
     */
    private static class StvarateljKvadrata implements StvarateljLika {
        @Override
        public String nazivLika() {
            return "KVADRAT";
        }

        @Override
        public GeometrijskiLik stvoriIzStringa(String parametri) {
            try {
                Scanner scanner = new Scanner(parametri);
                Tocka lijeviGornjiUgao = new Tocka(scanner.nextInt(), scanner.nextInt());
                int duljinaStranice = scanner.nextInt();
                return new Kvadrat(lijeviGornjiUgao, duljinaStranice);
            } catch (Exception e) {
                throw new IllegalArgumentException("Problem prilikom citanja prametara.");
            }
        }
    }
}
