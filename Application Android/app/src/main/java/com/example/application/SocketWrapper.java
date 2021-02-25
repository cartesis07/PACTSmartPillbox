package com.example.application;

import android.content.SharedPreferences;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class SocketWrapper { //Encapsule toutes les actions avec la socket. Doit être passé entre les activités et non réinstancié.
    private PrintWriter out;
    public BufferedReader in;
    TextView logTextView;
    SharedPreferences.Editor prefEdit;
    String ipAddress;
    int port;

    public SocketWrapper(final String ipAddress, final int port, TextView logTextView, SharedPreferences.Editor prefEdit, boolean receive, String message) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.logTextView = logTextView;
        this.prefEdit = prefEdit;
        // if receive boolean is set to True : we want the server to send its output
        new Thread(() -> {
                try {
                    Socket clientSocket = new Socket(ipAddress, port);
                    out = new PrintWriter(clientSocket.getOutputStream());
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    this.send("android");

                    if (receive) {
                        displaySocketMessage("Connexion réussie\nEn attente des données du serveur");
                        receivePillsListFromServer();

                    } else {
                        send(message);
                    }
                    clientSocket.close();
                } catch (Exception e) {
                    handlerSocketError(e);
                }
        }).start();
    }

    public void send(final String message) {
        //WARNING : this has to be used in a thread
            out.println(message);
            out.flush();
    }

    public String receive() throws IOException {
        return in.readLine();
    }

    public void handlerSocketError(Exception e) {
        String errorMessage;
        switch (e.getClass().getSimpleName()) {
            case "UnknownHostException":
            case "IllegalArgumentException":
                errorMessage = "Entrez à nouveau les informations";
                break;
            default:
                errorMessage = "Vérifiez que la CureBox ainsi que votre téléphone\nsont bien connécté à internet, puis recommencez";
        }
        displaySocketMessage("Erreur de Connexion\n" + errorMessage);
    }

    public void displaySocketMessage(String message) { // WARNING : logTextView has a maximum of lines
        logTextView.post(() -> {
                    logTextView.setText(message);
        });
    }


    public void receivePillsListFromServer() {
        //receive all the lines from the sever until an "end" line and put it in dataBuffer
        //This function is only called in SoketWrapper construtor : all of this actions are done in specific thread

        ArrayList<String> completeServerData = new ArrayList<String>();
        try {
            String received;
            while( !(received = receive()).equals("end") ) {
                displaySocketMessage(received);
                completeServerData.add(received);
            }

            ArrayList<PillsWrapper> pillsList = decodePillsListFromServerData(completeServerData);
            codeGsonFile(pillsList);
            displaySocketMessage("Les données ont étés reçues\nVous pouvez continuer");

        } catch (Exception e) {
            handlerSocketError(e);
        }
    }

    public ArrayList<PillsWrapper> decodePillsListFromServerData(ArrayList<String> completeServerData) {
        ArrayList<PillsWrapper> pillsList = new ArrayList<PillsWrapper>();
        for (int i = 0; i < completeServerData.size(); i++) {

            String[] currentPriseinfo = completeServerData.get(i).split(";");
            String currentPillName = currentPriseinfo[0];
            String currentPriseHour = currentPriseinfo[1];
            int currentNumberOfPillForPrise = Integer.parseInt(currentPriseinfo[2]);

            if (i <= 0 || !pillsList.get(pillsList.size() - 1).getPillName().equals(currentPillName))
                pillsList.add(new PillsWrapper(pillsList.size()));
            //according that the prise are sorted. if two prise are for the same pill, they are one after another.
            //if the names don't match (or it is the first line) : the two pills are different, so we create a new PillsWapper for it and fill it with the prise
            // (reminder : one line = one prise)

            pillsList.get(pillsList.size() - 1).addPrise(currentPriseHour, currentNumberOfPillForPrise);
            pillsList.get(pillsList.size() - 1).setPillName(currentPillName);

        }
        return pillsList;
    }

    public void codeGsonFile(ArrayList<PillsWrapper> pillsList) {
        String json = new Gson().toJson(pillsList);
        prefEdit.putString("PillsList", json);
        prefEdit.commit();
    }

}
