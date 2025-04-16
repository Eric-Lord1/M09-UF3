import java.io.*;
import java.net.*;

public class Client {
    private static final int PORT = 7777;
    private static final String HOST = "localhost";

    private Socket socket;
    private PrintWriter out;

    public void connecta() {
        try {
            socket = new Socket(HOST, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.printf("Connectat a servidor en %s:%d\n", HOST, PORT);
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public void envia(String missatge) {
        if (out != null) {
            out.println(missatge);
            System.out.println("Enviat: " + missatge);
        }
    }

    public void tanca() {
        try {
            if (out != null) out.close();
            if (socket != null) socket.close();
            System.out.println("Connexió tancada.");
        } catch (IOException e) {
            System.err.println("Error en tancar connexió: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connecta();
        client.envia("Hola");
        client.envia("El barça guanyara la champions aquesta temporada");
        client.envia("I Raphina la pilota d'or");

        System.out.println("Prem enter per tancar...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.tanca();
    }
}
