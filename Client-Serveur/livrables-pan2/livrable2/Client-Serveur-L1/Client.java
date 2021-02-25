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

public class Client {

	public static void main(String[] args) {
		final Socket clientSocket;
		final BufferedReader in;
		final PrintWriter out;
		final Scanner sc = new Scanner(System.in);

		try {
			/*
			 * port et adresse IP ou nom d'hôte
			 */
			clientSocket = new Socket("***.***.***.**",10002);
   
			out = new PrintWriter(clientSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
   
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