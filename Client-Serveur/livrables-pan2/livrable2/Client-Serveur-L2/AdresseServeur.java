import java.net.InetAddress;
import java.net.UnknownHostException;

//cette classe permet d'obtenir l'adresse IP et le nom de l'hôte du serveur rapidement

public class AdresseServeur {
	
    static String nomHote ;
    static String adresseIPLocale ;

	public static String getIP() {

        try{
           InetAddress inetadr = InetAddress.getLocalHost();
           //nom de machine
           nomHote = (String) inetadr.getHostName();
           System.out.println("Nom de la machine = " + nomHote );
           //adresse ip sur le réseau
           adresseIPLocale = (String) inetadr.getHostAddress();
           System.out.println("Adresse IP locale = " + adresseIPLocale );
        } catch (UnknownHostException e) {
               e.printStackTrace();
        }
		return adresseIPLocale;
    }
}