package hr.fer.zemris.java.student0177035204.hw07;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

/**
 * Program that allows the user to encrypt/decrypt given file using
 * the AES crypto-algorithm and the 128-bit encryption key
 * or to calculate and check the SHA-256 file digest.
 * Program expects 2 or 3 command-line arguments. First argument is name of command.
 * Supported commands are 'checksha', 'encrypt' and 'decrypt'.
 * If first argument is 'checksha', second argument should be filename for which
 * sha-256 digest is being checked.
 * If first argument is 'encrypt' or 'decrypt', second argument should be filename of
 * source file, and third argument should be filename of destination file.
 *
 * @author ilijan
 */
public class Crypto {
    /** Buffer size for buffered input and output. */
    private static final int BUFFER_SIZE = 4096;

    /**
     * The main method for program {@link Crypto}.
     * @param args  command-line arguments.
     */
    public static void main(String[] args) {
        checkNumberOfArguments(args, 2, 3);
        switch(args[0]) {
            case "checksha":
                checkNumberOfArguments(args, 2);
                checkSHA(args[1]);
                break;
            case "encrypt":
                checkNumberOfArguments(args, 3);
                encryptOrDecrypt(Cipher.ENCRYPT_MODE, args[1], args[2]);
                break;
            case "decrypt":
                checkNumberOfArguments(args, 3);
                encryptOrDecrypt(Cipher.DECRYPT_MODE, args[1], args[2]);
                break;
            default:
                System.out.println("Unsupported command.");
                break;
        }
    }

    /**
     * Checks if number of provided arguments is valid.
     * @param args  provided arguments.
     * @param lengths   valid lengths of arguments.
     */
    public static void checkNumberOfArguments(String[] args, int ... lengths) {
        for (int l : lengths) {
            if (args.length == l) {
                return;
            }
        }
        System.out.println("Wrong number of arguments.");
        System.exit(0);
    }

    /**
     * Checks sha-256 digest for provided file.
     * @param filename filename of file whose digest is checked.
     */
    private static void checkSHA(String filename) {
        String providedDigest = inputString("Please provide expected sha-256 digest for " + filename + ":");
        String calculatedDigest = getDigest(filename);

        System.out.print("Digesting completed.");
        if (providedDigest.equals(calculatedDigest)) {
            System.out.println(" Digest of " + filename + " matches expected digest.");
        } else {
            System.out.println(" Digest of " + filename + " does not match the expected digest.");
            System.out.println("Digest was: " + calculatedDigest);
        }
    }

    /**
     * Prints message to standard output, and prompts user for input from standard input.
     * @param message   message printed on standard output.
     * @return  string read from standard input.
     */
    private static String inputString(String message) {
        System.out.println(message);
        System.out.print("> ");
        Scanner sc = new Scanner(System.in);
        return sc.next();
    }

    /**
     * Gets string containing hexadecimal representation of digest for given file.
     * @param filename file for which digest is generated.
     * @return hexadecimal representation of digest.
     */
    private static String getDigest(String filename) {
        try (InputStream inputStream = new BufferedInputStream(
                new FileInputStream(filename), BUFFER_SIZE)
        ) {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            while (true) {
                byte[] buff = new byte[BUFFER_SIZE];
                int r = inputStream.read(buff, 0, BUFFER_SIZE);
                if (r < 1) {
                    break;
                }
                messageDigest.update(buff, 0, r);
            }

            byte[] digest = messageDigest.digest();
            return byteToHex(digest);

        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("No such algorithm.");
        } catch (IOException ex) {
            System.out.println("Cannot read from file.");
        }
        return "";
    }

    /**
     * Encrypts or decrypts input file and saves it to output file, depending
     * on provided mode.
     * @param cipherMode    cipher mode, should be {@code Cipher.ENCRYPT_MODE} for encryption,
     *                      or {@code Cipher.DECRYPT_MODE} for decryption.
     * @param inputFile     input (source) file.
     * @param outputFile    output (destination) file.
     */
    private static void encryptOrDecrypt(int cipherMode, String inputFile, String outputFile) {

        String keyText = inputString("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");
        String ivText = inputString("Please provide initialization vector as hex-encoded text (32 hex-digits):");

        try (InputStream inputStream = new BufferedInputStream(
                new FileInputStream(inputFile), BUFFER_SIZE);

             OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(outputFile), BUFFER_SIZE)
        ) {
            SecretKeySpec keySpec = new SecretKeySpec(hexToByte(keyText), "AES");
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(hexToByte(ivText));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(cipherMode, keySpec, paramSpec);

            while (true) {
                byte[] inputBuffer = new byte[BUFFER_SIZE];
                int r = inputStream.read(inputBuffer, 0, BUFFER_SIZE);
                if (r < BUFFER_SIZE) {
                    outputStream.write(cipher.doFinal(inputBuffer, 0, r));
                    break;
                }
                outputStream.write(cipher.update(inputBuffer));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return;
        }

        printMessageOnExit(cipherMode, inputFile, outputFile);
    }

    /**
     * Prints message on encryption/decryption completion.
     * @param cipherMode    cipher mode.
     * @param inputFile     input file.
     * @param outputFile    output file.
     */
    public static void printMessageOnExit(int cipherMode, String inputFile, String outputFile) {
        if (cipherMode == Cipher.ENCRYPT_MODE) {
            System.out.print("Encryption completed.");
        } else if (cipherMode == Cipher.DECRYPT_MODE) {
            System.out.print("Decryption completed.");
        }
        System.out.println(" Generated file " + outputFile + " based on file " + inputFile + ".");

    }

    /**
     * Converts string containing pairs of hexadecimal numbers to array of bytes.
     * @param hexString string containing pairs of hexadecimal numbers.
     * @return  byte array.
     */
    public static byte[] hexToByte(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Converts array of bytes to string containing pairs of hexadecimal numbers.
     * @param bytes byte array.
     * @return  string of pairs of hexadecimal numbers.
     */
    public static String byteToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
