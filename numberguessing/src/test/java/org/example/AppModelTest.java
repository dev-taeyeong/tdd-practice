package org.example;

import org.example.stub.PositiveIntegerGeneratorStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AppModelTest {

    private static final String NEW_LINE = System.lineSeparator();

    @Test
    void sut_is_in_completed_when_it_is_initialized() {
        AppModel sut = new AppModel(new PositiveIntegerGeneratorStub(50));
        boolean actual = sut.isCompleted();
        assertThat(actual).isFalse();
    }

    @Test
    void sut_correctly_prints_select_mode_message() {
        AppModel sut = new AppModel(new PositiveIntegerGeneratorStub(50));
        String actual = sut.flushOutput();

        assertThat(actual).isEqualTo(
                "1: Single player game" + NEW_LINE +
                        "2: Multiplayer game" + NEW_LINE +
                        "3: Exit" + NEW_LINE +
                        "Enter selection: "
        );
    }

    @Test
    void sut_correctly_exits() {
        AppModel sut = new AppModel(new PositiveIntegerGeneratorStub(50));

        sut.processInput("3");

        boolean actual = sut.isCompleted();
        assertThat(actual).isTrue();
    }

    @Test
    void sut_correctly_prints_single_player_game_start_message() {
        AppModel sut = new AppModel(new PositiveIntegerGeneratorStub(50));
        sut.flushOutput();
        sut.processInput("1");

        String actual = sut.flushOutput();

        assertThat(actual).isEqualTo("Single player game" + NEW_LINE +
                "I'm thinking of a number between 1 and 100." + NEW_LINE +
                "Enter your guess: ");
    }

    @ParameterizedTest
    @CsvSource({"50, 40", "30, 29", "89, 9"})
    void sut_correctly_prints_too_low_message_in_single_player_game(int answer, int guess) {
        AppModel sut = new AppModel(new PositiveIntegerGeneratorStub(answer));
        sut.processInput("1");
        sut.flushOutput();
        sut.processInput(Integer.toString(guess));

        String actual = sut.flushOutput();

        assertThat(actual).isEqualTo("Your guess is too low." + NEW_LINE + "Enter your guess: ");
    }

    @ParameterizedTest
    @CsvSource({"50, 60", "80, 81"})
    void sut_correctly_prints_too_high_message_in_single_player_game(int answer, int guess) {
        AppModel sut = new AppModel(new PositiveIntegerGeneratorStub(answer));
        sut.processInput("1");
        sut.flushOutput();
        sut.processInput(Integer.toString(guess));

        String actual = sut.flushOutput();

        assertThat(actual).isEqualTo("Your guess is too high." + NEW_LINE + "Enter your guess: ");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 10, 100})
    void sut_correctly_prints_correct_message_in_single_player_game(int answer) {
        AppModel sut = new AppModel(new PositiveIntegerGeneratorStub(answer));
        sut.processInput("1");
        sut.flushOutput();
        int guess = answer;
        sut.processInput(Integer.toString(guess));

        String actual = sut.flushOutput();

        assertThat(actual).startsWith("Correct! ");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 100})
    void sut_correctly_prints_guess_count_if_single_player_game_finished(int fails) {
        AppModel sut = new AppModel(new PositiveIntegerGeneratorStub(50));
        sut.processInput("1");
        for (int i = 0; i < fails; i++) {
            sut.processInput("30");
        }
        sut.flushOutput();
        sut.processInput("50");

        String actual = sut.flushOutput();

        assertThat(actual).contains((fails + 1) + " guesses." + NEW_LINE);
    }

    @Test
    void sut_correctly_prints_one_guess_if_single_player_game_finished() {
        AppModel sut = new AppModel(new PositiveIntegerGeneratorStub(50));
        sut.processInput("1");
        sut.flushOutput();
        sut.processInput("50");

        String actual = sut.flushOutput();

        assertThat(actual).contains("1 guess.");
    }

    @Test
    void sut_prints_select_mode_message_if_single_player_game_finished() {
        AppModel sut = new AppModel(new PositiveIntegerGeneratorStub(50));
        sut.processInput("1");
        sut.flushOutput();
        sut.processInput("50");

        String actual = sut.flushOutput();

        assertThat(actual).endsWith(
                "1: Single player game" + NEW_LINE +
                        "2: Multiplayer game" + NEW_LINE +
                        "3: Exit" + NEW_LINE +
                        "Enter selection: "
        );
    }

    @Test
    void sut_returns_to_mode_selection_if_single_player_game_finished() {
        AppModel sut = new AppModel(new PositiveIntegerGeneratorStub(50));

        sut.processInput("1");
        sut.processInput("50");
        sut.processInput("3");

        boolean actual = sut.isCompleted();
        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = "100, 10, 1")
    void sut_generates_answer_for_each_game(String source) {
        int[] answers = Stream.of(source.split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();
        AppModel sut = new AppModel(new PositiveIntegerGeneratorStub(answers));
        for (int answer : answers) {
            sut.processInput("1");
            sut.flushOutput();
            sut.processInput(Integer.toString(answer));
        }

        String actual = sut.flushOutput();

        assertThat(actual).startsWith("Correct! ");
    }
}
