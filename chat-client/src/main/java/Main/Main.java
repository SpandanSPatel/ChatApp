package Main;

import view.LoginView;

import javax.swing.SwingUtilities;

import controller.AuthController;
import network.Client;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Client client = new Client();
            LoginView view = new LoginView();
            new AuthController(view, client);
        });
    }
}