import java.io.IOException;

//http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html
public class Main {

	public static void main(String[] args) throws IOException {		
		
		MultiThreadedServer server = new MultiThreadedServer(10020);
		new Thread(server).start();
		System.out.println("Serveur connecté");
	}

}