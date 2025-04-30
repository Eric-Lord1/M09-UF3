import java.io.ObjectInputStream;

public class FilServidorXat extends Thread {

    private ObjectInputStream input;

    public FilServidorXat(ObjectInputStream input) {
        this.input = input;
    }

    @Override
    public void run() {
        try {
            
            String missatge = "";

            while (!missatge.equals("sortir")) {
                missatge = (String) input.readObject();
                System.out.println("Rebut: " + missatge);
            }
            System.out.println("Fil del xat finalitzat.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
