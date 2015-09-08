package hr.fer.zemris.java.tecaj.hw4.grafika.demo;

import hr.fer.zemris.java.tecaj.hw4.collections.SimpleHashtable;
import hr.fer.zemris.java.tecaj.hw4.grafika.*;
import hr.fer.zemris.java.tecaj_3.prikaz.PrikaznikSlike;
import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Demonstracijski program za crtanje geometrijskih likova na ekran.
 * Program očekuje tri ulazna parametra putem komandne linije. Prvi parametar je ime datoteke
 * u kojoj se nalazi popis geometrijskih likova koje želimo nacrtati
 * te parametri potrebni za njihovo stvaranje.
 * Drugi i treći argument su cijeli brojevi koji predstavljaju širinu i visinu
 * prozora za crtanje u pikselima.
 * @author ilijan
 */
public class Crtalo {

    /**
     * Metoda koja se poziva prilikom pokretanja programa {@code Crtalo}.
     * @param args  argumenti komandne linije.
     */
    public static void main(String[] args) {
        SimpleHashtable stvaratelji = podesi(Linija.class, Pravokutnik.class, Kvadrat.class, Elipsa.class, Kruznica.class);

        String[] definicije = new String[0];
        try {
            definicije = Files.readAllLines(
                    Paths.get(args[0]), StandardCharsets.UTF_8).toArray(new String[0]);
        } catch (IOException e) {
            System.out.println("Greska prilikom otvaranja datoteke.");
            e.printStackTrace();
            System.exit(-1);
        }

        GeometrijskiLik[] likovi = new GeometrijskiLik[definicije.length];

        try {
            for (int i = 0; i < definicije.length; i++) {
                Scanner scanner = new Scanner(definicije[i]);
                String lik = scanner.next();
                scanner.useDelimiter("$");
                String parametri = scanner.next();

                StvarateljLika stvaratelj = (StvarateljLika) stvaratelji.get(lik);
                likovi[i] = stvaratelj.stvoriIzStringa(parametri);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Program se prekida radi greske. Slika nece biti nacrtana.");
            e.printStackTrace();
            System.exit(-1);
        }

        try {
            Slika slika = new Slika(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            for (GeometrijskiLik lik : likovi) {
                lik.popuniLik(slika);
            }

            PrikaznikSlike.prikaziSliku(slika);
        } catch (Exception e) {
            System.out.println("Pogresan unos sirine i visine slike.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Metoda stvara hash tablicu tipa {@link SimpleHashtable} koja sadrži parove
     * podataka (ključ, vrijednost), gdje ključeve predstavljaju nazivi geometrijskih likova,
     * a vrijednosti su "reference" na metode koje stvaraju odgovarajući tip geometrijskog
     * lika iz stringa.
     * @param razredi   Razredi za koje se omogućuje stvaranje iz stringa.
     * @return          Hash tablica metoda-tvornica spremljenih po nazivima likova.
     */
    private static SimpleHashtable podesi(Class<?> ... razredi) {
        SimpleHashtable stvaratelji = new SimpleHashtable();
        for(Class<?> razred : razredi) {
            try {
                Field field = razred.getDeclaredField("STVARATELJ");
                StvarateljLika stvaratelj = (StvarateljLika)field.get(null);
                stvaratelji.put(stvaratelj.nazivLika(), stvaratelj);
            } catch(Exception e) {
                throw new RuntimeException(
                        "Nije moguće doći do stvaratelja za razred "
                                + razred.getName() + ".", e);
            }
        }
        return stvaratelji;
    }
}
