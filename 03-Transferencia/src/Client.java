import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static final String HOST = "localhost";
    public static final int PORT = 7777;
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        try {

            Socket socket = new Socket(HOST, PORT);

            System.out.println("Client connectat a localhost:7777");

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());


            System.out.print("Introdueix el nom del fitxer a descarregar: ");
            String nomFitxer = scanner.nextLine();

            oos.writeObject(nomFitxer);

            byte[] contingut = (byte[]) ois.readObject();

            FileOutputStream fos = new FileOutputStream("rebut_" + nomFitxer);
            fos.write(contingut);

            fos.close();
            socket.close();
            scanner.close();

            System.out.println("Fitxer rebut i desat com a rebut_" + nomFitxer);

        } catch (Exception e) {
            System.err.println("Error al client: " + e.getMessage());
        }
    }
}
