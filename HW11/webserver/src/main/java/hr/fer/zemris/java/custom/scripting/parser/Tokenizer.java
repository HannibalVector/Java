package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.scripting.tokens.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Takes string and generates array of tokens which represent expressions used in tags.
 */
public class Tokenizer {
    private CharReader reader;
    private Token[] tokens;

    private final String spaces = " \t\r\n";
    private final String operators = "+-*/%";
    private final char stringOpener = '\"';
    private final char functionOpener = '@';
    private final String specialCharacters = spaces + operators + stringOpener + functionOpener;


    public Tokenizer(String string) {
        reader = new CharReader(string);
        reader.setEscapedCharacters("\"\\");
    }

    public Token[] getTokens() {
        if(tokens == null) {
            tokens = tokenize();
        }
        return tokens;
    }

    public Token[] tokenize() {
        List<Token> tokensCollection = new ArrayList<>();

        reader.skipSpaces();
        while(!reader.isEndOfString()) {
            tokensCollection.add(nextToken());
            reader.skipSpaces();
        }

        int numberOfTokens = tokensCollection.size();
        Token[] tokens = new Token[numberOfTokens];

        for(int i = 0; i < numberOfTokens; i++) {
            tokens[i] = tokensCollection.get(i);
        }

        return tokens;
    }

    private Token nextToken() {

        if(isCurrentNumber()) {
            return getNextNumber();
        }

        if(isCurrentOperator()) {
            return new TokenOperator(String.valueOf(reader.read()));
        }

        if(isCurrentFunction()) {
            return getNextFunction();
        }

        if(isCurrentString()) {
            return getNextString();
        }

        return getNextVariable();
    }

    private String getNextVariableName() {
        StringBuilder nextVariableBuilder = new StringBuilder();
        while(!reader.isEndOfString() && !isCurrentSpecialCharacter()) {
            nextVariableBuilder.append(reader.read());
        }

        String variableName = nextVariableBuilder.toString();

        if (variableName.isEmpty()) {
            throw new InvalidVariableNameException();
        }
        int variableNameLength = variableName.length();
        for (int i = 0; i < variableNameLength; i++) {
            char currentChar = variableName.charAt(i);
            if (!Character.isLetterOrDigit(currentChar) && currentChar != '_') {
                throw new InvalidVariableNameException();
            }
        }

        if (!Character.isLetter(variableName.charAt(0))) {
            throw new InvalidVariableNameException();
        }

        return variableName;
    }

    private TokenVariable getNextVariable() {
        return new TokenVariable(getNextVariableName());
    }

    private Token getNextString() {
        reader.skip(); // removes " from the beginning of string
        String string;

        try {
            string = reader.readCarefullyUntil("\"");
            reader.skip();
        } catch (CharReaderException e) {
            throw new StringNotClosedException();
        }
        return new TokenString(string);
    }

    private Token getNextFunction() {
        reader.skip();
        return new TokenFunction(functionOpener + getNextVariableName());
    }

    private Token getNextNumber() {
        StringBuilder nextNumberBuilder = new StringBuilder();

        nextNumberBuilder.append(reader.read());
        boolean hasDecimalPoint = false;

        while(!reader.isEndOfString() &&
                (Character.isDigit(reader.current()) || (reader.current() == '.' && !hasDecimalPoint))) {
            if (reader.current() == '.') {
                hasDecimalPoint = true;
            }
            nextNumberBuilder.append(reader.read());
        }

        if (hasDecimalPoint) {
            return new TokenConstantDouble(Double.parseDouble(nextNumberBuilder.toString()));
        } else {
            return new TokenConstantInteger(Integer.parseInt(nextNumberBuilder.toString()));
        }
    }

    private boolean isCurrentOperator() {
        return operators.contains(String.valueOf(reader.current()));
    }

    private boolean isCurrentString() {
        return reader.current() == stringOpener;
    }

    private boolean isCurrentFunction() {
        return reader.current() == functionOpener;
    }

    private boolean isCurrentSpecialCharacter() {
        return specialCharacters.contains(String.valueOf(reader.current()));
    }

    private boolean isCurrentNumber() {
        if(Character.isDigit(reader.current())) {
            return true;
        }
        if (reader.current() == '-'  && reader.hasNext() && Character.isDigit(reader.nextChar())) {
            return true;
        }
        return false;
    }
}
