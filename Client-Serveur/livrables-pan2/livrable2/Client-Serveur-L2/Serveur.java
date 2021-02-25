import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


/*Pour tester Client-Serveur-L1 : 
 * Ouvrir le serveur et le client dans deux workspaces différents
 * Renseigner l'adresse IP du serveur au client
 * Choisir un numéro de port
 * Démarrer le serveur en premier
 */

//On a bien testé Client-Serveur-L2 entre deux machines avec un lien wifi et une connexion Ethernet filiaire
//On peut obtenir l'adresse IP du serveur grâce à la la méthode getIP de la classe AdresseServeur

public class Serveur {
	
	static String IP;
	static int port;

	public static void main(String[] args) throws IOException {
		
		port = 16004;
		
		final ServerSocket serveurSocket  ;
		final Socket clientSocket ;
		final BufferedReader in ;
		final PrintWriter out;
		final Scanner sc = new Scanner(System.in);

		serveurSocket = new ServerSocket(port);
		clientSocket = serveurSocket.accept();
		out = new PrintWriter(clientSocket.getOutputStream());
		in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream()));
		IP = AdresseServeur.getIP();
		System.out.println("L'adresse IP du serveur est : " + IP);

		Thread envoi= new Thread(new Runnable() {
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
		envoi.start();
		Thread recevoir= new Thread(new Runnable() {
			String msg ;
			@Override
			public void run() {
				try {
					msg = in.readLine();

					//tant que le client est connecté
					while(msg!=null){
						System.out.println("Client : "+msg);
						msg = in.readLine();
					}
					System.out.println("Client déconnecté");
					out.close();
					in.close();
					sc.close();
					clientSocket.close();
					serveurSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		recevoir.start();
	}
}