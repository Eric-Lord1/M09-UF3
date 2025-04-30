import java.io.*;
import java.net.*;

public class ServidorXat {

    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    private static final String MSG_SORTIR = "sortir";

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public void iniciarServidor() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciat a " + HOST + ":" + PORT);
    }

    public void pararServidor() throws IOException {
        if (serverSocket != null)
            serverSocket.close();
        System.out.println("El servidor ha tancat la connexió.");
    }

    public String getNom(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream lector = new ObjectInputStream(inputStream);
        return (String) lector.readObject();
    }

    public static void main(String[] args) {

        ServidorXat servidor = new ServidorXat();

        try {

            servidor.iniciarServidor();
            servidor.clientSocket = servidor.serverSocket.accept();
            System.out.println("Client connectat: " + servidor.clientSocket.getInetAddress());

            InputStream rawInput = servidor.clientSocket.getInputStream();
            OutputStream rawOutput = servidor.clientSocket.getOutputStream();

            servidor.output = new ObjectOutputStream(rawOutput);
            servidor.output.flush();

            servidor.input = new ObjectInputStream(rawInput);
            String nom = (String) servidor.input.readObject();

            System.out.println("Nom rebut: " + nom);

            Thread fil = new Thread(new FilServidorXat(servidor.input));
            fil.start();

            BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));
            String missatge = "";

            while (!missatge.equals(MSG_SORTIR)) {
                System.out.print("Missatge ('sortir' per tancar): ");
                missatge = consola.readLine();
                servidor.output.writeObject(missatge);
                servidor.output.flush();
            }

            fil.join();
            servidor.clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                servidor.pararServidor();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
