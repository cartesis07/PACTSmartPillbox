import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CsvFileHelper {
	
    public final static char SEPARATOR = ';';
    public static String filename = "CSV_test.csv";
	public static File myfile = getResource(filename);


	//getResourcePath permet d'obtenir le chamin d'un fichier
    public static String getResourcePath(String fileName) {
       final File f = new File("");
       final String dossierPath = f.getAbsolutePath() + File.separator + fileName;
       return dossierPath;
   }

    //getResource permet de créer le fichier File
   public static File getResource(String fileName) {
       final String completeFileName = getResourcePath(fileName);
       File file = new File(completeFileName);
       return file;
   }
   
   //permet de créer la liste des lignes
   public static ArrayList<String> readFile(File file) throws IOException {

       ArrayList<String> result = new ArrayList<String>();

       FileReader fr = new FileReader(file);
       BufferedReader br = new BufferedReader(fr);

       for (String line = br.readLine(); line != null; line = br.readLine()) {
           result.add(line);
       }

       br.close();
       fr.close();

       return result;
   }
   
	public static void main(String[] args) throws IOException {
		ArrayList<String> testList = readFile(myfile);
		for (int i = 0; i < testList.size(); i++) {
			System.out.println(testList.get(i));
		}
	}
	
	//ajouter une prise au fichier CSV
	public static void addLine(String ID, String HOUR, int repetition) throws IOException {
		BufferedWriter bf = new BufferedWriter(new FileWriter(myfile, true));
		bf.newLine();
		bf.write(ID + ";" + HOUR + ";" + repetition);
		bf.close();
	}
	
	public static void addLine_decrement_version(String new_line) throws IOException {
		BufferedWriter bf = new BufferedWriter(new FileWriter(myfile, true));
		bf.newLine();
		bf.write(new_line);
		bf.close();
	}
	
	//fonction permettant de modifier une ligne : à implémenter après
	public static void readReplace(String oldPattern,String replPattern, int lineNumber) {
        String line;
        StringBuffer sb = new StringBuffer();
        int nbLinesRead = 0;
        try {
            FileInputStream fis = new FileInputStream(myfile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            while ((line = reader.readLine()) != null) {
                nbLinesRead++;
                line = line.toLowerCase();
                if (nbLinesRead == lineNumber) {
                    line = line.replaceFirst(oldPattern.toLowerCase(),replPattern);
                }
                sb.append(line + "\n");
            }
            reader.close();
            BufferedWriter out = new BufferedWriter(new FileWriter(myfile));
            out.write(sb.toString());
            out.close();
        } catch (Exception e) {
        }
    }
	
	//attention on indente les lignes du fichiers entre 0 et n-1
	public static void deleteLine(int lineNumber) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(myfile)));
            StringBuffer sb = new StringBuffer(); 
            String line;
            int number = number_of_lines();
            int nbLinesRead = 0;       
            while ((line = reader.readLine()) != null) {
                if (nbLinesRead != lineNumber && nbLinesRead != number - 1) {
                    sb.append(line + "\n");
                }
                //permet de ne pas rajouter de ligne vide à la fin
                if (nbLinesRead != lineNumber && nbLinesRead == number - 1) {
                    sb.append(line);
                }
                nbLinesRead++;
            }
            reader.close();
            BufferedWriter out = new BufferedWriter(new FileWriter(myfile));
            out.write(sb.toString());
            out.close();
        } catch (Exception e) {
        }
    }
	
	//permet de gérer le cas où on supprime la dernière ligne du fichier
	public static void deleteLinereal(int lineNumber) throws IOException {
		if (lineNumber == number_of_lines() - 1) {
			deleteLine(lineNumber);
			deleteLine(lineNumber);
		}
		else {
			deleteLine(lineNumber);
		}
	}
	
	//trouve le numéro ligne qui correspond à l'ID donné (fonctionnel)
	public static int findLine(String ID) throws IOException {
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(myfile)));
        String line;
        for (int result = 0; result < number_of_lines(); result++) {
        	line = reader.readLine();
        	if (ID.equals(line.substring(0, 3))) {
                return result;
        	}
        }
        return -1;
	}
	
	//compte le nombre de lignes dans le fichier (fonctionnel)
	public static int number_of_lines() throws IOException {
		int result = 0;
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(myfile)));
		while (reader.readLine() != null) {
			result++;
		}
		return result;
	}
	
	public static String test() throws IOException{
		int number = number_of_lines();
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(myfile)));
		String Hour = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
		String line;
		for (int line_number = 0; line_number < number; line_number++) {
			line = reader.readLine();
			if (Hour.equals(line.substring(4,9))) {
				return line.substring(0,3);
			}
		}
		return "null";
	}

	public static int decrement(String ID) throws IOException {
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(myfile)));
		int line = findLine(ID);
		String line_to_change = null;
		for (int line_number = 0; line_number < line + 1; line_number++) {
			line_to_change = reader.readLine();
		}
		deleteLinereal(line);
		int length = line_to_change.length();
		int new_increment = Integer.parseInt(line_to_change.substring(10,length)) - 1;
		if (new_increment == 0) {
			return 0;
		}
		else {
			String new_line = (String) line_to_change.subSequence(0, 10) + String.valueOf(new_increment);
			addLine_decrement_version(new_line);
		}
		return 1;
	}
	
	//permet d'obtenir l'incrément correspondant à une ligne
	public static int get_increment(int line) throws IOException {
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(myfile)));
		String my_line = null;
		for (int line_number = 0; line_number < line + 1; line_number++){
			my_line = reader.readLine();
		}
		String result = null;
		int length = my_line.length();
		result = (String) my_line.subSequence(10, length);
		int result_int = Integer.parseInt(result);
		return result_int;
	}
	
	public static void refill() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(myfile)));
		int number = number_of_lines();
		String content = null;
		for (int j = 0; j < number; j++) {
			content = reader.readLine();
			if(get_increment(j) == 1 || get_increment(j) == 0) {
				System.out.println("Il faut recharger la boîte avec des pilules du médicament : " + content.substring(0, 3));
			}
		}
	}
}