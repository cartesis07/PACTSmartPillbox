package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class WheelSelectorActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PillsWrapper> pillsList;

    private SharedPreferences pref;

    public String ipAdress;
    public int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_handler);

        recyclerView = findViewById(R.id.wheel_recycler_view);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //pillsList = new ArrayList<>();
        /*
        for (int i = 0; i < 6; i++)
            pillsList.add(new PillsWrapper(i + 1)); //Le dernier numéro est le bouton "Ajouter une prise" voir WheelAdapter getItemViewCount
        pillsList.add(new PillsWrapper(10));
        pillsList.get(1).addPrise("12:05", 5);
        */

        // get pillsList from Gson's file
        pref = getSharedPreferences("PillsListPreference", MODE_PRIVATE);
        String json = pref.getString("PillsList", "");
        Type listType = new TypeToken<ArrayList<PillsWrapper>>(){}.getType();
        pillsList = new Gson().fromJson(json, listType);
        //if (pillsList == null) pillsList = new ArrayList<PillsWrapper>(); //just for debugging

        Log.i("DATA, WheelSelector", pillsList.get(pillsList.size() - 1).getPillName());

        //Socket informations
        Bundle extras = getIntent().getExtras();
        ipAdress = extras.getString("IpAddress", "wesh");
        port = extras.getInt("Port", 4);


        recyclerViewAdapter = new WheelAdapter(pillsList, this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public ArrayList<PillsWrapper> getPillsList() {
        return pillsList;
    }

    public void addPill(PillsWrapper pill) {
        pillsList.add(pill);
        refreshGson();
    }

    public void removePill(int position) {
        pillsList.remove(position);
        refreshGson();
    }

    public void refreshGson() {
        SharedPreferences.Editor prefEdit = pref.edit();
        prefEdit.clear();

        String json = new Gson().toJson(pillsList);
        prefEdit.putString("PillsList", json);
        prefEdit.commit();

    }

    public void startActivityAjoutNouveauMedicament(View v) {
        //addPill(new PillsWrapper(pillsList.size() + 1));
        Intent intent = new Intent(this, PillsHandlerActivity.class);

        intent.putExtra("IpAddress", ipAdress);
        intent.putExtra("Port", port);

        startActivity(intent);

        //Example of how to use send message
        //SocketWrapper socketWrapper = new SocketWrapper(ipAdress, port, null, null, false, "yolodd");
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String nomMedicament = data.getStringExtra("NomMedicament");
            String joursRestants = data.getStringExtra("JoursRestants");
            try {
                FileInputStream fis = new FileInputStream("doc.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);
                PillsWrapper prises = (PillsWrapper) ois.readObject();
                fis.close();
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //envoyer les sockets
    }

     */
}
