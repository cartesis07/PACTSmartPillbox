import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Read {
	
	public static String read(String filename, int i) {
		FileReader in = null;
		BufferedReader br = null;
		String temporary = new String(); // on crée une variable temporaire qui va contenir une ligne du fichier texte
		try {
			in = new FileReader(filename);
			br = new BufferedReader(in); 
			for (int j = 0; j < i; j++) {
				br.readLine();
			}
			temporary = br.readLine();	
		} catch (FileNotFoundException e) {
			System.out.println("Le fichier demandé n'existe pas");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temporary;
	}
	
	public static int number(String filename){
		FileReader in = null;
		BufferedReader br = null;
		int compteur = 0;
		try {
			in = new FileReader(filename);
			br = new BufferedReader(in); 
			while (br.readLine() != null) {
				compteur++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("Le fichier demandé n'existe pas");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return compteur;
	}
}
