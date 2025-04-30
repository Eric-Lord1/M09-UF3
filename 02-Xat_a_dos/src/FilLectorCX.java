import java.io.ObjectInputStream;

public class FilLectorCX implements Runnable {
    private ObjectInputStream input;

    public FilLectorCX(ObjectInputStream input) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
