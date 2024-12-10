package org.example; //Declare the class's package

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose mode: 'cli' or 'gui'"); //Prompting the user to select between CLI and GUI modes
        String mode = scanner.nextLine().toLowerCase();

        if (mode.equals("cli")) { //Checking whether the user selected "cli"
            // Run the CLI logic
            new CLIApp().run(); //Craeting and launching CLIApp (CLI-based logic)
        } else if (mode.equals("gui")) {
            // Run the GUI logic
            MainApp.main(args); // Launching the JavaFx application (GUI-based logic)
        } else {
            System.out.println("Invalid mode. Please restart and choose 'cli' or 'gui'.");
        }
    }
}

