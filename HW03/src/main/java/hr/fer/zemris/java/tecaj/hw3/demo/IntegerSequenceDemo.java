package hr.fer.zemris.java.tecaj.hw3.demo;

import hr.fer.zemris.java.tecaj.hw3.IntegerSequence;

/**
 * Demo program for class {@link IntegerSequence} which implements
 * {@code Iterable<Integer>} interface, and can supply iterators.
 * Created by ilijan on 4/1/15.
 */
public class IntegerSequenceDemo {

    public static void main(String[] args) {
        IntegerSequence range = new IntegerSequence(1, 11, 2);
        for(int i : range) {
            for(int j : range) {
                System.out.println("i="+i+", j="+j);
            }
        }

    }

}
