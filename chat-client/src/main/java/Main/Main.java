package Main;

import view.LoginView;

import javax.swing.SwingUtilities;

import controller.AuthController;
import network.Client;

public class Main { // Class

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Client client = new Client(); // Object
            LoginView view = new LoginView(); // Object
            new AuthController(view, client); // Object
        });
    }
}