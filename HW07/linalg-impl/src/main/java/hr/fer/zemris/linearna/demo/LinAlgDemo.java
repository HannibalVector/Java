package hr.fer.zemris.linearna.demo;

import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Matrix;
import hr.fer.zemris.linearna.Vector;

/**
 * Demonstration application for package {@link hr.fer.zemris.linearna}.
 *
 * @author ilijan
 */
public class LinAlgDemo {

    /**
     * Main method for application {@link LinAlgDemo}.
     * @param args  command-line arguments. Not used anywhere.
     */
    public static void main(String[] args) {
        System.out.println("Baricentricne koordinate:");
        System.out.println("-------------------------");
        baricentricneKoordinate();

        System.out.println("\nRjesavanje sustava:");
        System.out.println("-------------------");
        rjesavanjeSustava();
    }

    /**
     * Method calculates barycentric coordinates for some triangle.
     */
    public static void baricentricneKoordinate() {
        IVector a = Vector.parseSimple("1 0 0");
        IVector b = Vector.parseSimple("5 0 0");
        IVector c = Vector.parseSimple("3 8 0");
        IVector t = Vector.parseSimple("3 4 0");

        double pov = b.nSub(a).nVectorProduct(c.nSub(a)).norm() / 2.0;
        double povA = b.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
        double povB = a.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
        double povC = a.nSub(t).nVectorProduct(b.nSub(t)).norm() / 2.0;
        double t1 = povA / pov;
        double t2 = povB / pov;
        double t3 = povC / pov;
        System.out.println(
                "Baricentricne koordinate su: ("
                        + t1 + ", " + t2 + ", " + t3 + ")."
        );
    }

    /**
     * Method solves some linear system.
     */
    public static void rjesavanjeSustava() {
        IMatrix A = Matrix.parseSimple("1.2 3.4 5.6 | 7.8 9.1 11.12" +
                "| 13.14 15.16 17.18");
        IMatrix b = Matrix.parseSimple("1 | 2 | 3");

        System.out.println("Matrica sustava: ");
        System.out.println(A);

        System.out.println("Vektor desne strane: ");
        System.out.println(b);

        System.out.println("Inverz matrice sustava: ");
        IMatrix A_inv = A.nInvert();
        System.out.println(A_inv);

        System.out.println("Rjesenje sustava je: ");
        System.out.println(A_inv.nMultiply(b));
    }
}
