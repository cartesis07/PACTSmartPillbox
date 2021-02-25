package com.example.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class HomeActivity extends AppCompatActivity implements IPaddressDialog.IPaddressDialogListener {

    SocketWrapper socketWrapper;
    TextView logTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logTextView = new TextView(this);
        logTextView.setLines(3);
        logTextView.setGravity(Gravity.CENTER);
        logTextView.setText("Vous n'êtes pas connecté\nAppuyez sur le bouton Connexion");

        LinearLayout linearLayout = findViewById(R.id.ScrollViewLinearLayout);
        linearLayout.addView(logTextView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void configurationDesPrises(View view) {
        Intent intent = new Intent(this, WheelSelectorActivity.class) ;

        intent.putExtra("IpAddress", socketWrapper.ipAddress);
        intent.putExtra("Port", socketWrapper.port);
        startActivity(intent);
    }

    public void connexion(View v) {
        DialogFragment newFragment = new IPaddressDialog();
        newFragment.show(getSupportFragmentManager(), "Connexion");

    }

    @Override
    public void onDialogPositiveClick(IPaddressDialog dialog) {
        final String ipAddress = dialog.ipAddressEditText.getText().toString();
        final int port = Integer.parseInt(dialog.portEditText.getText().toString());

        SharedPreferences pref = getSharedPreferences("PillsListPreference", MODE_PRIVATE);

        socketWrapper = new SocketWrapper(ipAddress, port, logTextView, pref.edit(), true, null);

    }


}

