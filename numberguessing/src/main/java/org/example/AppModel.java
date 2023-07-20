package org.example;

import java.util.stream.Stream;

public final class AppModel {

    private static final String NEW_LINE = System.lineSeparator();
    private static final String SELECT_MODE_MESSAGE =
            "1: Single player game" + NEW_LINE +
            "2: Multiplayer game" + NEW_LINE +
            "3: Exit" + NEW_LINE +
            "Enter selection: ";

    private final PositiveIntegerGenerator generator;
    private boolean completed;
    private StringBuffer outputBuffer;
    private Processor processor;

    private interface Processor {

        Processor run(String input);
    }

    public AppModel(PositiveIntegerGenerator generator) {
        this.generator = generator;
        completed = false;
        outputBuffer = new StringBuffer(SELECT_MODE_MESSAGE);
        processor = this::processModeSelection;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String flushOutput() {
        String output = outputBuffer.toString();
        outputBuffer.setLength(0);
        return output;
    }

    public void processInput(String input) {
        processor = processor.run(input);
    }

    private Processor processModeSelection(String input) {
        if (input.equals("1")) {
            println("Single player game");
            println("I'm thinking of a number between 1 and 100.");
            print("Enter your guess: ");
            int answer = generator.generateLessThanOrEqualToHundred();
            return getSinglePlayerGameProcessor(answer, 1);
        } else if (input.equals("2")) {
            println("Multiplayer game");
            print("Enter player names separated with commas: ");
            int answer = generator.generateLessThanOrEqualToHundred();
            return startMultiPlayerGame(answer);
        } else {
            completed = true;
            return null;
        }
    }

    private Processor getSinglePlayerGameProcessor(int answer, int tries) {
        return input -> {
            int guess = Integer.parseInt(input);
            if (guess < answer) {
                println("Your guess is too low.");
                print("Enter your guess: ");
                return getSinglePlayerGameProcessor(answer, tries + 1);
            } else if (guess > answer) {
                println("Your guess is too high.");
                print("Enter your guess: ");
                return getSinglePlayerGameProcessor(answer, tries + 1);
            } else {
                println("Correct! " + tries + (tries == 1 ? " guess." : " guesses."));
                print(SELECT_MODE_MESSAGE);
                return this::processModeSelection;
            }
        };
    }

    private Processor startMultiPlayerGame(int answer) {
        return input -> {
            Object[] players = Stream.of(input.split(",")).map(String::trim).toArray();
            print("I'm thinking of a number between 1 and 100.");
            return getMultiPlayerGameProcessor(players, answer, 1);
        };
    }

    private Processor getMultiPlayerGameProcessor(Object[] players, int answer, int tries) {
        Object player = players[(tries - 1) % players.length];
        outputBuffer.append("Enter ").append(player).append("'s guess: ");
        return input -> {
            int guess = Integer.parseInt(input);
            if (guess < answer) {
                println(player + "'s guess is too low.");
                return getMultiPlayerGameProcessor(players, answer, tries + 1);
            } else if (guess > answer) {
                println(player + "'s guess is too high.");
                return getMultiPlayerGameProcessor(players, answer, tries + 1);
            } else {
                println("Correct! " + player + " wins.");
                print(SELECT_MODE_MESSAGE);
                return this::processModeSelection;
            }
        };
    }

    private void println(String message) {
        outputBuffer.append(message).append(NEW_LINE);
    }

    private void print(String message) {
        outputBuffer.append(message);
    }
}
