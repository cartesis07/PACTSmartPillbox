package com.example.application;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PillsHandlerActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private String NomMedicament ;
    private ArrayList<PillsWrapper> pillsList;
    private PillsWrapper pillsWrapper;
    //private PillsWrapper Prises = new PillsWrapper(1);
    private String JoursRestants ;
    SharedPreferences pref;

    private String ipAdress;
    private int port;

    public String getNomMecicament() {
        return NomMedicament;
    }

    public PillsWrapper getPrises() {
        return pillsWrapper;
    }

    public String getJoursRestants() {
        return JoursRestants;
    }

    public void setNomMecicament(String nomMecicament) {
        NomMedicament = nomMecicament;
    }

    public void setPrises(PillsWrapper Prises) {
        this.pillsWrapper = Prises;
    }

    public void setJoursRestants(String joursRestants) {
        JoursRestants = joursRestants;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pills_handler);

        // Decode Json
        pref = getSharedPreferences("PillsListPreference", MODE_PRIVATE);
        String json = pref.getString("PillsList", "");
        Type listType = new TypeToken<ArrayList<PillsWrapper>>(){}.getType();
        pillsList = new Gson().fromJson(json, listType);
        pillsList.add(new PillsWrapper(pillsList.size()));
        pillsWrapper = pillsList.get(pillsList.size() -1);
        Log.i("d,zldzk", String.valueOf(pillsList.size()));

        //Socket informations
        Bundle extras = getIntent().getExtras();
        ipAdress = extras.getString("IpAddress", "wesh");
        port = extras.getInt("Port", 4);

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment() ;
                timePicker.show(getSupportFragmentManager(), "time picker") ;

                //new NumberPicker(getApplicationContext());

            }
        }) ;


        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v) {
                        showDatePickerDialog();
            }
        });


        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Valider();
            }
        });


        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Annuler();
            }
        });
    }

    private void Valider() {
           EditText editText = (EditText)findViewById(R.id.editText) ;
           String nomMedicament = editText.getText().toString() ;

           pillsWrapper.setPillName(nomMedicament);

           encodeJson(pref.edit());

           ArrayList<String> toSend = pillsWrapper.getPillsText();

           for(int i = 0; i < toSend.size(); i++) {
               SocketWrapper socketWrapper = new SocketWrapper(ipAdress, port, null, null, false, toSend.get(i));
           }

           Intent i = new Intent(this, WheelSelectorActivity.class);
           i.putExtra("IpAddress", ipAdress);
           i.putExtra("Port", port);

           startActivity(i);
           /*
        try
        {
            FileOutputStream fos = new FileOutputStream("doc.ser"); //il faut créer le fichier doc.ser !
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(pillsWrapper);
            oos.close();
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
           i.putExtra("NomMedicament", NomMedicament) ;
           i.putExtra("JoursRestants", JoursRestants) ;

            i.putExtra("IpAddress", ipAdress);
            i.putExtra("Port", port);

           setResult(RESULT_OK,i) ;
           startActivity(i) ;

            */
    }


    private void Annuler() {
            Intent i = new Intent(this, WheelSelectorActivity.class) ;
            setResult(RESULT_CANCELED,i);
            startActivity(i) ;
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show() ;

    }


    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hour = String.valueOf(hourOfDay);
        String min = String.valueOf(minute) ;

        pillsWrapper.addPrise(hour + ":"+min,1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Date date = new Date() ;
        String s = String.valueOf(dayOfMonth)+"/"+ String.valueOf(month) + "/" + String.valueOf(year) ;
        try {
            date =new SimpleDateFormat("dd/MM/yyyy").parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date currentDate = new Date() ;
        ;
        long diff = date. getTime() - currentDate. getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = (hours / 24) + 1;
        setJoursRestants(Long.toString(days));
    }


    public void encodeJson(SharedPreferences.Editor prefEdit) {
        prefEdit.clear();


        //Log.i("DATA_PILLSHANDLER", "before Gson" + pillsList.get(pillsList.size() - 1).getPillName() + " " + pillsList.size());

        String code = new Gson().toJson(pillsList);
        prefEdit.putString("PillsList", code);
        prefEdit.commit();

        /*
        String truc = pref.getString("PillsList", "");
        Type listType = new TypeToken<ArrayList<PillsWrapper>>(){}.getType();
        pillsList = new Gson().fromJson(truc, listType);

        Log.i("DATA_PILLSHANDLER", "after Gson" + pillsList.get(pillsList.size() - 1).getPillName() + " " + pillsList.size());

         */

    }

}

