package com.example.application;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class IPaddressDialog extends DialogFragment {

    public interface IPaddressDialogListener {
        void onDialogPositiveClick(IPaddressDialog dialog);
    }
    IPaddressDialogListener listener;
    EditText ipAddressEditText, portEditText;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
            listener = (IPaddressDialogListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View popup = inflater.inflate(R.layout.activity_home_ip_popup, null);
        ipAddressEditText = popup.findViewById(R.id.connection_popup_ipadress);
        portEditText = popup.findViewById(R.id.connection_popup_port);

        builder.setView(popup);
        builder.setPositiveButton("Valider", (dialog, id) -> listener.onDialogPositiveClick(IPaddressDialog.this));
        builder.setNegativeButton("Annuler", null);
        return builder.create();
    }
}
