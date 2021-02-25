import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/*Pour tester Client-Serveur-L1 : 
 * Ouvrir le serveur et le client dans deux workspaces différents
 * Renseigner l'adresse IP du serveur au client
 * Choisir un numéro de port
 * Démarrer le serveur en premier
 */

//On a bien testé Client-Serveur-L2 entre deux machines avec un lien wifi et une connexion Ethernet filiaire

public class Client {
	
	static String IP;
	static int port;

	public static void main(String[] args) {
		IP = "***.***.*.**";
		port = 16000;

		final Socket clientSocket;
		final BufferedReader in;
		final PrintWriter out;
		final Scanner sc = new Scanner(System.in);

		try {
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
				@Override
				public void run() {
					while(true){
					msg = sc.nextLine();
					out.println(msg);
					out.flush();
					}
				}
			});
			envoyer.start();
			
			Thread recevoir = new Thread(new Runnable() {
				String msg;
				@Override
				public void run() {
					try {
						msg = in.readLine();
						while(msg!=null){
							System.out.println("Serveur : "+msg);
							msg = in.readLine();
						}
						System.out.println("Serveur déconnecté");
						out.close();
						in.close();
						sc.close();
						clientSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			recevoir.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}