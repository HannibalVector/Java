package hr.fer.zemris.java.gui.calc;

import javax.swing.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Utility class holding definitions of various types of calculator buttons: buttons for entering digits, signs or
 * decimal point, buttons representing functions or operations, and other.
 * @author ilijan
 */
public class CalculatorButtons {
    /** Decimal point symbol. */
    private static final String DOT_SYMBOL = ".";
    /** Minus sign. */
    private static final String NEG_SIGN = "-";

    /**
     * General calculator button.
     */
    public static class CalculatorButton extends JButton {
        /** Status of the calculator. Offers access to screen, stack and other variables representing state of the
         *  calculator. */
        protected Calculator.CalculatorStatus status;

        /**
         * Constructor takes text to display on the button and {@link hr.fer.zemris.java.gui.calc.Calculator.CalculatorStatus} object representing status of
         * the calculator.
         * @param text  text to display on the button.
         * @param status    object representing status of the calculator.
         */
        public CalculatorButton(String text, Calculator.CalculatorStatus status) {
            super(text);
            this.status = status;
        }
    }

    /**
     * Button invokes function which maps real number (of type {@code double}) entered on calculator screen
     * to other real number (of type {@code double}).
     */
    public static class FunctionButton extends CalculatorButton {
        /** Real function. */
        private Function<Double, Double> function;
        /** Inverse of the function which is invoked if invert checkbox is checked. */
        private Function<Double, Double> inverseFunction;

        /**
         * Constructor takes symbol, function, its inverse and status object to initialize new {@code FunctionButton}.
         * @param symbol    symbol to display on the button.
         * @param function  real function to invoke on the number on screen.
         * @param inverseFunction   inverse of the given function.
         * @param status            status of the calculator.
         */
        public FunctionButton(String symbol,
                              Function<Double, Double> function,
                              Function<Double, Double> inverseFunction,
                              Calculator.CalculatorStatus status) {
            super(symbol, status);
            this.function = function;
            this.inverseFunction = inverseFunction;

            this.addActionListener(e -> {
                    try {
                        double number = Double.parseDouble(status.screen.getText());
                        Function<Double, Double> functionToUse =
                                status.inverseCheckBox.isSelected() ? inverseFunction : function;

                        double result = functionToUse.apply(number);
                        status.screen.setText(String.valueOf(result));
                        status.inputFinished = true;
                    } catch (Exception ex) {
                        status.screen.setText("Invalid operand.");
                        status.inputFinished = true;
                    }
                }
            );
        }
    }

    /**
     * Appends a digit on the screen if input is expected, or clears screen and starts new input if previous input was
     * finished.
     */
    public static class DigitButton extends CalculatorButton {
        /** String representation of digit to be added to number on screen. */
        private String digit;

        /**
         * Constructor initializes new {@code DigitButton} using digit symbol and calculator status.
         * @param digit     string representation of digit.
         * @param status    status of the calculator.
         */
        public DigitButton(String digit, Calculator.CalculatorStatus status) {
            super(digit, status);
            this.digit = digit;

            this.addActionListener(e ->
            {
                if (status.inputFinished) {
                    status.screen.setText("");
                    status.inputFinished = false;
                }
                status.screen.setText(status.screen.getText() + digit);
            });
        }
    }

    /**
     * Prints decimal point on calculator screen.
     */
    public static class DotButton extends CalculatorButton {
        /**
         * Initializes {@link DotButton} using calculator status.
         * @param status    status of the calculator.
         */
        public DotButton(Calculator.CalculatorStatus status) {
            super(DOT_SYMBOL, status);
            addActionListener(e -> {
                        if (status.inputFinished) {
                            status.screen.setText("0.");
                            status.inputFinished = false;
                            return;
                        }

                        if (status.screen.getText().contains(DOT_SYMBOL)) {
                            return;
                        }
                        status.screen.setText(status.screen.getText() + DOT_SYMBOL);
                    }
            );
        }
    }

    /**
     * Changes sign of the number printed on calculator screen.
     */
    public static class SignButton extends CalculatorButton {
        /**
         * Initializes {@link SignButton} using calculator status.
         * @param status    status of the calculator.
         */
        public SignButton(Calculator.CalculatorStatus status) {
            super("+/-", status);
            addActionListener(e -> {
                        if (status.screen.getText().startsWith(NEG_SIGN)) {
                            status.screen.setText(
                                    status.screen.getText().substring(1, status.screen.getText().length()));
                        } else {
                            status.screen.setText(NEG_SIGN + status.screen.getText());
                        }
                    }
            );
        }
    }

    /**
     * Finishes input and performs pending binary function or operation if present. If not, just finishes input.
     */
    public static class EqualsButton extends CalculatorButton {
        /**
         * Initializes {@link EqualsButton} using calculator status.
         * @param status    status of the calculator.
         */
        public EqualsButton(Calculator.CalculatorStatus status) {
            super("=", status);
            addActionListener(e -> {
                    try {
                        double number = Double.parseDouble(status.screen.getText());
                        if (status.nextOperation != null) {
                            double result = status.nextOperation.apply(status.firstOperand, number);
                            status.screen.setText(String.valueOf(result));
                            status.nextOperation = null;
                        }
                        status.inputFinished = true;
                    } catch (Exception ex) {
                        status.screen.setText("Invalid operand: " + status.screen.getText());
                        status.inputFinished = true;
                    }
                }
            );
        }
    }

