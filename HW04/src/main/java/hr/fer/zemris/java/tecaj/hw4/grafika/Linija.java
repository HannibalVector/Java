package hr.fer.zemris.java.tecaj.hw4.grafika;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

import java.util.Scanner;

/**
 * Predstavlja dužinu u koordinatnom sustavu.
 * @author ilijan
 */
public class Linija extends GeometrijskiLik {

    /**
     * Statička instanca klase {@link StvarateljLinije} koja implementira sučelje
     * {@link StvarateljLika} koje sadrži metodu - tvornicu linije iz danog stringa.
     */
    public static final StvarateljLika STVARATELJ = new StvarateljLinije();

    /** Početna točka dužine. */
    private Tocka pocetnaTocka;

    /** Završna točka dužine. */
    private Tocka zavrsnaTocka;

    /**
     * Konstruktor prima početnu i završnu točku dužine.
     * @param pocetnaTocka  početna točka dužine.
     * @param zavrsnaTocka  završna točka dužine.
     */
    public Linija(Tocka pocetnaTocka, Tocka zavrsnaTocka) {
        this.pocetnaTocka = pocetnaTocka;
        this.zavrsnaTocka = zavrsnaTocka;
    }

    @Override
    public boolean sadrziTocku(int x, int y) {
       /*
            Metoda konstruira dva vektora s pocetnom tockom u (x, y) i krajem u pocetnoj tocki, odnosno zavrsnoj tocki.
            Ukoliko je vektorski produkt ova dva vektora po modulu jednak nuli, tada su ti vektori kolinearni,
            odnosno tocka (x, y) lezi na pravcu.
            Jos treba provjeriti da je x koordinata unutar segmenta koji odredjuje duzinu.
            Radi zaokruzivanja na int bolje bi bilo dopustiti jos neku toleranciju koliko tocka moze biti udaljena od
            duzine, a da je jos uvijek smatramo dijelom duzine, ali ta tolerancija ovisi o pocetnoj i krajnjoj tocki i
            implementacija bi bila prekomplicirana.
        */
        int dx1 = pocetnaTocka.getX() - x;
        int dy1 = pocetnaTocka.getY() - y;
        int dx2 = zavrsnaTocka.getX() - x;
        int dy2 = zavrsnaTocka.getY() - y;

        int manjiX = Math.min(pocetnaTocka.getX(), zavrsnaTocka.getX());
        int veciX = Math.max(pocetnaTocka.getX(), zavrsnaTocka.getX());

        return Math.abs(dx1 * dy2 - dy1 * dx2) == 0 && x >= manjiX && x <= veciX;
    }


    @Override
    public void popuniLik(Slika slika) {
        int x1 = pocetnaTocka.getX();
        int y1 = pocetnaTocka.getY();
        int x2 = zavrsnaTocka.getX();
        int y2 = zavrsnaTocka.getY();

        // služi za provjeru da li točka koju pokušavamo nacrtati izlazi van dimenzija slike
        Pravokutnik ekran = new Pravokutnik(new Tocka(0, 0), slika.getSirina() - 1, slika.getVisina() - 1);

        if (x1 == x2) {
            for (int y = y1; y < y2; y++) {
                if(ekran.sadrziTocku(x1, y)) {
                    slika.upaliTocku(x1, y);
                }
            }
            return;
        }

        double nagibPravca = ((double)(y2 - y1)) / (x2 - x1);
        for (int x = x1; x < x2; x++) {
            int y = y1 + (int)(nagibPravca * x);
            if(ekran.sadrziTocku(x, y)) {
                slika.upaliTocku(x, y);
            }
        }
    }

    /**
     * Klasa implementira sučelje {@link StvarateljLika} koje sadrži
     * metodu - tvornicu linija iz danog stringa.
     */
    private static class StvarateljLinije implements StvarateljLika {
        @Override
        public String nazivLika() {
            return "LINIJA";
        }

        @Override
        public GeometrijskiLik stvoriIzStringa(String parametri) {
            try {
                Scanner scanner = new Scanner(parametri);
                Tocka pocetnaTocka = new Tocka(scanner.nextInt(), scanner.nextInt());
                Tocka zavrsnaTocka = new Tocka(scanner.nextInt(), scanner.nextInt());
                return new Linija(pocetnaTocka, zavrsnaTocka);
            } catch (Exception e) {
                throw new IllegalArgumentException("Problem prilikom citanja prametara.");
            }
        }
    }
}
