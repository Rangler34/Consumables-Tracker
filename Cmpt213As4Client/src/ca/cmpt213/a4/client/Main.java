package ca.cmpt213.a4.client;

import ca.cmpt213.a4.client.view.GUI;

import javax.swing.*;

/**
 * Main is where the entire application is ran
 */
public class Main {

    /**
     * Where the application is run, constructs the GUI user interface
     *
     * @param args string array
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });


    }
}
