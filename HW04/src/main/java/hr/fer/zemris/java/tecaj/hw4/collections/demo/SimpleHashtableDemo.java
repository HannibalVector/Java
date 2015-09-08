package hr.fer.zemris.java.tecaj.hw4.collections.demo;

import hr.fer.zemris.java.tecaj.hw4.collections.SimpleHashtable;

/**
 * @author ilijan
 */
public class SimpleHashtableDemo {

    public static void main(String[] args) {

        // create collection:
        SimpleHashtable examMarks = new SimpleHashtable(2);
        // fill data:
        examMarks.put("Ivana", Integer.valueOf(2));
        examMarks.put("Ante", Integer.valueOf(2));
        examMarks.put("Jasna", Integer.valueOf(3));
        System.out.println("Number of stored pairs: " + examMarks.size());
        examMarks.put("Kristina", Integer.valueOf(5));
        System.out.println(examMarks);

        // query collection:
        Integer ivanaGrade = (Integer)examMarks.get("Ivana");
        System.out.println("Ivana's exam grade is: " + ivanaGrade); // writes: 5

        examMarks.put("Ivana", Integer.valueOf(5)); // overwrites old grade for Ivana
        System.out.println(examMarks);

        // query collection:
        Integer kristinaGrade = (Integer)examMarks.get("Kristina");
        System.out.println("Kristina's exam grade is: " + kristinaGrade); // writes: 5
        // What is collection's size? Must be four!
        System.out.println("Number of stored pairs: " + examMarks.size()); // writes: 4

        System.out.println("Contains key Jasna? " + examMarks.containsKey("Jasna"));
        System.out.println("Contains key Pjer? " + examMarks.containsKey("Pjer"));

        examMarks.put("Mirko", Integer.valueOf(6));
        System.out.println(examMarks);
        System.out.println("Number of stored pairs: " + examMarks.size()); // writes: 4

        examMarks.remove("Jasna");
        System.out.println(examMarks);
        System.out.println("Number of stored pairs: " + examMarks.size()); // writes: 4

        examMarks.remove("Ivana");
        System.out.println(examMarks);
        System.out.println("Number of stored pairs: " + examMarks.size()); // writes: 4

        SimpleHashtable nova = new SimpleHashtable();
        System.out.println(nova.toString());



    }

}
