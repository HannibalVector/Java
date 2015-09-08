package hr.fer.zemris.java.student0177035204.hw06;

import java.util.Scanner;

/**
 * Razred implementira osnovne funkcionalnosti za rad s cijelim
 * brojevima.
 * @author Ilijan
 */
public class CijeliBrojeviKonzola {
    public static void main(String[] args) {
        prompt();
    }

    private static void prompt() {
        System.out.print("Unesite broj > ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        switch (input) {
            case "quit":
                System.out.println("Hvala na druzenju.");
                System.exit(0);
                break;
            default:
                try {
                    int broj = Integer.parseInt(input);

                    System.out.println(
                            "Paran: " + odgovor(CijeliBrojevi.jeParan(broj))
                                    + ", neparan: " + odgovor(CijeliBrojevi.jeNeparan(broj))
                                    + ", prim: " + odgovor(CijeliBrojevi.jeProst(broj))
                    );
                    //System.out.println("Unio si broj " + broj);
                    prompt();
                } catch (Exception e) {
                    System.out.println("Nije unesen ispravan broj." +
                            " Ponovite unos ili unesite quit za kraj.");
                    prompt();
                }
            break;
        }
    }

    private static String odgovor(boolean istinosnaVrijednost) {
        if (istinosnaVrijednost) {
            return "DA";
        } else {
            return "NE";
        }
    }
}
