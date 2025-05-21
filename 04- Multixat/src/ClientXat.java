import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat extends Thread {

    Socket socket;
    ObjectOutputStream output;
    ObjectInputStream input;
    boolean sortir;

    public void connecta() {
        try {
            socket = new Socket("localhost", 9999);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            System.out.println("Client connectat a localhost:9999");
            System.out.println("Flux d'entrada i sortida creat.");
        } catch (Exception e) {
            System.out.println("Error connectant amb el servidor.");
            e.printStackTrace();
            sortir = true;
        }
    }

    public void enviarMissatge(String msg) {
        try {
            output.writeObject(msg);
            output.flush();
            System.out.println("Enviant missatge: " + msg);
        } catch (Exception e) {
            System.out.println("Error enviant missatge.");
            sortir = true;
        }
    }

    public void tancarClient() {
        try {
            input.close();

            output.close();
            socket.close();
            System.out.println("Tancant client...");
        } catch (Exception e) {
            System.out.println("Error tancant el client.");
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("DEBUG: Iniciant rebuda de missatges...");

            while (!sortir) {
                String missatgeCru = (String) input.readObject();
                String codi = Missatge.getCodiMissatge(missatgeCru);
                String[] parts = Missatge.getPartsMissatge(missatgeCru);

                switch (codi) {
                    case Missatge.CODI_SORTIR_TOTS:
                        sortir = true;
                        break;

                    case Missatge.CODI_MSG_PERSONAL:
                        System.out.println("Missatge de (" + parts[1] + "): " + parts[2]);
                        break;

                    case Missatge.CODI_MSG_GRUP:
                        System.out.println(parts[1]);
                        break;

                    default:
                        System.out.println("Codi desconegut: " + codi);
                }
            }
        } catch (Exception e) {
            System.out.println("Error rebent missatge. Sortint...");
        } finally {
            tancarClient();
        }
    }

    public String getLinea(Scanner sc, String missatge, boolean obligatori) {
        
        String linia;

        System.out.print(missatge + " ");
        linia = sc.nextLine().trim();

        if (!obligatori || !linia.isEmpty()) {
            return linia;
        }

        return "Aquest camp és obligatori.";

    }

    public void mostrarAjuda() {
        System.out.println("---------------------");
        System.out.println("Comandes disponibles:");
        System.out.println("  1.- Conectar al servidor (primer pass obligatori)");
        System.out.println("  2.- Enviar missatge personal");
        System.out.println("  3.- Enviar missatge al grup");
        System.out.println("  4.- (o línia en blanc)-> Sortir del client");
        System.out.println("  5.- Finalitzar tothom");
        System.out.println("---------------------");
    }

    public static void main(String[] args) {
        ClientXat client = new ClientXat();
        client.connecta();

        Thread rebre = new Thread(() -> client.run());
        rebre.start();

        Scanner sc = new Scanner(System.in);
        client.mostrarAjuda();

        while (!client.sortir) {
            System.out.print("> ");
            String opcio = sc.nextLine();

            switch (opcio) {
                case "1":
                    System.out.print("Introdueix el nom: ");
                    String nom = sc.nextLine();
                    client.enviarMissatge(Missatge.getMissatgeConectar(nom));
                    break;

                case "2":
                    System.out.print("Destinatari: ");
                    String dest = sc.nextLine();
                    System.out.print("Missatge a enviar: ");
                    String missatgePers = sc.nextLine();
                    client.enviarMissatge(Missatge.getMissatgePersonal(dest, missatgePers));
                    break;

                case "3":
                    System.out.print("Missatge al grup: ");
                    String missatgeGrp = sc.nextLine();
                    client.enviarMissatge(Missatge.getMissatgeGrup(missatgeGrp));
                    break;

                case "4":
                    client.enviarMissatge(Missatge.getMissatgeSortirClient("Adéu"));
                    client.sortir = true;
                    break;

                case "5":
                    client.enviarMissatge(Missatge.getMissatgeSortirTots("Adéu"));
                    client.sortir = true;
                    break;

                default:
                    if (opcio.trim().isEmpty()) {
                        client.enviarMissatge(Missatge.getMissatgeSortirClient("Adéu"));
                        client.sortir = true;
                    } else {
                        System.out.println("Opció no vàlida.");
                        client.mostrarAjuda();
                    }
            }
        }

        client.tancarClient();
        sc.close();
    }

}
