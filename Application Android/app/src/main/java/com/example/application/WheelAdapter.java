package com.example.application;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WheelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<PillsWrapper> listeDesRoues;
    private WheelSelectorActivity wheelSelectorActivity;

    public WheelAdapter(ArrayList<PillsWrapper> dataSet, WheelSelectorActivity wheelSelectorActivity) {
        this.listeDesRoues = dataSet;
        this.wheelSelectorActivity = wheelSelectorActivity; //reférence sur l'ancienne activité pour le mapping des boutons
    }

    public static class WheelViewHolder extends RecyclerView.ViewHolder {

        public TextView wheelNumber, pillName, numberOfPill, dateDeFin, dateDeRemplissage, prises;
        public Button extendButton, modifButton, supprButton;
        public LinearLayout extendedLayout, wholeLayout;
        public WheelViewHolder(View v) {
            super(v);
            wholeLayout = v.findViewById(R.id.wheelrecyclerview_whole_layout);
            wheelNumber = v.findViewById(R.id.wheelrecylerview_wheel_number);
            pillName = v.findViewById(R.id.wheelrecylerview_pill_name);
            extendButton = v.findViewById(R.id.wheelrecyclerview_extend_button);
            numberOfPill = v.findViewById(R.id.wheelrecyclerview_number_of_pill);
            dateDeFin = v.findViewById(R.id.wheelrecylerview_date_de_fin);
            dateDeRemplissage = v.findViewById(R.id.wheelrecylerview_date_de_remplissage);
            extendedLayout = v.findViewById(R.id.wheelrecyclerview_extended_layout);
            prises = v.findViewById(R.id.wheelrecyclerview_listesdesprises);
            modifButton = v.findViewById(R.id.wheelrecyclerview_modif_button);
            supprButton = v.findViewById(R.id.wheelrecyclerview_suppr_button);
        }
    }

    public static class ButtonViewHolder extends RecyclerView.ViewHolder {

        public Button button;
        public ButtonViewHolder(View v) {
            super(v);
            button = v.findViewById(R.id.wheelrecyclerview_button);
        }
    }

    @Override
    public int getItemViewType(int position) { return position; } //nécéssaire pour onCreateViewHolder qui prends int ViewType et non position

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        Log.i("CREATE", "position " + position + " sur " + listeDesRoues.size());

        if (position == listeDesRoues.size()) { // le boutton est en dernière position. Il n'est pas présent dans la listeDesRoues (voir getItemcount)
            View buttonLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_button_layout, parent, false);
            return new ButtonViewHolder(buttonLayoutView);
        }

        View wheelExtendedLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_wheel_card_layout, parent, false);
        return new WheelViewHolder(wheelExtendedLayoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i("BINDING", "position " + position + " sur " + listeDesRoues.size());

        if (position == listeDesRoues.size()) {
            ButtonViewHolder bvh = (ButtonViewHolder)holder;
            return; //Si c'est un bouton, on stop la fonction
        }


        PillsWrapper pillsWrapper = listeDesRoues.get(position);
        WheelViewHolder wvh = (WheelViewHolder)holder;

        wvh.wheelNumber.setText(String.valueOf(pillsWrapper.getWheelNumber()));
        wvh.pillName.setText(pillsWrapper.getPillName());
        wvh.extendButton.setText("+");
        wvh.numberOfPill.setText("Reste\n" + pillsWrapper.getNumberOfPillsInWheel() + "\nPillules");
        wvh.dateDeFin.setText("Fin de la prise dans " + pillsWrapper.getDaysUntilEnd() + " jours");
        wvh.dateDeRemplissage.setText("Remplissage nécessaire dans " + pillsWrapper.getDaysUntilRefill() + " jours");
        wvh.prises.setLines(pillsWrapper.getNumberOfPrises());
        wvh.prises.setText(pillsWrapper.getPrisesText());
        setExtendButtonClickListener(position, wvh, pillsWrapper);
        //setModifButtonClickListener(position, evh.modifButton);
        setSupprButtonClickListener(position, wvh.supprButton);
        if (!pillsWrapper.isExtended) wvh.extendedLayout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return listeDesRoues.size() +1 ; //Le bouton ne fait pas parti de listeDesRoues, on rajoute un pour le considérer quand même (voir getItemViewType)
    }


    // ********** Mapping des boutons ************ //

    public void setExtendButtonClickListener(final int buttonsParentPosition, final WheelViewHolder wvh, PillsWrapper pillsWrapper) {
        wvh.extendButton.setOnClickListener(v -> {
            if (pillsWrapper.isExtended) {
                wvh.extendedLayout.setVisibility(View.GONE);
                wvh.extendButton.setText("+");
            } else {
                wvh.extendedLayout.setVisibility(View.VISIBLE);
                wvh.extendButton.setText("-");
            }
            pillsWrapper.toggleExtension();
            wvh.wholeLayout.invalidate();
        }
        );
    }

    public void setModifButtonClickListener (final int buttonsParentPosition, Button button) {
        button.setOnClickListener(v -> {
            //wheelSelectorActivity.startActivityAjoutNouvellePrise();
        });
    }

    public void setSupprButtonClickListener (final int buttonsParentPosition, Button button) {
        button.setOnClickListener(v -> {
            SocketWrapper socketWrapper = new SocketWrapper(wheelSelectorActivity.ipAdress, wheelSelectorActivity.port, null, null, false, "Suppr" + listeDesRoues.get(buttonsParentPosition).getPillName());
            //Log.w("SUPRESSION", " carte " + listeDesRoues.get(buttonsParentPosition).getWheelNumber() + " à la position " + buttonsParentPosition);
            //listeDesRoues.remove(buttonsParentPosition);
            wheelSelectorActivity.removePill(buttonsParentPosition);

            notifyItemRemoved(buttonsParentPosition);
            notifyItemRangeRemoved(buttonsParentPosition, getItemCount());

            WheelAdapter adapter = (WheelAdapter)wheelSelectorActivity.recyclerView.getAdapter();
            wheelSelectorActivity.recyclerView.setAdapter(null);
            wheelSelectorActivity.recyclerView.setAdapter(adapter);


        }
        );
    }
}
