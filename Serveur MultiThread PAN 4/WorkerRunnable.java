import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**

 */
public class WorkerRunnable implements Runnable{

    protected Socket clientSocket = null;
    protected String serverText   = null;
	final Scanner sc = new Scanner(System.in);

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
    }

    public void run() {
        try {
			BufferedReader in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
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
				String first_msg;
				int length;
				@Override
				public void run() {
					try {
						first_msg = in.readLine();
						if (first_msg.equals("android")) {
							serverText = first_msg;
							System.out.println("Client : " + serverText);
							//tant que le client est connecté
							while(serverText != null){
								serverText = in.readLine();
								length = serverText.length();
								System.out.println(first_msg + " : " + serverText);
								//attention substring va de 0 à n-1 !!
									if (length == 9 && serverText.substring(0, 6).equals("delete")) {
										String line_to_delete_s = serverText.substring(6,9);
										int line = CsvFileHelper.findLine(line_to_delete_s);
										CsvFileHelper.deleteLinereal(line);
									}
									if (length > 5 && serverText.substring(0,3).contentEquals("add")) {
										CsvFileHelper.addLine(serverText.substring(3,6), serverText.substring(6,11), Integer.parseInt(serverText.substring(11,length)));
									}
							}
						}
						if (first_msg.equals("e")) {
							serverText = first_msg;
							System.out.println("Client : " + serverText);
							//tant que le client est connecté
							while(serverText != null){
								serverText = in.readLine();
								length = serverText.length();
								System.out.println(first_msg + " : " + serverText);
								if (serverText.contentEquals("p")) {
									System.out.println("ping reçu");
									String rps;
									if (CsvFileHelper.test().equals("null")) {
										rps = "0";
										out.println(rps);
										out.flush();
										System.out.println("Tu n'as rien à faire");
									}
									else {
										rps = "1";
										out.println(rps);
										out.flush();
										System.out.println("Délivrer le médicament");
										String response = "";
										while(!(response.equals("2"))) {
											System.out.println("Médicament pas encore pris");
											response = in.readLine();
										}
										System.out.println("Médicament délivré !");
										CsvFileHelper.decrement(CsvFileHelper.test());
									}
								}
							}
						}
						System.out.println(first_msg + " déconnecté");
						in.close();
						out.close();
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