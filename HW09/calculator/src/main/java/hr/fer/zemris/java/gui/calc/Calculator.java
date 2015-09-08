package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.layouts.CalcLayout;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.function.BiFunction;

import static hr.fer.zemris.java.gui.calc.CalculatorButtons.*;

/**
 * Simple calculator. Input is expected using mouse through graphical user interface.
 * @author ilijan
 */
public class Calculator extends JFrame {

    /**
     *  Encapsulates calculator status. Offers access to calculator screen, invert button, stack,
     *  memory for the first operand in binary operations and binary functions, and memory
     *  for last invoked binary function or operation which awaits for input of the second operand.
     */
    public static class CalculatorStatus {
        /** Memory for the first operand in binary operations and binary functions. */
        double firstOperand;
        /** Last invoked binary function or operation which awaits for input of the second operand.
         *  {@code null} if no binary function or operation awaits for input of the second operand. */
        BiFunction<Double, Double, Double> nextOperation;
        /** Marks when input of number is finished. Entering a digit after the input is finished clears the screen. */
        boolean inputFinished;
        /** Stack offered to user to store inputs. */
        LinkedList<Double> stack;

        /** Calculator screen. */
        JLabel screen;
        /** Invert button used for inverting functions. */
        JCheckBox inverseCheckBox;

        /**
         * Constructor initializes calculator status using label used as screen and invert checkbox as input
         * parameters.
         * @param screen    calculator screen.
         * @param inverseCheckBox   invert checkbox.
         */
        public CalculatorStatus(JLabel screen, JCheckBox inverseCheckBox) {
            this.screen = screen;
            this.inverseCheckBox = inverseCheckBox;
            inputFinished = true;
            stack = new LinkedList<>();
        }
    }

    /**
     * Constructor initializes Calculator application.
     */
    public Calculator() {

        this.setTitle("Calculator");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        initGUI();
    }

    /**
     * Initializes Calculator GUI (configures size of window, main panel and defines functions for buttons and adds
     * them to the layout.
     */
    private void initGUI() {
        this.setLocation(100, 100);
        this.setSize(new Dimension(570, 270));

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        this.setContentPane(pane);

        LayoutManager2 calcLayout = new CalcLayout(5);
        pane.setLayout(calcLayout);
        pane.setBounds(10, 10, 600, 300);


        JLabel calcScreen = new JLabel("0");
        calcScreen.setOpaque(true);
        calcScreen.setBackground(Color.ORANGE);
        calcScreen.setHorizontalAlignment(SwingConstants.RIGHT);
        calcScreen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)));
        pane.add(calcScreen, "1,1");

        JCheckBox inverseCheckBox = new JCheckBox("Inv");
        pane.add(inverseCheckBox, "5,7");

        CalculatorStatus status = new CalculatorStatus(calcScreen, inverseCheckBox);

        pane.add(new DigitButton("0", status), "5,3");
        pane.add(new DigitButton("1", status), "4,3");
        pane.add(new DigitButton("2", status), "4,4");
        pane.add(new DigitButton("3", status), "4,5");
        pane.add(new DigitButton("4", status), "3,3");
        pane.add(new DigitButton("5", status), "3,4");
        pane.add(new DigitButton("6", status), "3,5");
        pane.add(new DigitButton("7", status), "2,3");
        pane.add(new DigitButton("8", status), "2,4");
        pane.add(new DigitButton("9", status), "2,5");


        pane.add(new DotButton(status), "5,5");
        pane.add(new SignButton(status), "5,4");

        pane.add(new FunctionButton("1/x", x -> 1 / x, x -> 1 / x, status), "2,1");
        pane.add(new FunctionButton("log", Math::log10, x -> Math.pow(10, x), status), "3,1");
        pane.add(new FunctionButton("ln", Math::log, Math::exp, status), "4,1");
        pane.add(new FunctionButton("sin", Math::sin, Math::asin, status), "2,2");
        pane.add(new FunctionButton("cos", Math::cos, Math::acos, status), "3,2");
        pane.add(new FunctionButton("tan", Math::tan, Math::atan, status), "4,2");
        pane.add(new FunctionButton("ctg", x -> 1 / Math.tan(x), x -> Math.atan(1 / x), status), "5,2");

        pane.add(new EqualsButton(status), "1,6");
        pane.add(new BinaryOperationButton("/", (x, y) -> x / y, status), "2,6");
        pane.add(new BinaryOperationButton("*", (x, y) -> x * y, status), "3,6");
        pane.add(new BinaryOperationButton("-", (x, y) -> x - y, status), "4,6");
        pane.add(new BinaryOperationButton("+", (x, y) -> x + y, status), "5,6");
        pane.add(new BinaryFunctionButton("x^n", (x, n) -> Math.pow(x, n), (x, n) -> Math.pow(x, 1 / n),
                status), "5,1");

        pane.add(new ClearButton(status), "1,7");
        pane.add(new ResetButton(status), "2,7");
        pane.add(new PushButton(status), "3,7");
        pane.add(new PopButton(status), "4,7");

        this.pack();
    }

    /**
     * The main method for Calculator application.
     * @param args  command-line arguments. Not used anywhere.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new Calculator();
            frame.setVisible(true);
        });
    }
}
