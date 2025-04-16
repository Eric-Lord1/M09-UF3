import java.io.*;
import java.net.*;

public class Servidor {

    private static final int PORT = 7777;
    private static final String HOST = "localhost";

    private ServerSocket srvSocket;
    private Socket clientSocket;

    public void connecta() {
        try {
            srvSocket = new ServerSocket(PORT);
            clientSocket = srvSocket.accept();

            System.out.println("Client connectat :)");

        } catch (IOException e) {
            System.err.println("ERROR -> " + e.getMessage());
        }
    }

    public void repDades() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println("El client diu: " + line);
            }
            
            reader.close();
        } catch (IOException e) {
            System.err.println("ERROOOOOR: " + e.getMessage());
        }
    }

    public void tanca() {
        try {
            if (clientSocket != null) clientSocket.close();
            if (srvSocket != null) srvSocket.close();
            System.out.println("Connexions tancades.");
        } catch (IOException e) {
            System.err.println("Error en tancar connexions: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.connecta();
        servidor.repDades();
        servidor.tanca();
    }
}
