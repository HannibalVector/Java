package hr.fer.zemris.java.tecaj.hw3.demo;

import hr.fer.zemris.java.tecaj.hw3.CString;

/**
 * Demo program for class {@link CString}.
 * Created by ilijan on 3/31/15.
 */
public class CStringDemo {

    public static void main(String[] args) {
        CString abcd = new CString("A, b, c, d, maca prede.");
        CString end = new CString("prede.");
        CString start = new CString("AB");
        CString nesto = new CString("poglavnik");
        CString svasta = new CString("pijani ");

        CString novi = new CString(nesto.substring(0, 5));
        System.out.println(novi);

        System.out.println(abcd.charAt(5));
        System.out.println(abcd.startsWith(start));
        System.out.println(abcd.endsWith(end));
        System.out.println(abcd.contains(nesto));

        System.out.println("abcd: " + abcd.length());
        System.out.println(abcd.substring(4, 9));
        System.out.println(svasta.add(nesto));
        System.out.println(svasta.length());
        System.out.println(nesto.length());
        System.out.println(svasta.add(nesto).length());
        System.out.println(abcd.replaceAll('a', 'x'));
        System.out.println(abcd.replaceAll(new CString("maca"), new CString("Hitler")));
    }
}
