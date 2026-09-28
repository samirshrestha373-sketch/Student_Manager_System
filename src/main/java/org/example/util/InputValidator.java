package org.example.util;

import java.util.Scanner;

/**
 * Small helper around Scanner that keeps re-prompting until it gets
 * valid input, so the rest of the app never has to deal with
 * InputMismatchException or empty strings.
 */
public final class InputValidator {

    private InputValidator() {
    }

    public static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    public static double readDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public static String readNonEmptyString(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    /**
     * Reads a string but allows it to be left blank (used for optional/update fields).
     */
    public static String readOptionalString(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    public static double readDoubleInRange(Scanner sc, String prompt, double min, double max) {
        while (true) {
            double value = readDouble(sc, prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.printf("Please enter a value between %.1f and %.1f.%n", min, max);
        }
    }

    public static int readPositiveInt(Scanner sc, String prompt) {
        while (true) {
            int value = readInt(sc, prompt);

            if (value > 0) {
                return value;
            }

            System.out.println("Please enter a positive number.");
        }
    }

}
