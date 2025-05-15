import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GestorClients extends Thread {

    Socket socket;
    ServidorXat servidor;
    ObjectOutputStream output;
    ObjectInputStream input;
    String nom;

    public GestorClients(Socket socket, ServidorXat servidor) throws Exception {
        this.socket = socket;
        this.servidor = servidor;
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
    }

    public String getNom() {
        return nom;
    }

    public void enviarMissatge(String remitent, String msg) {
        try {
            String missatge = Missatge.getMissatgePersonal(remitent, msg);
            output.writeObject(missatge);
            output.flush();
        } catch (Exception e) {
            System.out.println("Error enviant missatge a " + nom);
        }
    }

    public void processaMissatge(String missatgeCru) {
        String codi = Missatge.getCodiMissatge(missatgeCru);
        String[] parts = Missatge.getPartsMissatge(missatgeCru);

        switch (codi) {
            case Missatge.CODI_CONECTAR:
                this.nom = parts[1];
                servidor.afegirClient(this);
                break;

            case Missatge.CODI_SORTIR_CLIENT:
                servidor.eliminarClient(nom);
                break;

            case Missatge.CODI_SORTIR_TOTS:
                servidor.finalitzarXat();
                break;

            case Missatge.CODI_MSG_PERSONAL:
                String destinatari = parts[1];
                String missatge = parts[2];
                servidor.enviarMissatgePersonal(destinatari, nom, missatge);
                break;

            case Missatge.CODI_MSG_GRUP:
                servidor.enviarMissatgeGrup(parts[1]);
                break;

            default:
                System.out.println("Codi desconegut: " + codi);
        }
    }

    @Override
    public void run() {
        try {
            while (!servidor.sortir) {
                String rawMsg = (String) input.readObject();
                processaMissatge(rawMsg);
            }
            socket.close();
            input.close();
            output.close();
        } catch (Exception e) {
            System.out.println("Error rebent missatge. Sortint...");
        }
    }

}
