package fr.ummisco.gamasenseit.app;

import java.util.Scanner;

public class Launcher {

    public static void main(String[] args) {
        try {
            App.main(args);
        } catch (Exception err) {
            err.printStackTrace();
            System.err.println("Press enter to close");
            new Scanner(System.in).nextLine();
        }
    }

}