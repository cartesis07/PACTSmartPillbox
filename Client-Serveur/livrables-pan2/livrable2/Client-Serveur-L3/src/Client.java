import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;

/*Pour tester Client-Serveur-L3 : 
 * Ouvrir le serveur et le client dans deux workspaces différents
 * Renseigner l'adresse IP du serveur au client
 * Choisir un numéro de port
 * Démarrer le serveur en premier
 */

//On a bien testé Client-Serveur-L3 entre deux machines avec un lien wifi et une connexion Ethernet filiaire

public class Client {
	
	static String IP;
	static int port;

	public static void main(String[] args) {
		IP = "***.***.*.**"; //adresse IP du serveur
		port = 16041;

		final Socket clientSocket;
		final BufferedReader in;
		final PrintWriter out;
		@SuppressWarnings("resource")
		final Scanner sc = new Scanner(System.in);

		try {
			
			int number = Read.number("Questions.txt");
			
			/*
			 * port et adresse IP ou nom d'hôte
			 */
			clientSocket = new Socket(IP,port);

			out = new PrintWriter(clientSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			out.println("CLient L1 connecté");
			System.out.println("CLient L1 connecté");
			out.println("Port : " + port);
			System.out.println("Port : " + port);
			out.flush();

			Thread envoyer = new Thread(new Runnable() {
				String msg;
				String servor_msg;
				int ecart;
				@Override
				public void run() {
					for (int i = 0; i < number; i++) {
						msg = Read.read("Questions.txt", i);
						out.println(msg);
						out.flush();
						try {
							System.out.println("");
							servor_msg = in.readLine();
							if (servor_msg != null) {
								System.out.println(servor_msg);
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						//Vérifications des réponses du serveur
						switch(msg) {
						case "CLOSE":
							System.out.println("Communication terminée");
							System.exit(0);
							break;
						case "HOUR":
							String servor_seconds = servor_msg.substring(6, 8);
							String local_hour = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date());
							String local_seconds = local_hour.substring(6, 8);
							int servor_seconds_int = Integer.parseInt(servor_seconds);
							int local_seconds_int = Integer.parseInt(local_seconds);
							if (local_seconds_int < servor_seconds_int) {
								ecart = Math.abs(servor_seconds_int - local_seconds_int - 60);
							}
							else {
								ecart = local_seconds_int - servor_seconds_int;
							}
							System.out.println("Le temps de réception du message du serveur est de : " + ecart + " secondes");
							break;
						case "NUMBER":
							int number_servor = Integer.parseInt(servor_msg);
							if (number_servor != i + 1) {
								System.out.println("Le nombre de questions comptabilisées par le serveur et le client sont différentes");
							}
							else {
								System.out.println("Le nombre de questions comptabilisées par le serveur et le client sont identiques");
							}
							break;
						case "DATE":
							String local_date = DateFormat.getDateInstance(DateFormat.FULL).format(new Date());
							if (!(local_date.equals(servor_msg))) {
								System.out.println("Les dates du client et du serveur sont différentes");
							}
							else {
								System.out.println("Les dates du client et du serveur sont identiques");
							}
							break;
						default:
							break;
						}
					}

					while(true) {
						
					msg = sc.nextLine();
					if (msg.equals("CLOSE")) {
						out.println(msg);
						out.flush();
						System.exit(0);
					}
					out.println(msg);
					out.flush();
					}
				}
			});
			envoyer.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
}