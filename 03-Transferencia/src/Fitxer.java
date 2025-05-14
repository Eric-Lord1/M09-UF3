import java.io.File;
import java.nio.file.Files;

public class Fitxer {
    
    private String nom;
    private byte[] contingut;

    public Fitxer(String nom) {
        this.nom = nom;
    }

    public byte[] getContingut() {

        File file = new File(nom);
        
        if (!file.exists() || !file.isFile()) {
            System.out.println("Error llegint el fitxer del client: " + nom);
            return null;
        }

        try {    
            contingut = Files.readAllBytes(file.toPath());
            return contingut;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
