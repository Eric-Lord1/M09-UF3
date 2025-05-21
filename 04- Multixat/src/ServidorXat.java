import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class ServidorXat {

    public final static int PORT = 9999;
    public final static String HOST = "localhost";
    public final static String MSG_SORTIR = "sortir";

    public Hashtable<String, GestorClients> table = new Hashtable<>();
    public boolean sortir = false;
    public ServerSocket socket;

    public void servidorAEscoltar() throws Exception {
        socket = new ServerSocket(PORT);
        System.out.println("Servidor iniciat a " + HOST + ":" + PORT);

        Socket socketClient = socket.accept();
        System.out.println("Client connectat: " + socketClient.getInetAddress());

        GestorClients gestor = new GestorClients(socketClient, this);
        afegirClient(gestor);
        gestor.start();
    }

    public void pararServidor() throws Exception {
        socket.close();
        System.out.println("Servidor aturat.");
    }

    public void finalitzarXat() {
        enviarMissatgeGrup(Missatge.getMissatgeSortirTots("Tancant tots els clients."));
        table.clear();
        sortir = true;
    }

    public void eliminarClient(String nomClient) {
        if (table.containsKey(nomClient)) {
            table.remove(nomClient);
            System.out.println("Usuari eliminat: " + nomClient);
        } else {
            System.out.println("ERROR: Usuari no trobat");
        }
    }

    public void afegirClient(GestorClients client) {
        table.put(client.getNom(), client);
        enviarMissatgeGrup("Entra: " + client.getNom());
    }

    public void enviarMissatgeGrup(String missatge) {
        for (GestorClients client : table.values()) {
            client.enviarMissatge("Servidor", missatge);
        }
    }

    public void enviarMissatgePersonal(String destinatari, String remitent, String missatge) {
        GestorClients clientDestinatari = table.get(destinatari);
        if (clientDestinatari != null) {
            clientDestinatari.enviarMissatge(remitent, missatge);
        } else {
            System.out.println("ERROR: Destinatari " + destinatari + " no trobat.");
        }
    }

    public static void main(String[] args) throws Exception {
        ServidorXat servidor = new ServidorXat();

        servidor.socket = new ServerSocket(ServidorXat.PORT);
        System.out.println("Servidor iniciat a " + ServidorXat.HOST + ":" + ServidorXat.PORT);

        while (!servidor.sortir) {
            Socket socketClient = servidor.socket.accept();
            System.out.println("Client connectat: " + socketClient.getInetAddress());

            GestorClients gestor = new GestorClients(socketClient, servidor);
            gestor.start();
        }

        servidor.pararServidor();

    }

}