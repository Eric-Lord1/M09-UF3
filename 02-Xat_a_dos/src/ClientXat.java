
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat {
    
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public void connecta() throws Exception {
        socket = new Socket("localhost", 9999);
        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();
        input = new ObjectInputStream(socket.getInputStream());

        System.out.println("Client connectat a localhost:9999 XD");
    }

    public void enviarMissatge(String msg) throws Exception {
        output.writeObject(msg);
        output.flush();
    }

    public void tancarClient() throws Exception {
        if (socket != null) socket.close();
        System.out.println("Client tancat.");
    }

    public static void main(String[] args) {

        ClientXat client = new ClientXat();
        Scanner scanner = new Scanner(System.in);

        try {

            client.connecta();

            System.out.print("Escriu el teu nom: ");
            String nom = scanner.nextLine();
            client.enviarMissatge(nom);

            Thread fil = new Thread(new FilLectorCX(client.input));
            fil.start();

            String missatge = "";

            while (!missatge.equals("sortir")) {
                System.out.print("Enviant missatge: ");
                missatge = scanner.nextLine();
                client.enviarMissatge(missatge);
            }

            fil.join();
            client.tancarClient();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
