package hr.fer.zemris.java.tecaj.hw4.collections.demo;

import hr.fer.zemris.java.tecaj.hw4.collections.SimpleHashtable;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * @author ilijan
 */
public class SimpleHashtableIteratorDemo {
    public static void main(String[] args) {
        // create collection:
        SimpleHashtable examMarks = new SimpleHashtable(2);
        // fill data:
        examMarks.put("Ivana", Integer.valueOf(2));
        examMarks.put("Ante", Integer.valueOf(2));
        examMarks.put("Jasna", Integer.valueOf(2));
        examMarks.put("Kristina", Integer.valueOf(5));
        examMarks.put("Ivana", Integer.valueOf(5)); // overwrites old grade for Ivana
        examMarks.put("Shtefica", Integer.valueOf(7));

        for(SimpleHashtable.TableEntry pair : examMarks) {
            System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
        }

        System.out.println("\nKartezijev produkt:");

        for(SimpleHashtable.TableEntry pair1 : examMarks) {
            for(SimpleHashtable.TableEntry pair2 : examMarks) {
                System.out.printf(
                        "(%s => %d) - (%s => %d)%n",
                        pair1.getKey(), pair1.getValue(),
                        pair2.getKey(), pair2.getValue()
                        );
            }
        }

        Iterator<SimpleHashtable.TableEntry> iter = examMarks.iterator();
        while(iter.hasNext()) {
            SimpleHashtable.TableEntry pair = iter.next();
            System.out.println(pair.toString());
            if(pair.getKey().equals("Ivana")) {
                iter.remove();
            }
        }

        System.out.println(examMarks);

        try {
            Iterator<SimpleHashtable.TableEntry> iter2 = examMarks.iterator();
            while(iter2.hasNext()) {
                SimpleHashtable.TableEntry pair = iter2.next();
                if(pair.getKey().equals("Jasna")) {
                    iter2.remove();
                    iter2.remove();
                }
            }
        } catch (IllegalStateException e) {
            System.out.println("Bacena je iznimka " + e);
        }
        System.out.println(examMarks);

        try {
            Iterator<SimpleHashtable.TableEntry> iter3 = examMarks.iterator();
            while(iter3.hasNext()) {
                SimpleHashtable.TableEntry pair = iter3.next();
                if(pair.getKey().equals("Kristina")) {
                    examMarks.remove("Kristina");
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Bacena je iznimka " + e);
        }

        System.out.println(examMarks);
    }


}
