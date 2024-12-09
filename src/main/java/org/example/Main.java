package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose mode: 'cli' or 'gui'");
        String mode = scanner.nextLine().toLowerCase();

        if (mode.equals("cli")) {
            // Run the CLI logic
            new CLIApp().run();
        } else if (mode.equals("gui")) {
            // Run the GUI logic
            MainApp.main(args); // Launch JavaFX application
        } else {
            System.out.println("Invalid mode. Please restart and choose 'cli' or 'gui'.");
        }
    }
}

