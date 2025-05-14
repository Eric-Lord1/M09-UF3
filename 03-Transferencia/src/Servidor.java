import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    private static final int PORT = 7777;
    private static final String HOST = "localhost";
    private ServerSocket serverSocket;
    private Socket socket;

    public Socket connectar() throws Exception {

        serverSocket = new ServerSocket(PORT);
        System.out.println("Acceptant connexions en -> " + HOST + ":" + PORT);
        System.out.println("Esperant connexió...");

        socket = serverSocket.accept();
        System.out.println("Connexió acceptada: " + socket.getInetAddress());
        return socket;

    }

    public void tancarConnexio(Socket socket) throws Exception {
        socket.close();
        System.out.println("Tancant connexió amb el client: " + socket.getInetAddress());
    }

    public void enviarFitxers() throws Exception {

        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

        String nomFitxer = (String) in.readObject();
        System.out.println("NomFitxer rebut: " + nomFitxer);

        Fitxer fitxer = new Fitxer(nomFitxer);
        byte[] contingut = fitxer.getContingut();

        if (contingut == null || contingut.length == 0) {
            System.out.println("Nom del fitxer buit o nul. Sortint...");
            out.writeObject(null);
            return;
        }

        System.out.println("Contingut del fitxer a enviar: " + contingut.length + " bytes");
        out.writeObject(contingut);
        out.flush();
        System.out.println("Fitxer enviat al client: " + nomFitxer);

    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        try {
            servidor.connectar();
            servidor.enviarFitxers();
            servidor.tancarConnexio(servidor.socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
