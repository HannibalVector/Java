package hr.fer.zemris.java.tecaj.hw4.grafika;

import java.util.Scanner;

/**
 * Predstavlja geometrijski lik kružnicu.
 * @author ilijan
 */
public class Kruznica extends Elipsa {

    /**
     * Statička instanca klase {@link StvarateljKruznice} koja implementira sučelje
     * {@link StvarateljLika} koje sadrži metodu - tvornicu kružnica iz danog stringa.
     */
    public static final StvarateljLika STVARATELJ = new StvarateljKruznice();

    /**
     * Konstruktor prima točku koja predstavlja centar kružnice te njen radijus.
     * @param centar    točka koja predstavlja centar kružnice.
     * @param radijus   radijus kružnice.
     */
    public Kruznica(Tocka centar, int radijus) {
        super(centar, radijus, radijus);
    }

    /**
     * Klasa implementira sučelje {@link StvarateljLika} koje sadrži
     * metodu - tvornicu kružnica iz danog stringa.
     */
    private static class StvarateljKruznice implements StvarateljLika {
        @Override
        public String nazivLika() {
            return "KRUG";
        }

        @Override
        public GeometrijskiLik stvoriIzStringa(String parametri) {
            try {
                Scanner scanner = new Scanner(parametri);
                Tocka centar = new Tocka(scanner.nextInt(), scanner.nextInt());
                int radijus = scanner.nextInt();
                return new Kruznica(centar, radijus);
            } catch (Exception e) {
                throw new IllegalArgumentException("Problem prilikom citanja prametara.");
            }
        }
    }
}
