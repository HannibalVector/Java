package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Application demonstrates usage of {@code ListModel<>} interface.
 * @author ilijan
 */
public class PrimDemo extends JFrame {
    /**
     * Initializes new window.
     */
    public PrimDemo() {
        this.setSize(new Dimension(600, 400));
        this.setTitle("PrimDemo");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initGUI();
    }

    /**
     *  Sets up layout and adds components to the main frame.
     */
    private void initGUI() {
        getContentPane().setLayout(new BorderLayout());

        PrimListModel model = new PrimListModel();

        JButton nextButton = new JButton("Sljedeći");
        nextButton.addActionListener(e -> model.next());
        getContentPane().add(nextButton, BorderLayout.PAGE_END);

        JList<Integer> lista1 = new JList<>(model);
        JList<Integer> lista2 = new JList<>(model);

        JPanel panelWithLists = new JPanel(new GridLayout(1, 0));
        panelWithLists.add(new JScrollPane(lista1));
        panelWithLists.add(new JScrollPane(lista2));

        getContentPane().add(panelWithLists, BorderLayout.CENTER);
    }

    /**
     * The main method for program {@link PrimDemo}.
     * @param args  command-line arguments. Not used anywhere.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new PrimDemo();
            frame.setVisible(true);
        });
    }

    /**
     *  Implements list model for the list of prime numbers which is populated
     *  incrementally.
     */
    public static class PrimListModel implements ListModel<Integer> {
        /** List of prime numbers. */
        private List<Integer> primeNumbers;
        /** List of listeners. */
        private List<ListDataListener> listeners;

        /**
         *  Initializes new {@link PrimListModel}. Initializes list of prime numbers
         *  to the list containing only number 1.
         */
        public PrimListModel() {
            primeNumbers = new ArrayList<>();
            primeNumbers.add(1);

            listeners = new ArrayList<>();
        }

        @Override
        public int getSize() {
            return primeNumbers.size();
        }

        @Override
        public Integer getElementAt(int index) {
            return primeNumbers.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            listeners.add(l);
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
        }

        /**
         *  Adds next prime number to the list and notifies listeners about change.
         */
        public void next() {
            if (primeNumbers.size() == 1) {
                primeNumbers.add(2);
                notifyListeners();
                return;
            } else if (primeNumbers.size() == 2) {
                primeNumbers.add(3);
                notifyListeners();
                return;
            }

            int nextPrimeCandidate = primeNumbers.get(primeNumbers.size() - 1) + 2;

outer:      while (true) {
                for (Integer prime : primeNumbers) {
                    if (prime > 1 && nextPrimeCandidate % prime == 0) {
                        nextPrimeCandidate += 2;
                        continue outer;
                    }
                }
                break;
            }
            primeNumbers.add(nextPrimeCandidate);
            notifyListeners();
        }

        /**
         * Notifies all listeners about the change of the last element in the list
         * of prime numbers.
         */
        private void notifyListeners() {
            for (ListDataListener l : listeners) {
                int newIndex = primeNumbers.size() - 1;
                l.contentsChanged(
                        new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, newIndex, newIndex));
            }
        }
    }
}
