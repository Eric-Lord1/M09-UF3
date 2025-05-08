import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static final int PORT = 7777;

    public static void main(String[] args) {

        try {

            ServerSocket serverSocket = new ServerSocket(PORT);

            System.out.println("Servidor en marxa a localhost:7777");
            System.out.println("Esperant conexions...");

            Socket socket = serverSocket.accept();

            System.out.println("Client connectat ueee");

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());


            String nomFitxer = (String) ois.readObject();
            System.out.println("Fitxer demanat pel client: " + nomFitxer);

            Fitxer fitxer = new Fitxer(nomFitxer);
            byte[] contingut = fitxer.getContingut();

            oos.writeObject(contingut);
            serverSocket.close();

            System.out.println("Fitxer enviat correctament.");

        } catch (Exception e) {
            System.err.println("Error al servidor: " + e.getMessage());
        }
    }
}
