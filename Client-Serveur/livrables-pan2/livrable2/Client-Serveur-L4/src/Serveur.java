import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;
import javax.imageio.ImageIO;


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
	static int port2;
	static int compteur = 0;
	
	 public static int fact (int n) {
	        if (n==0) return(1);
	        else return(n*fact(n-1));
	}
	 
	public static boolean isanInteger(String chaine) {
		try {
			Integer.parseInt(chaine);
		} catch (NumberFormatException e){
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws IOException {
		
		port = 16053;	
		port2 = 17009;
		final ServerSocket serveurSocket  ;
		final Socket clientSocket ;
		final BufferedReader in ;
		final PrintWriter out;
		final Scanner sc = new Scanner(System.in);

		serveurSocket = new ServerSocket(port);
		
		System.out.println("Serveur connecté");

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
//			String msg2 ;
			@Override
			public void run() {
				try {
					msg = in.readLine();
					//tant que le client est connecté
					while(msg!=null){
						msg = in.readLine();
						String toSend = "";
						switch(msg) {
						case "DATE":
							compteur++;
							toSend = DateFormat.getDateInstance(DateFormat.FULL).format(new Date());
							break;
						case "HOUR":
							compteur++;
							toSend = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date());
							break;
						case "NUMBER":
							compteur++;
							toSend = "" + compteur;
							break;
						case "CLOSE":
							System.out.println("Communication terminée");
							System.exit(0);
							break;
						default : 
							if (msg.length() > 10 && msg.subSequence(0, 10).equals("FACTORIAL.")) {
								compteur++;
								String num_str = (String) msg.subSequence(10, msg.length());
								int num_int = Integer.parseInt(num_str);
								int factorial = fact(num_int);
								toSend = Integer.toString(factorial);
							}

							if (msg.length() > 5 && msg.subSequence(0, 6).equals("IMAGE.")) {
								compteur++;
								try {
									ServerSocket serveurSocket2 = new ServerSocket(port2);
									out.println(msg);
									out.flush();
									Socket clientSocket2 = serveurSocket2.accept();
									String image_switch = (String) msg.subSequence(6,msg.length());
									switch(image_switch) {
									case "1":
										BufferedImage myPicture1 = ImageIO.read(new File("1.jpg"));
										ImageIO.write(myPicture1,"JPG",clientSocket2.getOutputStream());
										out.flush();
										serveurSocket2.close();
										clientSocket2.close();
										break;
									case "2":
										BufferedImage myPicture2 = ImageIO.read(new File("2.jpg"));
										ImageIO.write(myPicture2,"JPG",clientSocket2.getOutputStream());
										out.flush();
										serveurSocket2.close();
										clientSocket2.close();
										break;
									case "3":
										BufferedImage myPicture3 = ImageIO.read(new File("3.jpg"));
										ImageIO.write(myPicture3,"JPG",clientSocket2.getOutputStream());
										out.flush();
										serveurSocket2.close();
										clientSocket2.close();
										break;
									default:
										break;
									}
								}
								catch(SocketTimeoutException st)
								{
									System.out.println("Socket timed out!");
									break;
								}
								catch(IOException e)
								{
									e.printStackTrace();
									break;
								}	
								catch(Exception ex)
								{
									System.out.println(ex);
								}
							}
							break;
						}
						if (!(toSend.equals(""))) {
							out.println(toSend);
							out.flush();
						}
					}
					System.out.println("Client déconnecté");
					out.close();
					in.close();
					sc.close();
					clientSocket.close();
					serveurSocket.close();
					System.exit(0);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		recevoir.start();
	}
}