    /**
     * Performs binary operation. Stores current number and operation to be performed in memory until
     * the second operand is entered.
     */
    public static class BinaryOperationButton extends CalculatorButton {
        /** Binary operation to be performed. */
        private BiFunction<Double, Double, Double> binaryOperation;

        /**
         * Initializes new {@code BinaryOperationButton} using operation symbol, function representing binary operation
         * and calculator status.
         * @param symbol    symbol to display on the button.
         * @param binaryOperation   function representing binary operation to be performed.
         * @param status    status of the calculator.
         */
        public BinaryOperationButton(String symbol,
                              BiFunction<Double, Double, Double> binaryOperation,
                              Calculator.CalculatorStatus status) {
            super(symbol, status);
            this.binaryOperation = binaryOperation;

            this.addActionListener(e ->
                    {
                        try {
                            double number = Double.parseDouble(status.screen.getText());
                            if (status.nextOperation == null) {
                                status.firstOperand = number;
                            } else {
                                status.firstOperand = status.nextOperation.apply(status.firstOperand, number);
                            }
                            status.nextOperation = binaryOperation;
                            status.inputFinished = true;
                        } catch (Exception ex) {
                            status.screen.setText("Invalid operand: " + status.screen.getText());
                            status.inputFinished = true;
                        }
                    }
            );
        }
    }

    /**
     * Performs binary operation. Stores current number and operation to be performed in memory until
     * the second operand is entered. The difference between this class and {@link BinaryOperationButton} is that
     * binary function can be inverted and expects the definition of the inverse binary function.
     */
    public static class BinaryFunctionButton extends CalculatorButton {
        /** Binary function to be invoked. */
        private BiFunction<Double, Double, Double> function;
        /** Inverse of the function which is invoked if invert checkbox is checked. */
        private BiFunction<Double, Double, Double> inverseFunction;

        /**
         * Initializes new {@code BinaryFunctionButton} using operation symbol, function definition, inverse function
         * definition and calculator status.
         * @param symbol    symbol to display on the button.
         * @param function  function to be calculated.
         * @param inverseFunction   inverse of the given function.
         * @param status    status of the calculator.
         */
        public BinaryFunctionButton(String symbol,
                              BiFunction<Double, Double, Double> function,
                              BiFunction<Double, Double, Double> inverseFunction,
                              Calculator.CalculatorStatus status) {
            super(symbol, status);
            this.function = function;
            this.inverseFunction = inverseFunction;

            this.addActionListener(e -> {
                    try {
                        double number = Double.parseDouble(status.screen.getText());
                        BiFunction<Double, Double, Double> functionToUse =
                                status.inverseCheckBox.isSelected() ? inverseFunction : function;

                        if (status.nextOperation == null) {
                            status.firstOperand = number;
                        } else {
                            status.firstOperand = status.nextOperation.apply(status.firstOperand, number);
                        }
                        status.nextOperation = functionToUse;
                        status.inputFinished = true;
                    } catch (Exception ex) {
                        status.screen.setText("Invalid operand: " + status.screen.getText());
                        status.inputFinished = true;
                    }
                }
            );
        }
    }

    /** Clears the screen. */
    public static class ClearButton extends CalculatorButton {
        /**
         * Initializes {@link ClearButton} using calculator status.
         * @param status    status of the calculator.
         */
        public ClearButton(Calculator.CalculatorStatus status) {
            super("clr", status);
            addActionListener(e -> {
                        status.screen.setText("0");
                        status.inputFinished = true;
                    }
            );
        }
    }

    /** Resets the calculator to the initial state. */
    public static class ResetButton extends CalculatorButton {
        /**
         * Initializes {@link ResetButton} using calculator status.
         * @param status    status of the calculator.
         */
        public ResetButton(Calculator.CalculatorStatus status) {
            super("res", status);
            addActionListener(e -> {
                        status.screen.setText("0");
                        status.inputFinished = true;
                        status.firstOperand = 0;
                        status.nextOperation = null;
                        status.inverseCheckBox.setSelected(false);
                    }
            );
        }
    }

    /**
     * Pushes number currently on screen to calculator stack.
     */
    public static class PushButton extends CalculatorButton {
        /**
         * Initializes {@link PushButton} using calculator status.
         * @param status    status of the calculator.
         */
        public PushButton(Calculator.CalculatorStatus status) {
            super("push", status);
            addActionListener(e -> {
                        status.stack.addFirst(Double.parseDouble(status.screen.getText()));
                        status.inputFinished = true;
                    }
            );
        }
    }

    /**
     * Pops number from calculator stack.
     */
    public static class PopButton extends CalculatorButton {
        /**
         * Initializes {@link PopButton} using calculator status.
         * @param status    status of the calculator.
         */
        public PopButton(Calculator.CalculatorStatus status) {
            super("pop", status);
            addActionListener(e -> {
                        if (status.stack.isEmpty()) {
                            status.screen.setText("Stack empty.");
                            return;
                        }
                        Double poppedValue = status.stack.pop();
                        status.screen.setText(String.valueOf(poppedValue));
                    }
            );
        }
    }
}
