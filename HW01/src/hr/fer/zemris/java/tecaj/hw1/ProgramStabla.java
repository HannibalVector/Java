package hr.fer.zemris.java.tecaj.hw1;

/**
 * Program ProgramStabla implementira osnovne funkcionalnosti za rad s binarnim stablima - metode za ubacivanje cvora u stablo, provjeru da li stablo sadrzi podatak, sortiranje, ispis i odredjivanje velicine stabla.
 * 
 * @author Ilijan Kotarac
 *
 */
public class ProgramStabla {
	
	/**
	 * Klasa implementira "strukturu" koja pohranjuje cvor binarnog stabla.
	 * @author Ilijan Kotarac
	 *
	 */
	static class CvorStabla {
        CvorStabla lijevi;
        CvorStabla desni;
        String podatak;
    }

	/**
	 * Metoda koja se poziva prilikom pokretanja programa.
	 * @param args	Argumenti iz komandne linije.
	 */
    public static void main(String[] args) {

        CvorStabla cvor = null;

        cvor = ubaci(cvor, "Jasna");
        cvor = ubaci(cvor, "Ana");
        cvor = ubaci(cvor, "Ivana");
        cvor = ubaci(cvor, "Anamarija");
        cvor = ubaci(cvor, "Vesna");
        cvor = ubaci(cvor, "Kristina");

        System.out.println("Ispisujem stablo inorder:");
        ispisiStablo(cvor);

        int vel = velicinaStabla(cvor);
        System.out.println("Stablo sadrzi elemenata: " + vel);

        boolean pronaden = sadrziPodatak(cvor, "Ivana");
        System.out.println("Trazeni podatak je pronaden: " + pronaden);
    }

    /**
     * Metoda provjerava sadrzava li dano binarno stablo dani podatak.
     * @param korijen	Korijen binarnog stabla u kojem metoda trazi podatak.
     * @param podatak	Podatak (tipa {@link String}) kojeg metoda trazi u stablu.
     * @return			{@code true} ako stablo sadrzava podatak, {@code false} ako stablo ne sadrzava podatak.
     */
    static boolean sadrziPodatak(CvorStabla korijen, String podatak) {
        if (korijen == null) {
            return false;
        }
        else {
            int usporedba = korijen.podatak.compareTo(podatak);
            if (usporedba == 0) {
                return true;
            } else if (usporedba > 0) {
                return sadrziPodatak(korijen.lijevi, podatak);
            } else {
                return sadrziPodatak(korijen.desni, podatak);
            }
        }
    }

    /**
     * Metoda racuna velicinu (broj cvorova) binarnog stabla s danim korijenom.
     * @param cvor	Korijen stabla ciju velicinu metoda racuna.
     * @return		Broj cvorova u stablu s danim korijenom.
     */
    static int velicinaStabla(CvorStabla cvor) {
        if(cvor == null) {
            return 0;
        }
        else {
            return 1 + velicinaStabla(cvor.lijevi) + velicinaStabla(cvor.desni);
        }
    }

    /**
     * Metoda ubacuje podatak u stablo. Metoda provjerava da li se podatak vec nalazi u stablu te ako nije, ubacuje ga na odgovarajuce mjesto. Podaci koji leksikografski prethode cvoru ubacuju se u lijevo dijete, a ako dolaze iza cvora, u desno dijete.
     * @param korijen	Korijen stabla u koje zelimo ubaciti podatak.
     * @param podatak	Podatak (tipa {@link String}) kojeg metoda ubacuje u stablo. 
     * @return			Korijen stabla u koje je ubacen podatak.
     */
    static CvorStabla ubaci(CvorStabla korijen, String podatak) {
        if(korijen == null) {
            CvorStabla noviCvor = new CvorStabla();
            noviCvor.podatak = podatak;
            return noviCvor;
        }
        else {
            int usporedba = korijen.podatak.compareTo(podatak);
            if (usporedba > 0) {
                korijen.lijevi = ubaci(korijen.lijevi, podatak);
            }
            else if (usporedba < 0) {
                korijen.desni = ubaci(korijen.desni, podatak);
            }
            return korijen;
        }
    }

    /**
     * Metoda ispisuje podatke iz danog binarnog stabla u leksikografskom poretku.
     * @param cvor	Korijen binarnog stabla cije podatke metoda ispisuje.
     */
    static void ispisiStablo(CvorStabla cvor) {
        if (cvor.lijevi != null) {
            ispisiStablo(cvor.lijevi);
        }
        System.out.println("\t" + cvor.podatak);
        if (cvor.desni != null) {
            ispisiStablo(cvor.desni);
        }
    }

}
