package hr.fer.zemris.java.student0177035204.hw06;

/**
 * Razred implementira osnovne funkcionalnosti za rad s cijelim
 * brojevima.
 * @author Ilijan
 */
public final class CijeliBrojevi {

	/**
	 * Skriveni prazni konstruktor za utility razred.
	 */
    private CijeliBrojevi() {
        // skrivanje konstruktora za utility razred
    }

    /**
     * Provjerava da li je broj neparan.
     * @param broj	broj za kojeg provjeravamo neparnost.
     * @return		{@code true} ako je broj neparan,
     * 				{@code false} inače.
     */
    public static boolean jeNeparan(int broj) {
        return !jeParan(broj);
    }

    /**
     * Provjerava da li je broj paran.
     * @param broj	broj za kojeg provjeravamo parnost.
     * @return		{@code true} ako je broj paran,
     * 				{@code false} inače.
     */
    public static boolean jeParan(int broj) {
        return broj % 2 == 0;
    }

    /**
     * Provjerava da li je broj prost.
     * @param broj	broj za kojeg provjeravamo je li prost.
     * @return		{@code true} ako je broj prost,
     * 				{@code false} inače.
     */
    public static boolean jeProst(int broj) {
        for (int i = 2; i < Math.sqrt(broj); i++) {
            if (broj % i == 0) {
                return false;
            }
        }
        return true;
    }
}
