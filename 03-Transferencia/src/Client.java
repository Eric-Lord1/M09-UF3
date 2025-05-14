import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static String directori = "./";
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void connectar() throws Exception {
        socket = new Socket("localhost", 7777);
        System.out.println("Connectant a -> localhost:7777");
    }

    public void tancarConnexio() throws Exception {
        socket.close();
        System.out.println("Connexió tancada.");
    }

    public void rebreFitxers() throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Nom del fitxer a rebre ('sortir' per sortir): ");
        String name = scanner.nextLine();

        if ("sortir".equalsIgnoreCase(name)) {
            return;
        }

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        out.writeObject(name);
        out.flush();

        byte[] contingut = (byte[]) in.readObject();

        if (contingut == null) {
            System.out.println("Error: fitxer rebut és nul.");
            return;
        }

        String nomDestinacio = directori + new File(name).getName();
        FileOutputStream fos = new FileOutputStream(nomDestinacio);
        fos.write(contingut);
        fos.close();

        System.out.println("Fitxer rebut i guardat com: " + nomDestinacio);
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.connectar();
            client.rebreFitxers();
            client.tancarConnexio();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
