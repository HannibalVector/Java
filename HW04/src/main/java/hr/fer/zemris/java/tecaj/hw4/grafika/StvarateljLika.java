package hr.fer.zemris.java.tecaj.hw4.grafika;

/**
 * Sučelje zahtjeva implementaciju dviju metoda vezanih za naziv lika i
 * stvaranje novog geometrijskog lika iz stringa.
 * @author ilijan
 */
public interface StvarateljLika {
    /**
     * Vraća naziv geometrijskog lika koji implementira sučelje.
     * @return  naziv lika.
     */
    String nazivLika();

    /**
     * Stvara novi primjerak razreda koji implementira sučelje iz proslijeđenog stringa.
     * @param parametri {@code String} koji sadrži parametre za stvaranje novog primjerka
     *                                razreda koji implementira sučelje.
     * @return          novi primjerak razreda koji implementira sučelje.
     */
    GeometrijskiLik stvoriIzStringa(String parametri);
}
