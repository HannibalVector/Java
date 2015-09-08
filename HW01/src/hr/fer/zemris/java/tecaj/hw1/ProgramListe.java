package hr.fer.zemris.java.tecaj.hw1;

/**
 * Program ProgramListe implementira osnovne funkcionalnosti za rad s vezanim listama - metode za ubacivanje cvora na pocetak liste, odredjivanje velicine liste, ispis liste i sortiranje liste.
 * 
 * @author Ilijan Kotarac
 *
 */
public class ProgramListe {
	
	/**
	 * Klasa implementira "strukturu" za pohranu cvora vezane liste.
	 * @author Ilijan Kotarac
	 *
	 */
	static class CvorListe {
        CvorListe sljedeci;
        String podatak;
    }

	/**
	 * Metoda koja se pokrece prilikom pokretanja programa.
	 * @param args	Argumenti iz komandne linije.
	 */
    public static void main(String[] args) {
        CvorListe cvor = null;
        
        cvor = ubaci(cvor, "Jasna");
        cvor = ubaci(cvor, "Ana");
        cvor = ubaci(cvor, "Ivana");

        System.out.println("Ispisujem listu uz originalni poredak:");
        ispisiListu(cvor);
        cvor = sortirajListu(cvor);
        System.out.println("Ispisujem listu nakon sortiranja:");
        ispisiListu(cvor);

        int vel = velicinaListe(cvor);
        System.out.println("Lista sadrzi elemenata: " + vel);
    }

    /**
     * Metoda racuna velicinu dane liste (broj cvorova u listi).
     * @param cvor	Pocetni cvor liste.
     * @return		Velicina dane liste (broj cvorova).
     */
    static int velicinaListe(CvorListe cvor) {
        CvorListe trenutniCvor = cvor;
        int velicinaListe = 0;
        while (trenutniCvor != null) {
            velicinaListe++;
            trenutniCvor = trenutniCvor.sljedeci;
        }

        return velicinaListe;
    }

    /**
     * Metoda ubacuje podatak u novi cvor na pocetak liste. Metoda ne provjerava sadrzi li vec lista dani podatak.
     * @param prvi		Pocetni cvor liste.
     * @param podatak	Podatak (tipa {@link String}) kojeg metoda ubacuje u listu.
     * @return			Pocetni cvor liste s ubacenim podatkom.
     */
    static CvorListe ubaci(CvorListe prvi, String podatak) {
        CvorListe noviCvor = new CvorListe();
        noviCvor.sljedeci = prvi;
        noviCvor.podatak = podatak;

        return noviCvor;
    }
    
    /**
     * Metoda ispisuje danu listu.
     * @param cvor	Pocetni cvor liste koju metoda ispisuje.
     */
    static void ispisiListu(CvorListe cvor) {
        numeriranoIspisiListu(cvor, 0);
    }

	static void numeriranoIspisiListu(CvorListe cvor, int brojPrethodnika) {
	    if (cvor != null) {
	        System.out.println(brojPrethodnika + 1 + ".\t" + cvor.podatak);
	        numeriranoIspisiListu(cvor.sljedeci, brojPrethodnika + 1);
	    }
	}

    /**
     * Metoda sortira listu u leksikografskom poretku.
     * @param cvor	Pocetni cvor liste.
     * @return		Pocetni cvor sortirane liste.
     */
    // implementira Merge Sort algoritam
    static CvorListe sortirajListu(CvorListe cvor) {
        int velicinaListe = velicinaListe(cvor);

        // base case
        if (velicinaListe <= 1) {
            return cvor;
        }

        // razbija listu na dvije liste velicine >= 1
        CvorListe prvaPolovica = cvor;
        CvorListe drugaPolovica = cvor;

        int prethodnikPolovice = velicinaListe/2 - 1;
        for(int i = 0; i < prethodnikPolovice; i++) {
            drugaPolovica = drugaPolovica.sljedeci;
        }
        CvorListe temp = drugaPolovica.sljedeci;
        drugaPolovica.sljedeci = null;
        drugaPolovica = temp;

        prvaPolovica = sortirajListu(prvaPolovica);
        drugaPolovica = sortirajListu(drugaPolovica);

        // merge lijeve i desne sortirane polovice
        CvorListe sortiranaLista;
        CvorListe trenutniCvorPrvePolovice = prvaPolovica;
        CvorListe trenutniCvorDrugePolovice = drugaPolovica;

        if (prvaPolovica.podatak.compareTo(drugaPolovica.podatak) < 0) {
            sortiranaLista = prvaPolovica;
            trenutniCvorPrvePolovice = prvaPolovica.sljedeci;
        } else {
            sortiranaLista = drugaPolovica;
            trenutniCvorDrugePolovice = drugaPolovica.sljedeci;
        }

        CvorListe trenutniCvorSortiraneListe = sortiranaLista;
        while (true) {
            if (trenutniCvorPrvePolovice == null) {
                trenutniCvorSortiraneListe.sljedeci = trenutniCvorDrugePolovice;
                break;
            }
            else if (trenutniCvorDrugePolovice == null) {
                trenutniCvorSortiraneListe.sljedeci = trenutniCvorPrvePolovice;
                break;
            }

            if (trenutniCvorPrvePolovice.podatak.compareTo(trenutniCvorDrugePolovice.podatak)<0) {
                trenutniCvorSortiraneListe.sljedeci = trenutniCvorPrvePolovice;
                trenutniCvorPrvePolovice = trenutniCvorPrvePolovice.sljedeci;
            }
            else {
                trenutniCvorSortiraneListe.sljedeci = trenutniCvorDrugePolovice;
                trenutniCvorDrugePolovice = trenutniCvorDrugePolovice.sljedeci;
            }
            trenutniCvorSortiraneListe = trenutniCvorSortiraneListe.sljedeci;
        }

        return sortiranaLista;
    }
}
