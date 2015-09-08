package hr.fer.zemris.java.tecaj.hw4.grafika;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

/**
 * Osnovni apstraktni razred koji predstavlja geometrijski lik i kojeg će ostali razredi,
 * koji reprezentiraju konkretne geometrijske likove, nasljeđivati.
 * @author ilijan
 */
public abstract class GeometrijskiLik {

    /**
     * Ugniježđena klasa koja prestavlja strukturu za pohranu točke u koordinatnom sustavu.
     */
    public static class Tocka {
        /** x koordinata točke. */
        private int x;

        /** y koordinata točke. */
        private int y;

        /**
         * Konstruktor točke koji prima x i y koordinatu.
         * @param x apscisa točke.
         * @param y ordinata točke.
         */
        public Tocka(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Getter za x koordinatu točke.
         * @return  x koordinata točke.
         */
        public int getX() {
            return x;
        }

        /**
         * Getter za y koordinatu točke.
         * @return  y koordinata točke.
         */
        public int getY() {
            return y;
        }
    }

    /**
     * Metoda provjerava sadrži li {@code GeometrijskiLik} danu točku (x, y).
     * @param x x koordinata točke koja se provjerava.
     * @param y y koordinata točke koja se provjerava.
     * @return  {@code true} ako {@code GeometrijskiLik} sadrži danu točku,
     *          {@code false} inače.
     */
    public abstract boolean sadrziTocku(int x, int y);

    /**
     * Metoda popunjava danu sliku, klase {@link Slika} na način da upali točke
     * na slici koje su unutar lika.
     * @param slika slika na kojoj metoda pali pojedinačne točke.
     */
    public void popuniLik (Slika slika) {
        for (int y = 0, sirina = slika.getSirina(), visina = slika.getVisina(); y < visina; y++) {
            for (int x = 0; x < sirina; x++) {
                if (this.sadrziTocku(x, y)) {
                    slika.upaliTocku(x, y);
                }
            }
        }
    }
}
