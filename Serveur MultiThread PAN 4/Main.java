import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {		
		
		MultiThreadedServer server = new MultiThreadedServer(15003);
		new Thread(server).start();
		System.out.println("Serveur connecté");
	}

}